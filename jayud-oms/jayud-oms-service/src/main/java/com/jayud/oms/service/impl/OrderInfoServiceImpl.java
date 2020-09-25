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

    @Autowired
    ICurrencyInfoService currencyInfoService;

    @Autowired
    ICostInfoService costInfoService;

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
        if("submit".equals(form.getCmd())){//是否有录入费用
            List<OrderInfoVO> orderInfoVOS = pageInfo.getRecords();
            for (OrderInfoVO orderInfo : orderInfoVOS) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("main_order_no",orderInfo.getOrderNo());
                List<OrderPaymentCost> paymentCosts = paymentCostService.list(queryWrapper);
                List<OrderReceivableCost> receivableCosts = receivableCostService.list(queryWrapper);
                int num = 0;
                if(paymentCosts != null){
                    num = num + paymentCosts.size();
                }
                if(receivableCosts != null){
                    num = num + receivableCosts.size();
                }
                if(num > 0) {
                    orderInfo.setCost(true);
                }else {
                    orderInfo.setCost(false);
                }
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
                orderPaymentCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderPaymentCost.setOrderNo(form.getOrderNo());
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
                orderReceivableCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderReceivableCost.setOrderNo(form.getOrderNo());
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
    public InputCostVO getCostDetail(GetCostDetailForm form) {
        if(form.getCmd() == null || "".equals(form.getCmd()) || form.getMainOrderNo() == null ||
           "".equals(form.getMainOrderNo())){
            return null;//参数异常
        }
        List<OrderPaymentCost> orderPaymentCosts = new ArrayList<>();
        List<OrderReceivableCost> orderReceivableCosts = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        if("main_cost".equals(form.getCmd())){
            queryWrapper.eq("main_order_no",form.getMainOrderNo());
            queryWrapper.eq("order_no","");
        }else if("sub_cost".equals(form.getCmd())){
            queryWrapper.eq("main_order_no",form.getMainOrderNo());
            queryWrapper.ne("order_no","");
        }
        orderPaymentCosts = paymentCostService.list(queryWrapper);
        orderReceivableCosts = receivableCostService.list(queryWrapper);
        List<InputPaymentCostVO> inputPaymentCostVOS = ConvertUtil.convertList(orderPaymentCosts,InputPaymentCostVO.class);
        List<InputReceivableCostVO> inputReceivableCostVOS = ConvertUtil.convertList(orderReceivableCosts,InputReceivableCostVO.class);
        //设置费用类型/应收项目/币种名称
        for (InputPaymentCostVO inputPaymentCost : inputPaymentCostVOS) {
            QueryWrapper currencyCode = new QueryWrapper();
            currencyCode.eq("currency_code",inputPaymentCost.getCurrencyCode());
            CurrencyInfo currencyInfo = currencyInfoService.getOne(currencyCode);
            QueryWrapper costCode = new QueryWrapper();
            costCode.eq("id_code",inputPaymentCost.getCostCode());
            CostInfo costInfo = costInfoService.getOne(costCode);
            inputPaymentCost.setCostName(costInfo.getName());
            inputPaymentCost.setCurrencyName(currencyInfo.getCurrencyName());
        }
        for (InputReceivableCostVO inputReceivableCost : inputReceivableCostVOS) {
            QueryWrapper currencyCode = new QueryWrapper();
            currencyCode.eq("currency_code",inputReceivableCost.getCurrencyCode());
            CurrencyInfo currencyInfo = currencyInfoService.getOne(currencyCode);
            QueryWrapper costCode = new QueryWrapper();
            costCode.eq("id_code",inputReceivableCost.getCostCode());
            CostInfo costInfo = costInfoService.getOne(costCode);
            inputReceivableCost.setCostName(costInfo.getName());
            inputReceivableCost.setCurrencyName(currencyInfo.getCurrencyName());
        }
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
           paymentCostService.saveOrUpdateBatch(orderPaymentCosts);
           receivableCostService.saveOrUpdateBatch(orderReceivableCosts);
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
        queryWrapper.eq("biz_code",form.getBizCode());
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
        queryWrapper.eq("main_order_id",form.getMainOrderId());
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
        return orderStatusVOS;
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
