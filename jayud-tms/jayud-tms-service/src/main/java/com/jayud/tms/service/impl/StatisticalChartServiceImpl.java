package com.jayud.tms.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.mapper.OrderTransportMapper;
import com.jayud.tms.model.bo.BasePageForm;
import com.jayud.tms.model.vo.DriverOrderTakeAdrVO;
import com.jayud.tms.model.vo.statistical.BusinessPeople;
import com.jayud.tms.model.vo.statistical.ProcessNode;
import com.jayud.tms.model.vo.statistical.TVOrderTransportVO;
import com.jayud.tms.service.IOrderTakeAdrService;
import com.jayud.tms.service.StatisticalChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticalChartServiceImpl implements StatisticalChartService {

    @Autowired
    private OrderTransportMapper orderTransportMapper;
    @Autowired
    private IOrderTakeAdrService orderTakeAdrService;
    @Autowired
    private OmsClient omsClient;

    @Override
    public IPage<TVOrderTransportVO> findTVShowOrderByPage(BasePageForm form, List<String> legalNames) {
        //定义分页参数
        Page<TVOrderTransportVO> page = new Page(form.getPageNum(), form.getPageSize());

        IPage<TVOrderTransportVO> pageInfo = orderTransportMapper.findTVShowOrderByPage(page, legalNames);
        if (pageInfo.getRecords().size() == 0) {
            return pageInfo;
        }
        List<TVOrderTransportVO> records = pageInfo.getRecords();

        Map<Long, String> subOrderMap = new HashMap<>();
        Set<String> mainOrderNos = new HashSet<>();
        Set<Long> warehouseIds = new HashSet<>();
        Set<String> subOrderNos = new HashSet<>();
        Set<Long> driverIds = new HashSet<>();
        Set<Long> vehicleIds = new HashSet<>();

        for (TVOrderTransportVO record : records) {
            subOrderMap.put(record.getId(), record.getStatus());
            mainOrderNos.add(record.getMainOrderNo());
            warehouseIds.add(record.getWarehouseInfoId());
            subOrderNos.add(record.getOrderNo());
            driverIds.add(record.getDriverInfoId());
            vehicleIds.add(record.getVehicleId());
        }

        //查询主订单信息
        ApiResult mainOrderResult = omsClient.getMainOrderByOrderNos(new ArrayList<>(mainOrderNos));
        //查询中转仓库地址
        ApiResult warehouseResult = this.omsClient.getWarehouseInfoByIds(new ArrayList<>(warehouseIds));
        //查询司机信息
        ApiResult driverResult = this.omsClient.getDriverInfoByIds(new ArrayList<>(driverIds));
        //查询车辆信息
        ApiResult vehicleInfoResult = this.omsClient.getVehicleInfoByIds(new ArrayList<>(vehicleIds));
        //流程处理
        Map<Long, List<ProcessNode>> nodeProcessing = this.processNodeProcessing(subOrderMap);


        //查询提货商品信息
//        List<OrderTakeAdr> orderTakeAdrs = this.orderTakeAdrService.getOrderTakeAdrByOrderNos(subOrderNos, OrderTakeAdrTypeEnum.ONE.getCode());
        //是否录用费用


        List<TVOrderTransportVO> pageList = pageInfo.getRecords();
        List<DriverOrderTakeAdrVO> takeAdrsList = this.orderTakeAdrService.getDriverOrderTakeAdr(new ArrayList<>(subOrderNos), null);
        for (TVOrderTransportVO orderTransportVO : pageList) {
            //拼装主订单信息
            orderTransportVO.assemblyMainOrderData(mainOrderResult.getData());
            //拼接中转仓库信息
            orderTransportVO.assemblyWarehouseInfo(warehouseResult.getData());
            //拼装地址信息
            orderTransportVO.assemblyTakeAddr(takeAdrsList);
            //拼接司机信息
            orderTransportVO.assemblyDriverInfo(driverResult.getData());
            //拼装车辆信息
            orderTransportVO.assemblyVehicleInfo(vehicleInfoResult.getData());
            //拼装节点
            orderTransportVO.assemblyProcessNode(nodeProcessing.get(orderTransportVO.getId()));

        }

        return pageInfo;
    }

    @Override
    public Map<Long, List<ProcessNode>> processNodeProcessing(Map<Long, String> subOrderMap) {

        ApiResult result = omsClient.getLogisticsTrackByType(new ArrayList<>(subOrderMap.keySet()), BusinessTypeEnum.ZGYS.getCode());
        Map<Long, List<ProcessNode>> map = new HashMap<>();
        if (result.getData() == null) {
            return map;
        }
        JSONArray array = new JSONArray(result.getData());
        Map<Long, List<Object>> group = array.stream().collect(Collectors.groupingBy(e -> ((JSONObject) e).getLong("orderId")));

        Map<Long, List<ProcessNode>> tmp = new LinkedHashMap<>();
        group.forEach((k, v) -> {
            //子订单状态
            String subStatus = subOrderMap.get(k);
            LinkedHashMap<String, ProcessNode> allNodes = getAllNodes();


            for (Object o : v) {
                JSONObject node = (JSONObject) o;

                String status = node.getStr("status");
                ProcessNode processNode = allNodes.get(status);

                if (processNode != null) {
                    processNode.setCompleteTime(DateUtils.format(node.getDate("operatorTime"), "HH:mm"))
                            .setStatus(2).setUserName(node.getStr("operatorUser"));
                    //特殊处理通关异常 通关前复核完成后,处理下个节点需要特效处理
                    if ("复核".equals(processNode.getName())) {
                        if (OrderStatusEnum.TMS_T_9_1.getCode().equals(subStatus)
                                || OrderStatusEnum.TMS_T_9_2.getCode().equals(subStatus)) {
                            allNodes.put(OrderStatusEnum.TMS_T_9.getCode(), new ProcessNode().setName("通关").setStatus(1));
                        }
                    }
                }
            }
            tmp.put(k, new ArrayList<>(allNodes.values()));
        });

        //补充节点
        subOrderMap.forEach((k, v) -> {
            List<ProcessNode> processNodes = tmp.get(k);
            if (processNodes == null) {
                tmp.put(k, new ArrayList<>(getAllNodes().values()));
            }
        });

        return tmp;
    }


    @Override
    public IPage<BusinessPeople> getRankingBusinessPeople(BasePageForm form, List<String> legalNames) {
        //定义分页参数
        Page<BusinessPeople> page = new Page(form.getPageNum(), form.getPageSize());
        //统计业务员排名
        IPage<BusinessPeople> iPage = this.orderTransportMapper.findStatisticsSalesmanRanking(page, legalNames);
        List<BusinessPeople> records = iPage.getRecords();
        Integer totalSum = records.stream().mapToInt(BusinessPeople::getOrderNum).sum();
        records.forEach(e -> e.calculatePercentage(new BigDecimal(totalSum)));
        return iPage;
    }


    /**
     * 获取节点
     */
    private LinkedHashMap<String, ProcessNode> getAllNodes() {
        LinkedHashMap<String, ProcessNode> nodes = new LinkedHashMap<>();
//        nodes.put("接单", OrderStatusEnum.TMS_T_1.getCode());
//        nodes.put("派车", OrderStatusEnum.TMS_T_2.getCode());
//        nodes.put("审核", OrderStatusEnum.TMS_T_3.getCode());
//        nodes.put("确认派车", OrderStatusEnum.TMS_T_4.getCode());
//        nodes.put("提货", OrderStatusEnum.TMS_T_5.getCode());
//        nodes.put("过磅", OrderStatusEnum.TMS_T_6.getCode());
//        nodes.put("复核", OrderStatusEnum.TMS_T_8.getCode());
//        nodes.put("通关", OrderStatusEnum.TMS_T_9.getCode());
//        nodes.put("清关", OrderStatusEnum.HK_CLEAR_1.getCode());
//        nodes.put("入仓", OrderStatusEnum.TMS_T_10.getCode());
//        nodes.put("出仓", OrderStatusEnum.TMS_T_13.getCode());
//        nodes.put("派送", OrderStatusEnum.TMS_T_14.getCode());
//        nodes.put("签收", OrderStatusEnum.TMS_T_15.getCode());
        nodes.put(OrderStatusEnum.TMS_T_1.getCode(), new ProcessNode().setName("接单"));
        nodes.put(OrderStatusEnum.TMS_T_2.getCode(), new ProcessNode().setName("派车"));
        nodes.put(OrderStatusEnum.TMS_T_3.getCode(), new ProcessNode().setName("审核"));
        nodes.put(OrderStatusEnum.TMS_T_4.getCode(), new ProcessNode().setName("派车"));
        nodes.put(OrderStatusEnum.TMS_T_5.getCode(), new ProcessNode().setName("提货"));
        nodes.put(OrderStatusEnum.TMS_T_6.getCode(), new ProcessNode().setName("过磅"));
        nodes.put(OrderStatusEnum.TMS_T_8.getCode(), new ProcessNode().setName("复核"));
        nodes.put(OrderStatusEnum.TMS_T_9.getCode(), new ProcessNode().setName("通关"));
        nodes.put(OrderStatusEnum.HK_CLEAR_1.getCode(), new ProcessNode().setName("清关"));
        nodes.put(OrderStatusEnum.TMS_T_10.getCode(), new ProcessNode().setName("入仓"));
        nodes.put(OrderStatusEnum.TMS_T_13.getCode(), new ProcessNode().setName("出仓"));
        nodes.put(OrderStatusEnum.TMS_T_14.getCode(), new ProcessNode().setName("派送"));
        nodes.put(OrderStatusEnum.TMS_T_15.getCode(), new ProcessNode().setName("签收"));

        return nodes;
    }
}
