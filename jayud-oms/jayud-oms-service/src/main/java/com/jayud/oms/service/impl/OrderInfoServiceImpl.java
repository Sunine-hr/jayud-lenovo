package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.IOrderPaymentCostService;
import com.jayud.oms.service.IOrderReceivableCostService;
import com.jayud.oms.service.IProductBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 主订单基础数据表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    IProductBizService productBizService;

    @Autowired
    IOrderPaymentCostService paymentCostService;

    @Autowired
    IOrderReceivableCostService receivableCostService;

    @Override
    public String oprMainOrder(InputOrderForm form) {
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        if(form != null && form.getOrderNo() != null){//修改
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
            orderInfo.setUpTime(LocalDateTime.now());
            orderInfo.setUpUser(getLoginUser());
        }else {//新增
            //生成主订单号
            String orderNo = StringUtils.loadNum("MAIN",12);
            while (true){
                if(!isExistOrder(orderNo)){//重复
                    orderNo = StringUtils.loadNum("MAIN",12);
                }else {
                    break;
                }
            }
            orderInfo.setOrderNo(orderNo);
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setCreatedUser(getLoginUser());
            if("preSubmit".equals(form.getCmd())) {
                orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_2.getCode()));
            }else if("submit".equals(form.getCmd())){
                orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
            }
        }
        saveOrUpdate(orderInfo);
        return orderInfo.getOrderNo();
    }

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        List<OrderInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        if(orderInfoList == null || orderInfoList.size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form) {
        //定义分页参数
        Page<OrderInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("oi.id"));
        IPage<OrderInfoVO> pageInfo = baseMapper.findOrderInfoByPage(page, form);
        return pageInfo;
    }

    @Override
    public InputOrderVO getMainOrderById(Long idValue) {
        return baseMapper.getMainOrderById(idValue);
    }

    @Override
    public boolean saveOrUpdateCost(InputCostForm form) {
        try {
            if (form == null || form.getMainOrderId() == null) {
                return false;
            }
            InputOrderVO inputOrderVO = getMainOrderById(form.getMainOrderId());
            if (inputOrderVO == null) {
                return false;
            }
            List<OrderPaymentCost> orderPaymentCosts = new ArrayList<>();
            List<OrderReceivableCost> orderReceivableCosts = new ArrayList<>();
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            orderPaymentCosts = ConvertUtil.convertList(paymentCostForms, OrderPaymentCost.class);
            orderReceivableCosts = ConvertUtil.convertList(receivableCostForms, OrderReceivableCost.class);
            for (OrderPaymentCost orderPaymentCost : orderPaymentCosts) {//应付费用
                orderPaymentCost.setOptName(getLoginUser());
                orderPaymentCost.setOptTime(LocalDateTime.now());
                orderPaymentCost.setCreatedTime(LocalDateTime.now());
                orderPaymentCost.setCreatedUser(getLoginUser());
                if ("preSubmit_main".equals(form.getCmd())) {
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd())) {
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                }
            }
            for (OrderReceivableCost orderReceivableCost : orderReceivableCosts) {//应收费用
                orderReceivableCost.setOptName(getLoginUser());
                orderReceivableCost.setOptTime(LocalDateTime.now());
                orderReceivableCost.setCreatedTime(LocalDateTime.now());
                orderReceivableCost.setCreatedUser(getLoginUser());
                if ("noSubmit".equals(form.getCmd())) {
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit".equals(form.getCmd())) {
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                }
            }
            paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
            receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public InputCostVO getCostDetail(Long id) {
        List<OrderPaymentCost> orderPaymentCosts = paymentCostService.list();
        List<OrderReceivableCost> orderReceivableCosts = receivableCostService.list();
        List<InputPaymentCostVO> inputPaymentCostVOS = ConvertUtil.convertList(orderPaymentCosts,InputPaymentCostVO.class);
        List<InputReceivableCostVO> inputReceivableCostVOS = ConvertUtil.convertList(orderReceivableCosts,InputReceivableCostVO.class);
        InputCostVO inputCostVO = new InputCostVO();
        inputCostVO.setPaymentCostList(inputPaymentCostVOS);
        inputCostVO.setReceivableCostList(inputReceivableCostVOS);
        return inputCostVO;
    }

    @Override
    public boolean auditCost(AuditCostForm form) {
        try {
           List<Long> paymentIds = form.getPaymentIds();
           List<Long> receivableIds = form.getReceivableIds();
           List<OrderPaymentCost> orderPaymentCosts = new ArrayList<>();
           List<OrderReceivableCost> orderReceivableCosts = new ArrayList<>();
           for(Long id : paymentIds){
               OrderPaymentCost orderPaymentCost = new OrderPaymentCost();
               //参数校验
               if(id == null || "".equals(id) || (!form.getStatus().equals(1) && !form.getStatus().equals(2))){
                   return false;
               }
               orderPaymentCost.setId(id);
               orderPaymentCost.setStatus(Integer.valueOf(form.getStatus()));
               orderPaymentCost.setRemarks(form.getRemarks());
               orderPaymentCosts.add(orderPaymentCost);
           }
            for(Long id : receivableIds){
                OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                //参数校验
                if(id == null || "".equals(id) || (!form.getStatus().equals(1) && !form.getStatus().equals(2))){
                    return false;
                }
                orderReceivableCost.setId(id);
                orderReceivableCost.setStatus(Integer.valueOf(form.getStatus()));
                orderReceivableCost.setRemarks(form.getRemarks());
                orderReceivableCosts.add(orderReceivableCost);
            }
            paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
            receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return loginUser;
    }
}
