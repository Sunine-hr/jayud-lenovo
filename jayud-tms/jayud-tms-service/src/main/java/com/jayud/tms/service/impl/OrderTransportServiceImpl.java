package com.jayud.tms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.mapper.OrderTransportMapper;
import com.jayud.tms.model.bo.InputOrderTakeAdrForm;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.InputOrderTakeAdrVO;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.model.vo.SendCarPdfVO;
import com.jayud.tms.service.IDeliveryAddressService;
import com.jayud.tms.service.IOrderTakeAdrService;
import com.jayud.tms.service.IOrderTransportService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 中港运输订单 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTransportServiceImpl extends ServiceImpl<OrderTransportMapper, OrderTransport> implements IOrderTransportService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    IOrderTakeAdrService orderTakeAdrService;

    @Autowired
    IDeliveryAddressService deliveryAddressService;

    @Autowired
    IOrderTransportService orderTransportService;


    @Override
    public boolean createOrderTransport(InputOrderTransportForm form) {
        OrderTransport orderTransport = ConvertUtil.convert(form,OrderTransport.class);
        if(orderTransport == null){
            return false;
        }
        List<InputOrderTakeAdrForm> orderTakeAdrForms1 = form.getTakeAdrForms1();
        List<InputOrderTakeAdrForm> orderTakeAdrForms2 = form.getTakeAdrForms2();
        List<InputOrderTakeAdrForm> orderTakeAdrForms = new ArrayList<>();

        //值为空的不进行保存
        List<InputOrderTakeAdrForm> handleTakeAdrForms = new ArrayList<>();
        handleTakeAdrForms.addAll(orderTakeAdrForms1);
        handleTakeAdrForms.addAll(orderTakeAdrForms2);
        for(InputOrderTakeAdrForm inputOrderTakeAdrForm : handleTakeAdrForms){
            //如果收货提货信息的联系人都不填,不保存该信息,视为恶意操作
            if(!StringUtil.isNullOrEmpty(inputOrderTakeAdrForm.getContacts())){
                orderTakeAdrForms.add(inputOrderTakeAdrForm);
            }
        }

        if(orderTransport.getId() != null){//修改
            //修改时,先把以前的收货提货地址清空
            List<Long> ids = new ArrayList<>();
            for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {
               Long deliveryId = inputOrderTakeAdrForm.getDeliveryId();
               ids.add(deliveryId);
            }
            if(ids.size() > 0) {
                deliveryAddressService.removeByIds(ids);//删除地址信息
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no",form.getOrderNo());
            orderTakeAdrService.remove(queryWrapper);//删除货物信息
            orderTransport.setUpdatedTime(LocalDateTime.now());
            orderTransport.setUpdatedUser(getLoginUser());
        }else {//新增
            //生成订单号
            String orderNo = StringUtils.loadNum(CommonConstant.T,12);
            while (true){
                if(!isExistOrder(orderNo)){//重复
                    orderNo = StringUtils.loadNum(CommonConstant.T,12);
                }else {
                    break;
                }
            }
            orderTransport.setOrderNo(orderNo);
            orderTransport.setCreatedUser(getLoginUser());
        }
        for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {
            OrderTakeAdr orderTakeAdr = ConvertUtil.convert(inputOrderTakeAdrForm,OrderTakeAdr.class);
            DeliveryAddress deliveryAddress = ConvertUtil.convert(inputOrderTakeAdrForm,DeliveryAddress.class);
            deliveryAddress.setStatus(Integer.valueOf(CommonConstant.VALUE_0));
            deliveryAddress.setCreateUser(getLoginUser());
            deliveryAddressService.save(deliveryAddress);
            orderTakeAdr.setDeliveryId(deliveryAddress.getId());
            orderTakeAdr.setOrderNo(orderTransport.getOrderNo());
            orderTakeAdrService.save(orderTakeAdr);
        }
        orderTransport.setCntrPic(StringUtils.getFileStr(form.getCntrPics()));
        orderTransport.setCntrPicName(StringUtils.getFileNameStr(form.getCntrPics()));
        orderTransport.setStatus(OrderStatusEnum.TMS_T_0.getCode());
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        return result;
    }

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_NO,orderNo);
        List<OrderTransport> orderTransports = baseMapper.selectList(queryWrapper);
        if(orderTransports == null || orderTransports.size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public InputOrderTransportVO getOrderTransport(String mainOrderNo) {
        InputOrderTransportVO inputOrderTransportVO = baseMapper.getOrderTransport(mainOrderNo);
        if(inputOrderTransportVO == null){
            return new InputOrderTransportVO();
        }
        //获取提货/送货地址
        List<InputOrderTakeAdrVO> inputOrderTakeAdrVOS = orderTakeAdrService.findTakeGoodsInfo(inputOrderTransportVO.getOrderNo());
        List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();
        List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();
        Integer totalAmount = 0;//总件数
        Double totalWeight = 0.0;//总重量
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : inputOrderTakeAdrVOS) {
            if(CommonConstant.VALUE_1.equals(inputOrderTakeAdrVO.getOprType())){//提货
                orderTakeAdrForms1.add(inputOrderTakeAdrVO);
                totalAmount = totalAmount + inputOrderTakeAdrVO.getPieceAmount();
                totalWeight = totalWeight + inputOrderTakeAdrVO.getWeight();
            }else {
                orderTakeAdrForms2.add(inputOrderTakeAdrVO);  //送货
            }
        }
        inputOrderTransportVO.setTotalAmount(totalAmount);
        inputOrderTransportVO.setTotalWeight(totalWeight);
        inputOrderTransportVO.setOrderTakeAdrForms1(orderTakeAdrForms1);
        inputOrderTransportVO.setOrderTakeAdrForms2(orderTakeAdrForms2);
        return inputOrderTransportVO;
    }

    @Override
    public IPage<OrderTransportVO> findTransportOrderByPage(QueryOrderTmsForm form) {
        //定义分页参数
        Page<OrderTransportVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("ot.id"));
        IPage<OrderTransportVO> pageInfo = baseMapper.findTransportOrderByPage(page, form);
        return pageInfo;
    }

    @Override
    public SendCarPdfVO initPdfData(String orderNo, String classCode) {
        // TODO
        return null;
    }

    /**
     * 当前登录用户
     * @return
     */
    private String getLoginUser(){
        return redisUtils.get("loginUser",100);
    }
}
