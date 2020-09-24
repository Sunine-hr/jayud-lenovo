package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    IOrderStatusService orderStatusService;

    @Autowired
    ILogisticsTrackService logisticsTrackService;

    @Override
    public String oprMainOrder(InputMainOrderForm form) {
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        if(form != null && form.getOrderId() != null){//修改
            //修改时也要返回主订单号
            OrderInfo oldOrder = baseMapper.selectById(form.getOrderId());
            orderInfo.setId(form.getOrderId());
            orderInfo.setOrderNo(oldOrder.getOrderNo());
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
        page.addOrder(OrderItem.asc("temp.id"));
        IPage<OrderInfoVO> pageInfo = baseMapper.findOrderInfoByPage(page, form);
        if("submit".equals(form.getCmd())){//处理流程
            List<OrderInfoVO> orderInfoVOS = pageInfo.getRecords();
            for (OrderInfoVO orderInfo : orderInfoVOS) {
                List<OrderStatusVO> orderStatusVOS = new ArrayList<>();
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("biz_code",orderInfo.getBizCode());
                List<OrderStatus> allProcess = orderStatusService.list(queryWrapper);//所有流程
                allProcess.sort((h1, h2) -> {//排序
                    if (h1.getFId().equals(h2.getFId())) {
                        return Integer.compare(h1.getSorts(), h2.getSorts());
                    }
                    return Integer.compare(h1.getFId(), h2.getFId());

                });
                allProcess.forEach(x ->{
                    OrderStatusVO orderStatus = new OrderStatusVO();
                    orderStatus.setId(x.getId());
                    orderStatus.setProcessName(x.getName());
                    orderStatus.setProcessCode(x.getIdCode());
                    orderStatus.setStatus();
                    if (x.getFId() == 0) {
                        orderStatusVOS.add(orderStatus);
                    } else {
                        orderStatusVOS.forEach(v -> {
                            if (v.getId() == x.getFId()) {
                                v.addChildren(orderStatus);
                            }
                        });
                    }
                });
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("main_order_id",orderInfo.getId());
                queryWrapper.eq("order_id","");
                List<LogisticsTrack> logisticsTracks = logisticsTrackService.list(queryWrapper);//已操作的流程
                Map<String, LogisticsTrack> logisticsTrackMap = logisticsTracks.stream().collect(Collectors.toMap(LogisticsTrack::getStatus, x -> x));
                if (!logisticsTracks.isEmpty()) {
                    orderStatusVOS.forEach(x -> {
                        if (logisticsTrackMap.keySet().contains(x.getProcessCode())) {
                            x.setStatusChangeTime(DateUtils.getLocalToStr(logisticsTrackMap.get(x.getProcessCode()).getOperatorTime()));
                            if (x.getChildren().isEmpty()) {
                                x.setStatus("3");
                            } else {
                                x.setStatus("2");
                                final boolean[] flag = {false};
                                x.getChildren().forEach(v -> {
                                    if (!flag[0]) {
                                        if (logisticsTrackMap.keySet().contains(v.getProcessCode())) {
                                            v.setStatus("3");
                                            v.setStatusChangeTime(DateUtils.getLocalToStr(logisticsTrackMap.get(x.getProcessCode()).getOperatorTime()));
                                        } else {
                                            flag[0] = true;
                                        }
                                    }
                                });
                                if (!flag[0]) {
                                    x.setStatus("3");
                                }
                            }
                        }
                    });
                }
                orderInfo.setStatusList(orderStatusVOS);
            }
        }
        return pageInfo;
    }

    @Override
    public InputMainOrderVO getMainOrderById(Long idValue) {
        return baseMapper.getMainOrderById(idValue);
    }

    @Override
    public Long getIdByOrderNo(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
        if(orderInfo == null){
            return 0L;
        }
        return orderInfo.getId();
    }


    @Override
    public boolean saveOrUpdateCost(InputCostForm form) {
        try {
            if (form == null || form.getMainOrderId() == null) {
                return false;
            }
            InputMainOrderVO inputOrderVO = getMainOrderById(form.getMainOrderId());
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
