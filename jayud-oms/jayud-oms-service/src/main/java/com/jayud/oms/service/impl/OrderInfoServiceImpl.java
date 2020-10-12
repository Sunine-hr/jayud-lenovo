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
import com.jayud.oms.feign.CustomsClient;
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

    @Autowired
    ICurrencyInfoService currencyInfoService;

    @Autowired
    ICostInfoService costInfoService;

    @Autowired
    CustomsClient customsClient;

    @Autowired
    ICostTypeService costTypeService;

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
            String orderNo = StringUtils.loadNum("M",12);
            while (true){
                if(!isExistOrder(orderNo)){//重复
                    orderNo = StringUtils.loadNum("M",12);
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
        page.addOrder(OrderItem.desc("temp.id"));
        IPage<OrderInfoVO> pageInfo = baseMapper.findOrderInfoByPage(page, form);
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
            //业务场景:暂存时提交所有未提交审核的信息,为了避免用户删除一条然后又添加的情况，每次暂存前先把原来未提交审核的清空
            if("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())){
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("main_order_no",inputOrderVO.getOrderNo());
                queryWrapper.isNull("order_no");
                queryWrapper.eq("status",OrderStatusEnum.COST_1.getCode());
                paymentCostService.remove(queryWrapper);
                receivableCostService.remove(queryWrapper);
            }
            for (OrderPaymentCost orderPaymentCost : orderPaymentCosts) {//应付费用
                orderPaymentCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderPaymentCost.setOrderNo(form.getOrderNo());
                if ("preSubmit_main".equals(form.getCmd())) {
                    orderPaymentCost.setCreatedTime(LocalDateTime.now());
                    orderPaymentCost.setCreatedUser(getLoginUser());
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd())) {
                    orderPaymentCost.setOptName(getLoginUser());
                    orderPaymentCost.setOptTime(LocalDateTime.now());
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                }
            }
            for (OrderReceivableCost orderReceivableCost : orderReceivableCosts) {//应收费用
                orderReceivableCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderReceivableCost.setOrderNo(form.getOrderNo());
                if ("preSubmit_main".equals(form.getCmd())) {
                    orderReceivableCost.setCreatedTime(LocalDateTime.now());
                    orderReceivableCost.setCreatedUser(getLoginUser());
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd())) {
                    orderReceivableCost.setOptName(getLoginUser());
                    orderReceivableCost.setOptTime(LocalDateTime.now());
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                }
            }
            if(orderPaymentCosts.size() > 0) {
                paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
            }
            if(orderReceivableCosts.size() > 0) {
                receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public InputCostVO getCostDetail(GetCostDetailForm form) {
        if(form.getCmd() == null || "".equals(form.getCmd()) || form.getMainOrderNo() == null ||
           "".equals(form.getMainOrderNo())){
            return null;//参数异常
        }
        List<InputPaymentCostVO> inputPaymentCostVOS = paymentCostService.findPaymentCost(form);
        List<InputReceivableCostVO> inputReceivableCostVOS = receivableCostService.findReceivableCost(form);
        InputCostVO inputCostVO = new InputCostVO();
        inputCostVO.setPaymentCostList(inputPaymentCostVOS);
        inputCostVO.setReceivableCostList(inputReceivableCostVOS);
        return inputCostVO;
    }

    @Override
    public boolean auditCost(AuditCostForm form) {
        try {
           List<OrderPaymentCost> orderPaymentCosts = form.getPaymentCosts();
           List<OrderReceivableCost> orderReceivableCosts = form.getReceivableCosts();
           for(OrderPaymentCost paymentCost : orderPaymentCosts){
               paymentCost.setStatus(Integer.valueOf(form.getStatus()));
               paymentCost.setRemarks(form.getRemarks());
           }
           for(OrderReceivableCost receivableCost : orderReceivableCosts){
               receivableCost.setStatus(Integer.valueOf(form.getStatus()));
               receivableCost.setRemarks(form.getRemarks());
           }
           if(orderPaymentCosts != null && orderPaymentCosts.size() > 0){
               paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
           }
           if(orderReceivableCosts != null && orderReceivableCosts.size() > 0){
               receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
           }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<OrderStatusVO> handleProcess(QueryOrderStatusForm form) {
        List<OrderStatusVO> orderStatusVOS = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_code", form.getBizCode());
        queryWrapper.eq("status", "1");
        List<OrderStatus> allProcess = orderStatusService.list(queryWrapper);//所有流程
        allProcess.sort((h1, h2) -> {//排序
            if (h1.getFId().equals(h2.getFId())) {
                return Integer.compare(h1.getSorts(), h2.getSorts());
            }
            return Integer.compare(h1.getFId(), h2.getFId());

        });
        allProcess.forEach(x -> {
            OrderStatusVO orderStatus = new OrderStatusVO();
            orderStatus.setId(x.getId());
            orderStatus.setProcessName(x.getName());
            orderStatus.setProcessCode(x.getIdCode());
            orderStatus.setContainState(x.getContainState());
            orderStatus.setSorts(x.getSorts());
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
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        //根据主订单ID获取子订单数量
        int customsNum = 0;//纯报关单数量
        if (OrderStatusEnum.CBG.getCode().equals(form.getBizCode()) && inputMainOrderVO != null) {
            customsNum = customsClient.getCustomsOrderNum(inputMainOrderVO.getOrderNo()).getData();
        }
        //以后有其他的会逐渐增加 TODO
        int finalCustomsNum = customsNum;
        orderStatusVOS.forEach(x -> {
            //循环处理主流程节点
            String containState = x.getContainState();
            if (containState != null && !"".equals(containState)) {
                //循环处理子节点，如纯报关
                if (!x.getChildren().isEmpty()) {
                    for (int i = 0; i < x.getChildren().size(); i++) {
                        OrderStatusVO subOrder = x.getChildren().get(i);
                        String subContainState = subOrder.getContainState();
                        if (subContainState != null && !"".equals(subContainState)) {
                            //原则上子订单所包含的操作状态只有一个
                            //String[] subContainStates = subContainState.split(",");
                            QueryWrapper subParam = new QueryWrapper();
                            subParam.eq("main_order_id", form.getMainOrderId());
                            subParam.eq("status", subContainState);
                            queryWrapper.isNotNull("order_id");//处理已下单
                            subParam.orderByDesc("created_time");
                            List<LogisticsTrack> subTrack = logisticsTrackService.list(subParam);//已操作的子流程
                            //若子订单流程记录小于子订单数，说明主流程节点状态为进行中
                            Integer sorts = x.getChildren().get(i).getSorts();
                            if (subTrack == null || subTrack.size() == 0) {
                                if(sorts != 1){
                                    x.setStatus("2");
                                }else {//如果子流程的第一个都没有已操作记录，说明主流程未进行，后面的子流程就没必要循环了
                                    break;
                                }
                            } else if (subTrack != null && subTrack.size() < finalCustomsNum) {
                                x.setStatus("2");//进行中
                                subOrder.setStatus("2");
                            } else {
                                //子节点循环中最后一个流程节点未操作完毕
                                if (!subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                    x.setStatus("2");//进行中
                                } else {
                                    x.setStatus("3");//已完成
                                    x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                }
                                subOrder.setStatus("3");
                                subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                            }
                        }
                    }
                } else {//没有子节点流程的
                    String[] containStates = containState.split(",");
                    for (int i = 0; i < containStates.length; i++) {
                        QueryWrapper mainParam = new QueryWrapper();
                        mainParam.eq("main_order_id", form.getMainOrderId());
                        mainParam.eq("status", containStates[i]);
                        queryWrapper.isNotNull("order_id");
                        mainParam.orderByDesc("created_time");
                        List<LogisticsTrack> subTrack = logisticsTrackService.list(mainParam);//已操作的子流程
                        //若子订单流程记录小于子订单数，说明报关中状态为进行中
                        if (subTrack == null || subTrack.size() == 0) {
                            x.setStatus("2");//报关进行中
                        } else if (subTrack != null && subTrack.size() < finalCustomsNum) {
                            x.setStatus("2");//报关进行中
                        } else {
                            x.setStatus("3");
                            x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                        }
                    }
                }
            }

        });
        return orderStatusVOS;
    }

    @Override
    public List<OrderStatusVO> handleSubProcess(HandleSubProcessForm form) {
        List<OrderStatusVO> orderStatusVOS = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_code", form.getClassCode());
        queryWrapper.eq("status", "1");
        List<OrderStatus> allProcess = orderStatusService.list(queryWrapper);//所有流程
        allProcess.sort((h1, h2) -> {//排序
            if (h1.getFId().equals(h2.getFId())) {
                return Integer.compare(h1.getSorts(), h2.getSorts());
            }
            return Integer.compare(h1.getFId(), h2.getFId());

        });
        allProcess.forEach(x -> {
            OrderStatusVO orderStatus = new OrderStatusVO();
            orderStatus.setId(x.getId());
            orderStatus.setProcessName(x.getName());
            orderStatus.setProcessCode(x.getIdCode());
            orderStatus.setContainState(x.getContainState());
            orderStatus.setSorts(x.getSorts());
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
        orderStatusVOS.forEach(x -> {
            //循环处理主流程节点
            String containState = x.getContainState();
            if (containState != null && !"".equals(containState)) {
                //循环处理子节点，如纯报关
                if (!x.getChildren().isEmpty()) {
                    for (int i = 0; i < x.getChildren().size(); i++) {
                        OrderStatusVO subOrder = x.getChildren().get(i);
                        String subContainState = subOrder.getContainState();
                        if (subContainState != null && !"".equals(subContainState)) {
                            QueryWrapper subParam = new QueryWrapper();
                            subParam.eq("order_id", form.getOrderId());
                            subParam.eq("status", subContainState);
                            subParam.orderByDesc("created_time");
                            List<LogisticsTrack> subTrack = logisticsTrackService.list(subParam);//已操作的子流程
                            Integer sorts = x.getChildren().get(i).getSorts();
                            if (subTrack == null || subTrack.size() == 0) {
                                if (sorts != 1) {
                                    x.setStatus("2");
                                } else {//如果子流程的第一个都没有已操作记录，说明主流程未进行，后面的子流程就没必要循环了
                                    break;
                                }
                            } else {
                                LogisticsTrack logisticsTrack = subTrack.get(0);//最新的状态
                                if (i == 0 && logisticsTrack != null) {
                                    subOrder.setStatus("3");//已完成
                                    subOrder.setStatusChangeTime(DateUtils.getLocalToStr(logisticsTrack.getOperatorTime()));
                                } else if (i > 0 && logisticsTrack != null) {
                                    //获取上一节点流程的创建时间
                                    String preSubContainState = x.getChildren().get(i - 1).getContainState();
                                    if (subContainState != null && !"".equals(subContainState)) {
                                        String[] subContainStates = preSubContainState.split(",");
                                        QueryWrapper preSubOrderQuery = new QueryWrapper();
                                        preSubOrderQuery.eq("order_id", form.getOrderId());
                                        preSubOrderQuery.in("status", subContainStates);
                                        preSubOrderQuery.orderByDesc("created_time");
                                        List<LogisticsTrack> preLogisticsTracks = logisticsTrackService.list(preSubOrderQuery);//已操作的主流程
                                        if (!preLogisticsTracks.isEmpty()) {
                                            LocalDateTime preCreateTime = preLogisticsTracks.get(0).getCreatedTime();
                                            if (logisticsTrack.getCreatedTime().compareTo(preCreateTime) >= 0) {
                                                subOrder.setStatus("3");//已完成
                                                subOrder.setStatusChangeTime(DateUtils.getLocalToStr(logisticsTrack.getOperatorTime()));
                                            }
                                        }
                                    }
                                }
                                //子节点循环中最后一个流程节点未操作完毕
                                if (!subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())) {
                                    x.setStatus("2");//进行中
                                } else {
                                    x.setStatus("3");//已完成
                                    x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                }
                            }
                        }
                    }
                } else {//没有子节点流程的
                    String[] containStates = containState.split(",");
                    for (int i = 0; i < containStates.length; i++) {
                        QueryWrapper mainParam = new QueryWrapper();
                        mainParam.eq("order_id", form.getOrderId());
                        mainParam.eq("status", containStates[i]);
                        queryWrapper.isNotNull("order_id");
                        mainParam.orderByDesc("created_time");
                        List<LogisticsTrack> subTrack = logisticsTrackService.list(mainParam);//已操作的子流程
                        //若子订单流程记录小于子订单数，说明报关中状态为进行中
                        if (subTrack == null || subTrack.size() == 0) {
                            x.setStatus("2");//报关进行中
                        } else {
                            x.setStatus("3");
                            x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                        }
                    }
                }
            }

        });
        return orderStatusVOS;
    }

    @Override
    public InputOrderVO getOrderDetail(GetOrderDetailForm form) {
        customsClient.getCustomsDetail(null);
        return null;
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
