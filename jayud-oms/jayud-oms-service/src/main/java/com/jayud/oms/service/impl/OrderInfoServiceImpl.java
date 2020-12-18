package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.CustomsClient;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.mapper.OrderInfoMapper;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.netty.util.internal.StringUtil;
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
    TmsClient tmsClient;

    @Autowired
    ICostTypeService costTypeService;

    @Autowired
    FileClient fileClient;

    @Autowired
    ICustomerInfoService customerInfoService;

    @Override
    public String oprMainOrder(InputMainOrderForm form) {
        OrderInfo orderInfo = ConvertUtil.convert(form, OrderInfo.class);
        if(form != null && form.getOrderId() != null){//修改
            //修改时也要返回主订单号
            OrderInfo oldOrder = baseMapper.selectById(form.getOrderId());
            orderInfo.setId(form.getOrderId());
            orderInfo.setOrderNo(oldOrder.getOrderNo());
            orderInfo.setUpTime(LocalDateTime.now());
            orderInfo.setUpUser(UserOperator.getToken());
        }else {//新增
            //生成主订单号
            String orderNo = StringUtils.loadNum(CommonConstant.M,12);
            while (true){
                if(!isExistOrder(orderNo)){//重复
                    orderNo = StringUtils.loadNum(CommonConstant.M,12);
                }else {
                    break;
                }
            }
            orderInfo.setOrderNo(orderNo);
            orderInfo.setCreateTime(LocalDateTime.now());
            orderInfo.setCreatedUser(UserOperator.getToken());
        }
        if(CommonConstant.PRE_SUBMIT.equals(form.getCmd())) {
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_2.getCode()));
        }else if(CommonConstant.SUBMIT.equals(form.getCmd()) && CommonConstant.VALUE_1.equals(form.getIsDataAll())){
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
        }else if(CommonConstant.SUBMIT.equals(form.getCmd()) && CommonConstant.VALUE_0.equals(form.getIsDataAll())){
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_4.getCode()));
        }else {
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode()));
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
        IPage<OrderInfoVO> pageInfo = null;
        if(CommonConstant.GO_CUSTOMS_AUDIT.equals(form.getCmd())){
            //定义排序规则
            page.addOrder(OrderItem.desc("oi.id"));
            pageInfo = baseMapper.findGoCustomsAuditByPage(page,form);
        } else {
            //定义排序规则
            page.addOrder(OrderItem.desc("temp.id"));
            pageInfo = baseMapper.findOrderInfoByPage(page, form);
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
            List<InputPaymentCostForm> paymentCostForms = form.getPaymentCostList();
            List<InputReceivableCostForm> receivableCostForms = form.getReceivableCostList();
            List<InputPaymentCostForm> newPaymentCostForms = new ArrayList<>();
            List<InputReceivableCostForm> newReceivableCostForms = new ArrayList<>();
            //1.如果是暂存情况下,已提交的费用不再次处理
            //2.空数据不进行保存处理
            //3.已审核通过的不再次处理
            for (InputPaymentCostForm inputPaymentCost : paymentCostForms) {
                if((!OrderStatusEnum.COST_2.getCode().equals(inputPaymentCost.getStatus())) &&
                   !StringUtil.isNullOrEmpty(inputPaymentCost.getCostCode()) &&
                        (!OrderStatusEnum.COST_3.getCode().equals(inputPaymentCost.getStatus()))){
                    newPaymentCostForms.add(inputPaymentCost);
                }
            }
            for (InputReceivableCostForm inputReceivableCost : receivableCostForms) {
                if((!OrderStatusEnum.COST_2.getCode().equals(inputReceivableCost.getStatus())) &&
                   !StringUtil.isNullOrEmpty(inputReceivableCost.getCostCode()) &&
                        (!OrderStatusEnum.COST_3.getCode().equals(inputReceivableCost.getStatus()))){
                    newReceivableCostForms.add(inputReceivableCost);
                }
            }
            List<OrderPaymentCost> orderPaymentCosts = ConvertUtil.convertList(newPaymentCostForms, OrderPaymentCost.class);
            List<OrderReceivableCost> orderReceivableCosts = ConvertUtil.convertList(newReceivableCostForms, OrderReceivableCost.class);
            //业务场景:暂存时提交所有未提交审核的信息,为了避免用户删除一条然后又添加的情况，每次暂存前先把原来未提交审核的清空
            if("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())){
                QueryWrapper queryWrapper = new QueryWrapper();
                if("preSubmit_main".equals(form.getCmd())){
                    queryWrapper.eq("main_order_no",inputOrderVO.getOrderNo());
                    queryWrapper.isNull("order_no");
                }
                if("preSubmit_sub".equals(form.getCmd())){
                    queryWrapper.eq("order_no",form.getOrderNo());
                }
                queryWrapper.eq("status",OrderStatusEnum.COST_1.getCode());
                paymentCostService.remove(queryWrapper);
                receivableCostService.remove(queryWrapper);
            }
            //当录入的是子订单费用,且主/子订单的法人主体和结算单位不相等时,不可汇总到主订单
            Boolean isSumToMain = true;//1
           if("preSubmit_main".equals(form.getCmd())){//入主订单费用
               form.setOrderNo(null);//表中是通过有没有子订单来判断这条数据是主订单的费用还是子订单的费用
            }else if("preSubmit_sub".equals(form.getCmd())){//入子订单费用
               if(!(inputOrderVO.getLegalName().equals(form.getSubLegalName()) && inputOrderVO.getUnitCode().equals(form.getSubUnitCode()))){
                   isSumToMain = false;//0-主，子订单的法人主体和结算单位不一致则不能汇总到主订单
               }
           }
            for (OrderPaymentCost orderPaymentCost : orderPaymentCosts) {//应付费用
                orderPaymentCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderPaymentCost.setOrderNo(form.getOrderNo());
                orderPaymentCost.setIsBill("0");//未出账
                orderPaymentCost.setSubType(form.getSubType());
                if ("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
                    orderPaymentCost.setIsSumToMain(isSumToMain);
                    orderPaymentCost.setCreatedTime(LocalDateTime.now());
                    orderPaymentCost.setCreatedUser(UserOperator.getToken());
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {
                    orderPaymentCost.setOptName(UserOperator.getToken());
                    orderPaymentCost.setOptTime(LocalDateTime.now());
                    orderPaymentCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_2.getCode()));
                }
            }
            for (OrderReceivableCost orderReceivableCost : orderReceivableCosts) {//应收费用
                orderReceivableCost.setMainOrderNo(inputOrderVO.getOrderNo());
                orderReceivableCost.setOrderNo(form.getOrderNo());
                orderReceivableCost.setIsBill("0");//未出账
                orderReceivableCost.setSubType(form.getSubType());
                if ("preSubmit_main".equals(form.getCmd()) || "preSubmit_sub".equals(form.getCmd())) {
                    orderReceivableCost.setIsSumToMain(isSumToMain);
                    orderReceivableCost.setCreatedTime(LocalDateTime.now());
                    orderReceivableCost.setCreatedUser(UserOperator.getToken());
                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()));
                } else if ("submit_main".equals(form.getCmd()) || "submit_sub".equals(form.getCmd())) {
                    orderReceivableCost.setOptName(UserOperator.getToken());
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
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        //根据主订单ID获取子订单数量
        int customsNum = 0;//纯报关单数量
        if ((OrderStatusEnum.CBG.getCode().equals(form.getClassCode()) && inputMainOrderVO != null) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())
        ) {
            customsNum = customsClient.getCustomsOrderNum(inputMainOrderVO.getOrderNo()).getData();
        }
        //以后有其他的会逐渐增加 TODO
        int finalCustomsNum = customsNum;
        orderStatusVOS.forEach(x -> {
            //循环处理主流程节点,下面有子流程得contain_state字段配置类型区分，不配置状态了
            String containState = x.getContainState();
            if (containState != null && !"".equals(containState)) {
                //循环处理子节点，如纯报关,出口报关,中港等
                if (!x.getChildren().isEmpty()) {
                    for (int i = 0; i < x.getChildren().size(); i++) {
                        OrderStatusVO subOrder = x.getChildren().get(i);
                        String subContainState = subOrder.getContainState();
                        if (subContainState != null && !"".equals(subContainState)) {
                            //目前规定：子订单所包含的操作状态只有一个
                            QueryWrapper subParam = new QueryWrapper();
                            subParam.eq("main_order_id", form.getMainOrderId());
                            subParam.eq("status", subContainState);
                            queryWrapper.isNotNull("order_id");
                            subParam.orderByDesc("created_time");
                            List<LogisticsTrack> subTrack = logisticsTrackService.list(subParam);//已操作的子流程
                            //此处需区分业务场景
                            //纯报关，出口报关
                            if (OrderStatusEnum.CBG.getCode().equals(inputMainOrderVO.getClassCode()) ||
                                    OrderStatusEnum.CKBG.getCode().equals(containState)) {
                                //若子订单流程记录小于子订单数，说明主流程节点状态为进行中
                                Integer sorts = x.getChildren().get(i).getSorts();
                                if (subTrack == null || subTrack.size() == 0) {
                                    if (sorts != 1) {
                                        x.setStatus("2");
                                    } else {//如果子流程的第一个都没有已操作记录，说明主流程未进行，后面的子流程就没必要循环了
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
                            //中港运输除出口报关其他子流程节点
                            else if (OrderStatusEnum.ZGYS.getCode().equals(inputMainOrderVO.getClassCode()) && !OrderStatusEnum.CKBG.getCode().equals(containState)) {
                                if (subTrack != null && subTrack.size() > 0) {
                                    subOrder.setStatus("3");//已完成
                                    subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    x.setStatus("2");//进行中
                                    if(subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())){
                                        x.setStatus("3");//已完成
                                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //没有子节点流程的，说明只要有该主流程状态得就是已完成
                    //1.没有子节点流程得contain_state字段值为操作状态一定要配置,可为多个，如:XX1,XX2
                    //并且XX1,XX2操作状态会保存在logistics_track表
                    String[] containStates = containState.split(",");
                    QueryWrapper mainParam = new QueryWrapper();
                    mainParam.eq("main_order_id", form.getMainOrderId());
                    mainParam.in("status", containStates);
                    mainParam.isNotNull("order_id");
                    mainParam.orderByDesc("created_time");
                    List<LogisticsTrack> subTrack = logisticsTrackService.list(mainParam);//XX1,XX2流程是否已经操作
                    if (subTrack != null && subTrack.size() < containStates.length) {
                        x.setStatus("2");//进行中
                    } else if (subTrack.size() == containStates.length) {
                        x.setStatus("3");//已完成
                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
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
                            subParam.eq("main_order_id", form.getMainOrderId());
                            subParam.eq("status", subContainState);
                            subParam.orderByDesc("created_time");
                            List<LogisticsTrack> subTrack = logisticsTrackService.list(subParam);//已操作的子流程
                            if (subTrack != null && subTrack.size() > 0) {
                                subOrder.setStatus("3");//已完成
                                subOrder.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                x.setStatus("2");//进行中
                                if(subOrder.getProcessCode().equals(x.getChildren().get(x.getChildren().size() - 1).getProcessCode())){
                                    x.setStatus("3");//已完成
                                    x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                                }
                            }
                        }
                    }
                } else {//没有子节点流程的
                    String[] containStates = containState.split(",");
                    QueryWrapper mainParam = new QueryWrapper();
                    mainParam.eq("main_order_id", form.getMainOrderId());
                    mainParam.in("status", containStates);
                    mainParam.orderByDesc("created_time");
                    List<LogisticsTrack> subTrack = logisticsTrackService.list(mainParam);//已操作的子流程
                    if (subTrack != null && subTrack.size() < containStates.length) {
                        x.setStatus("2");//进行中
                    } else if (subTrack.size() == containStates.length) {
                        x.setStatus("3");//已完成
                        x.setStatusChangeTime(DateUtils.getLocalToStr(subTrack.get(0).getOperatorTime()));
                    }
                }
            }

        });
        return orderStatusVOS;
    }

    @Override
    public InputOrderVO getOrderDetail(GetOrderDetailForm form) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        InputOrderVO inputOrderVO = new InputOrderVO();
        //获取主订单信息
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        inputOrderVO.setOrderForm(inputMainOrderVO);
        //获取纯报关和出口报关信息
        if (OrderStatusEnum.CBG.getCode().equals(form.getClassCode()) ||
            inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())) {
            InputOrderCustomsVO inputOrderCustomsVO = customsClient.getCustomsDetail(inputMainOrderVO.getOrderNo()).getData();
            if(inputOrderCustomsVO != null) {
                //附件处理
                List<FileView> allPics = new ArrayList<>();
                allPics.addAll(inputOrderCustomsVO.getCntrPics());
                allPics.addAll(inputOrderCustomsVO.getEncodePics());
                allPics.addAll(inputOrderCustomsVO.getAirTransportPics());
                allPics.addAll(inputOrderCustomsVO.getSeaTransportPics());
                inputOrderCustomsVO.setAllPics(allPics);
                //循环处理接单人和接单时间
                List<InputSubOrderCustomsVO> inputSubOrderCustomsVOS = inputOrderCustomsVO.getSubOrders();
                for (InputSubOrderCustomsVO inputSubOrderCustomsVO : inputSubOrderCustomsVOS) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("order_id", inputSubOrderCustomsVO.getSubOrderId());
                    queryWrapper.eq("status", OrderStatusEnum.CUSTOMS_C_1.getCode());
                    queryWrapper.orderByDesc("created_time");
                    List<LogisticsTrack> logisticsTracks = logisticsTrackService.list(queryWrapper);
                    if (!logisticsTracks.isEmpty()) {
                        inputSubOrderCustomsVO.setJiedanTimeStr(DateUtils.getLocalToStr(logisticsTracks.get(0).getOperatorTime()));
                        inputSubOrderCustomsVO.setJiedanUser(logisticsTracks.get(0).getOperatorUser());
                    }
                }
                inputOrderVO.setOrderCustomsForm(inputOrderCustomsVO);
            }
        }
        //获取中港运输信息
        if(OrderStatusEnum.ZGYS.getCode().equals(form.getClassCode())){
            InputOrderTransportVO inputOrderTransportVO = tmsClient.getOrderTransport(inputMainOrderVO.getOrderNo()).getData();
            if(inputOrderTransportVO != null) {
                //附件信息
                List<FileView> allPics = new ArrayList<>();
                allPics.addAll(StringUtils.getFileViews(inputOrderTransportVO.getCntrPic(), inputOrderTransportVO.getCntrPicName(), prePath));
                //获取反馈操作人时上传的附件
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq(SqlConstant.ORDER_ID, inputOrderTransportVO.getId());
                List<LogisticsTrack> logisticsTracks = logisticsTrackService.list(queryWrapper);
                for (LogisticsTrack logisticsTrack : logisticsTracks) {
                    allPics.addAll(StringUtils.getFileViews(logisticsTrack.getStatusPic(), logisticsTrack.getStatusPicName(), prePath));
                }
                //提货文件
                List<FileView> takeFiles = StringUtils.getFileViews(inputOrderTransportVO.getTakeFile(), inputOrderTransportVO.getTakeFileName(), prePath);
                inputOrderTransportVO.setTakeFiles(takeFiles);
                allPics.addAll(takeFiles);
                inputOrderTransportVO.setAllPics(allPics);

                //设置提货信息的客户
                List<InputOrderTakeAdrVO> orderTakeAdrForms1 = inputOrderTransportVO.getOrderTakeAdrForms1();
                for (InputOrderTakeAdrVO inputOrderTakeAdr1 : orderTakeAdrForms1) {
                    inputOrderTakeAdr1.setCustomerName(inputMainOrderVO.getCustomerName());
                }
                List<InputOrderTakeAdrVO> orderTakeAdrForms2 = inputOrderTransportVO.getOrderTakeAdrForms2();
                for (InputOrderTakeAdrVO inputOrderTakeAdr2 : orderTakeAdrForms2) {
                    inputOrderTakeAdr2.setCustomerName(inputMainOrderVO.getCustomerName());
                }
                inputOrderVO.setOrderTransportForm(inputOrderTransportVO);
            }
        }
        //获取内陆运输和深圳中转仓信息
        if(OrderStatusEnum.NLYS.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.SZZZC.getCode())){

        }

        return inputOrderVO;
    }

    @Override
    public boolean createOrder(InputOrderForm form) {
        //保存主订单
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();
        inputMainOrderForm.setCmd(form.getCmd());
        String mainOrderNo = oprMainOrder(inputMainOrderForm);
        if(StringUtil.isNullOrEmpty(mainOrderNo)){
            return false;
        }
        String classCode = inputMainOrderForm.getClassCode();//订单类型
        String selectedServer = inputMainOrderForm.getSelectedServer();//所选服务
        //纯报关和出口报关并且订单状态为驳回(C_1_1)或为空或为暂存待补全的待接单
        if(OrderStatusEnum.CBG.getCode().equals(classCode) ||
                selectedServer.contains(OrderStatusEnum.CKBG.getCode())){
            InputOrderCustomsForm orderCustomsForm = form.getOrderCustomsForm();
            if(StringUtil.isNullOrEmpty(orderCustomsForm.getSubCustomsStatus()) ||
                    (OrderStatusEnum.CUSTOMS_C_0.getCode().equals(orderCustomsForm.getSubCustomsStatus()) &&
                     (OrderStatusEnum.MAIN_2.getCode().equals(inputMainOrderForm.getStatus()) ||
                      OrderStatusEnum.MAIN_4.getCode().equals(inputMainOrderForm.getStatus()) ||
                      inputMainOrderForm.getStatus() == null)) ||
                    OrderStatusEnum.CUSTOMS_C_1_1.getCode().equals(orderCustomsForm.getSubCustomsStatus())) {
                //如果没有生成子订单则不调用
                if (orderCustomsForm.getSubOrders() != null && orderCustomsForm.getSubOrders().size() >= 0) {
                    orderCustomsForm.setMainOrderNo(mainOrderNo);
                    if (OrderStatusEnum.CBG.getCode().equals(classCode)) {
                        orderCustomsForm.setClassCode(OrderStatusEnum.CBG.getCode());
                    } else {
                        orderCustomsForm.setClassCode(OrderStatusEnum.CKBG.getCode());
                    }
                    orderCustomsForm.setLoginUser(UserOperator.getToken());
                    Boolean result = customsClient.createOrderCustoms(orderCustomsForm).getData();
                    if (!result) {//调用失败
                        return false;
                    }
                }
            }
        }
        //中港运输并且并且订单状态为驳回或为空或为待接单
        if(OrderStatusEnum.ZGYS.getCode().equals(classCode)){
            //创建中港订单信息
            InputOrderTransportForm orderTransportForm = form.getOrderTransportForm();
            if(StringUtil.isNullOrEmpty(orderTransportForm.getSubTmsStatus()) ||
               OrderStatusEnum.TMS_T_0.getCode().equals(orderTransportForm.getSubTmsStatus()) ||
               OrderStatusEnum.TMS_T_1_1.getCode().equals(orderTransportForm.getSubTmsStatus()) ||
               OrderStatusEnum.TMS_T_2_1.getCode().equals(orderTransportForm.getSubTmsStatus()) ||
               OrderStatusEnum.TMS_T_3_2.getCode().equals(orderTransportForm.getSubTmsStatus()) ||
               OrderStatusEnum.TMS_T_4_1.getCode().equals(orderTransportForm.getSubTmsStatus())) {
                if (!selectedServer.contains(OrderStatusEnum.XGQG.getCode())) {
                    //若没有选择香港清关,则情况香港清关信息，避免信息有误
                    orderTransportForm.setHkLegalName(null);
                    orderTransportForm.setHkUnitCode(null);
                    orderTransportForm.setIsHkClear(null);
                }
                orderTransportForm.setMainOrderNo(mainOrderNo);
                orderTransportForm.setLoginUser(UserOperator.getToken());

                //根据主订单获取提货地址送货地址得客户ID
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("id_code",inputMainOrderForm.getCustomerCode());
                CustomerInfo customerInfo = customerInfoService.getOne(queryWrapper);
                List<InputOrderTakeAdrForm> takeAdrForms1 = orderTransportForm.getTakeAdrForms1();
                List<InputOrderTakeAdrForm> takeAdrForms2 = orderTransportForm.getTakeAdrForms2();
                for (InputOrderTakeAdrForm takeAdrForm1: takeAdrForms1) {
                    takeAdrForm1.setCustomerId(customerInfo.getId());
                }
                for (InputOrderTakeAdrForm takeAdrForm2: takeAdrForms2) {
                    takeAdrForm2.setCustomerId(customerInfo.getId());
                }
                Boolean result = tmsClient.createOrderTransport(orderTransportForm).getData();
                if (!result) {//调用失败
                    return false;
                }
            }
        }
        //内陆运输和深圳中转仓
        if(selectedServer.contains(OrderStatusEnum.SZZZC.getCode())){
            //创建深圳中转仓信息 TODO
        }

        return true;
    }

    @Override
    public List<InitChangeStatusVO> findSubOrderNo(GetOrderDetailForm form) {
        List<InitChangeStatusVO> changeStatusVOS = new ArrayList<>();
        //获取主订单信息
        InputMainOrderVO inputMainOrderVO = getMainOrderById(form.getMainOrderId());
        //获取纯报关或出口报关信息
        if (OrderStatusEnum.CBG.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())) {
            List<InitChangeStatusVO> cbgList = customsClient.findCustomsOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.addAll(cbgList);
        }
        //获取中港运输信息
        if(OrderStatusEnum.ZGYS.getCode().equals(form.getClassCode())){
            InitChangeStatusVO initChangeStatusVO = tmsClient.getTransportOrderNo(inputMainOrderVO.getOrderNo()).getData();
            changeStatusVOS.add(initChangeStatusVO);
        }
        //获取内陆运输或深圳中转仓数据
        if(OrderStatusEnum.NLYS.getCode().equals(form.getClassCode()) ||
                inputMainOrderVO.getSelectedServer().contains(OrderStatusEnum.SZZZC.getCode())){
            //TODO
        }
        return changeStatusVOS;
    }

    @Override
    public boolean changeStatus(ChangeStatusListForm form) {
        List<ConfirmChangeStatusForm> forms = form.getForms();
        List<CustomsChangeStatusForm> bgs = new ArrayList<>();
        List<TmsChangeStatusForm> zgys = new ArrayList<>();
        //全勾修改主订单状态
        if(form.getCheckAll()){
            //循环处理,判断主订单是否需要录入费用
            Boolean needInputCost = false;
            //如果主订单下的所有子订单都不用录入费用那主订单也不需要录入费用了
            for (ConfirmChangeStatusForm temp : forms ) {
                if(temp.getNeedInputCost()){
                    needInputCost = true;
                    break;
                }
            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_3.getCode()));
            orderInfo.setId(form.getMainOrderId());
            orderInfo.setUpTime(LocalDateTime.now());
            orderInfo.setUpUser(UserOperator.getToken());
            orderInfo.setNeedInputCost(needInputCost);
            baseMapper.updateById(orderInfo);
        }
        for (ConfirmChangeStatusForm confirmChangeStatusForm : forms) {
            if(CommonConstant.BG.equals(confirmChangeStatusForm.getOrderType())){
                CustomsChangeStatusForm bg = new CustomsChangeStatusForm();
                bg.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                bg.setOrderNo(confirmChangeStatusForm.getOrderNo());
                bg.setStatus(form.getStatus());
                bg.setLoginUser(UserOperator.getToken());
                bgs.add(bg);
            }else if(CommonConstant.ZGYS.equals(confirmChangeStatusForm.getOrderType())){
                TmsChangeStatusForm tm = new TmsChangeStatusForm();
                tm.setNeedInputCost(confirmChangeStatusForm.getNeedInputCost());
                tm.setOrderNo(confirmChangeStatusForm.getOrderNo());
                tm.setStatus(form.getStatus());
                tm.setLoginUser(UserOperator.getToken());
                zgys.add(tm);
            }

        }
        //处理报关订单
        if (bgs.size() > 0) {
            customsClient.changeCustomsStatus(bgs).getData();
        }
        if(zgys.size() > 0){
            tmsClient.changeTransportStatus(zgys).getData();
        }
        return true;
    }

    @Override
    public OrderDataCountVO countOrderData() {
        return baseMapper.countOrderData();
    }

    @Override
    public InitGoCustomsAuditVO initGoCustomsAudit(InitGoCustomsAuditForm form) {
        InitGoCustomsAuditVO initGoCustomsAuditVO = new InitGoCustomsAuditVO();
        String prePath = fileClient.getBaseUrl().getData().toString();
        if(form.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode())){//出口报关
            initGoCustomsAuditVO = baseMapper.initGoCustomsAudit1(form);
        }else {//外部报关放行
            initGoCustomsAuditVO = baseMapper.initGoCustomsAudit2(form);
        }
        initGoCustomsAuditVO.setFileViewList(StringUtils.getFileViews(initGoCustomsAuditVO.getFileStr(),initGoCustomsAuditVO.getFileNameStr(),prePath));
        return initGoCustomsAuditVO;
    }


}
