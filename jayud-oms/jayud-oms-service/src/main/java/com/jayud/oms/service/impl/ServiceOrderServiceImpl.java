package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.ServiceOrderMapper;
import com.jayud.oms.model.bo.InputOrderServiceForm;
import com.jayud.oms.model.bo.InputOrderTakeAdrForm;
import com.jayud.oms.model.po.DeliveryAddress;
import com.jayud.oms.model.po.OrderTakeAdr;
import com.jayud.oms.model.po.ServiceOrder;
import com.jayud.oms.model.po.ServiceType;
import com.jayud.oms.model.vo.InputOrderServiceVO;
import com.jayud.oms.model.vo.InputOrderTakeAdrVO;
import com.jayud.oms.service.IDeliveryAddressService;
import com.jayud.oms.service.IOrderTakeAdrService;
import com.jayud.oms.service.IServiceOrderService;
import com.jayud.oms.service.IServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务单订单信息表 服务实现类
 * </p>
 *
 * @author
 * @since 2020-12-16
 */
@Service
public class ServiceOrderServiceImpl extends ServiceImpl<ServiceOrderMapper, ServiceOrder> implements IServiceOrderService {

    @Autowired
    private IServiceOrderService serviceOrderService;

    @Autowired
    private IOrderTakeAdrService orderTakeAdrService;

    @Autowired
    private IDeliveryAddressService deliveryAddressService;

    @Autowired
    private IServiceTypeService serviceTypeService;

    /**
     * 创建服务单订单
     * @param form
     * @return
     */
    @Override
    public boolean createOrder(InputOrderServiceForm form) {
        ServiceOrder serviceOrder = ConvertUtil.convert(form, ServiceOrder.class);
        if (serviceOrder == null) {
            return false;
        }
        List<InputOrderTakeAdrForm> orderTakeAdrForms1 = form.getTakeAdrForms1();
        for (InputOrderTakeAdrForm orderTakeAdrForm1 : orderTakeAdrForms1) {
            orderTakeAdrForm1.setOprType(Integer.valueOf(CommonConstant.VALUE_1));
        }
        List<InputOrderTakeAdrForm> orderTakeAdrForms2 = form.getTakeAdrForms2();
        for (InputOrderTakeAdrForm orderTakeAdrForm2 : orderTakeAdrForms2) {
            orderTakeAdrForm2.setOprType(Integer.valueOf(CommonConstant.VALUE_2));
        }

        List<InputOrderTakeAdrForm> orderTakeAdrForms = new ArrayList<>();
        orderTakeAdrForms.addAll(orderTakeAdrForms1);
        orderTakeAdrForms.addAll(orderTakeAdrForms2);

        String orderNo = form.getOrderNo();
        if (serviceOrder.getId() != null) {//修改
            //修改时,先把以前的收货信息清空
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no", form.getOrderNo());
            serviceOrderService.remove(queryWrapper);//删除货物信息
            serviceOrder.setUpdatedTime(LocalDateTime.now());
            serviceOrder.setUpdatedUser(form.getLoginUser());
        } else {//新增
            //生成订单号
            orderNo = StringUtils.loadNum(CommonConstant.F, 12);
            while (true) {
                if (!isExistOrder(orderNo)) {//重复
                    orderNo = StringUtils.loadNum(CommonConstant.F, 12);
                } else {
                    break;
                }
            }
            serviceOrder.setOrderNo(orderNo);
            serviceOrder.setCreatedUser(form.getLoginUser());
            serviceOrder.setCreatedTime(LocalDateTime.now());

        }
        for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("contacts",inputOrderTakeAdrForm.getContacts());
            queryWrapper.eq("phone",inputOrderTakeAdrForm.getPhone());
            queryWrapper.eq("address",inputOrderTakeAdrForm.getAddress());
            DeliveryAddress deliveryAddress1 = null;
            deliveryAddress1 = deliveryAddressService.getOne(queryWrapper);
            if(deliveryAddress1==null){
                DeliveryAddress deliveryAddress = new DeliveryAddress();
                deliveryAddress.setContacts(inputOrderTakeAdrForm.getContacts());
                deliveryAddress.setPhone(inputOrderTakeAdrForm.getPhone());
                deliveryAddress.setAddress(inputOrderTakeAdrForm.getAddress());
                deliveryAddressService.saveOrUpdate(deliveryAddress);
                deliveryAddress1 = deliveryAddressService.getOne(queryWrapper);
            }

            OrderTakeAdr orderTakeAdr = ConvertUtil.convert(inputOrderTakeAdrForm, OrderTakeAdr.class);
            orderTakeAdr.setOrderNo(orderNo);
            orderTakeAdr.setDeliveryId(deliveryAddress1.getId().longValue());
            orderTakeAdr.setCreateTime(LocalDateTime.now());
            orderTakeAdr.setCreateUser(form.getLoginUser());
            orderTakeAdr.setTakeTime(inputOrderTakeAdrForm.getTakeTimeStr());
            boolean b = orderTakeAdrService.saveOrUpdate(orderTakeAdr);
            if(b==false){
                return b;
            }
        }
        boolean result = serviceOrderService.saveOrUpdate(serviceOrder);
        return result;
    }

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_NO, orderNo);
        List<ServiceOrder> orderTransports = baseMapper.selectList(queryWrapper);
        if (orderTransports == null || orderTransports.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public InputOrderServiceVO getSerOrderDetails(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no",orderNo);
        ServiceOrder serviceOrder = baseMapper.selectOne(queryWrapper);
        InputOrderServiceVO orderServiceVO = new InputOrderServiceVO();
        orderServiceVO.setId(serviceOrder.getId());
        orderServiceVO.setOrderNo(serviceOrder.getOrderNo());
        orderServiceVO.setMainOrderNo(serviceOrder.getMainOrderNo());
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("id",serviceOrder.getType());
        ServiceType serviceType = serviceTypeService.getOne(queryWrapper1);
        orderServiceVO.setTypeDesc(serviceType.getType());
        orderServiceVO.setType(serviceOrder.getType());
        orderServiceVO.setAssociatedOrder(serviceOrder.getAssociatedOrder());

        //获取提货收货地址信息
        List<InputOrderTakeAdrVO> list = orderTakeAdrService.findTakeGoodsInfo(serviceOrder.getOrderNo());
        List<InputOrderTakeAdrVO> takeAdrForms1 = new ArrayList<>();
        List<InputOrderTakeAdrVO> takeAdrForms2 = new ArrayList<>();

        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : list) {
            if(inputOrderTakeAdrVO.getOprType().equals(Integer.valueOf(CommonConstant.VALUE_1))){
                takeAdrForms1.add(inputOrderTakeAdrVO);
            }else{
                takeAdrForms2.add(inputOrderTakeAdrVO);
            }
        }
        orderServiceVO.setTakeAdrForms1(takeAdrForms1);
        orderServiceVO.setTakeAdrForms2(takeAdrForms2);

        return orderServiceVO;
    }
}
