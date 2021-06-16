package com.jayud.oms.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ReceivableAndPayableOrderTypeEnum;
import com.jayud.common.enums.UnitEnum;
import com.jayud.oms.feign.CustomsClient;
import com.jayud.oms.feign.FinanceClient;
import com.jayud.oms.model.bo.AuditCostForm;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.InputSubOrderCustomsVO;
import com.jayud.oms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomsFinanceServiceImpl implements CustomsFinanceService {
    @Autowired
    CustomsClient customsClient;

    @Autowired
    FinanceClient financeClient;

    @Autowired
    IOrderReceivableCostService receivableCostService;

    @Autowired
    ICostInfoService costInfoService;

    @Autowired
    ICostTypeService costTypeService;

    @Autowired
    ICostGenreService costGenreService;

    @Autowired
    IOrderInfoService orderInfoService;

    @Autowired
    IProductBizService productBizService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveReceivable(List<CustomsReceivable> customsReceivable) {
        Optional<CustomsReceivable> first = customsReceivable.stream().filter(Objects::nonNull).findFirst();
        if (first.isPresent()) {
            InputSubOrderCustomsVO subOrderCustoms = customsClient.getOrderNoByYunCustomsNo(first.get().getCustomApplyNo()).getData();

            if (Objects.isNull(subOrderCustoms)) {
                return true;
            }
            // 获取子订单的应收费用
            QueryWrapper<OrderReceivableCost> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(OrderReceivableCost::getOrderNo, subOrderCustoms.getOrderNo());
            Map<String, OrderReceivableCost> receivableCostMap = receivableCostService.list(queryWrapper)
                    .stream().collect(Collectors.toMap(OrderReceivableCost::getCostCode, e -> e));

            // 获取云报关-金蝶财务费用项对应关系
            Map<String, CustomsFinanceFeeRelation> feeRelationMap = financeClient.getCustomsFinanceFeeRelation().getData()
                    .values()
                    .stream().collect(Collectors.toMap(CustomsFinanceFeeRelation::getYunbaoguanCode, e -> e));

            // 获取费用code
            Map<String, CostInfo> costInfoMap = costInfoService.list().stream().collect(Collectors.toMap(CostInfo::getName, e -> e));

            // 费用类别、费用类型
            CostType costType = costTypeService.getOne(Wrappers.<CostType>lambdaQuery().eq(CostType::getCodeName, "境内报关"));
            OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getOrderNo, subOrderCustoms.getMainOrderNo()));
            ProductBiz productBiz = productBizService.getOne(Wrappers.<ProductBiz>lambdaQuery().eq(ProductBiz::getIdCode, orderInfo.getBizCode()));
            if (Objects.isNull(productBiz)) {
                return false;
            }

            //写入费用项
            log.info("OMS开始处理应收费用明细...");
            for (CustomsReceivable item : customsReceivable) {
                List<Long> deleteReceivableCostList = new ArrayList<>();
                List<OrderReceivableCost> saveOrUpdateReceivableCostList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : item.getOriginData().getInnerMap().entrySet()) {

                    //直接将entry跟费用项表比对,如果费用关系表没有此code，且该项金额为零，跳过此项可继续
                    if (!feeRelationMap.containsKey(entry.getKey())) {
                        continue;
                    }

                    CustomsFinanceFeeRelation customsFinanceFeeRelation = getRelationMapItem(entry.getKey(), feeRelationMap, CustomsFinanceFeeRelation.class);
                    if (Objects.isNull(customsFinanceFeeRelation)) {
                        log.error("云报关费用项{}无法匹配金蝶费用项;", entry.getKey());
                        continue;
                    }

                    // 金蝶费用名称跟OMS费用名称匹配
                    if (!costInfoMap.containsKey(customsFinanceFeeRelation.getKingdeeName())) {
                        continue;
                    }

                    // 费用code
                    CostInfo costInfo = costInfoMap.get(customsFinanceFeeRelation.getKingdeeName());
                    String costCode = costInfo.getIdCode();
                    BigDecimal price = new BigDecimal(entry.getValue().toString());
                    // 费用已存在处理->更新,删除
                    if (receivableCostMap.containsKey(costCode)) {
                        if (Objects.equals("0.00", entry.getValue().toString())) {
                            deleteReceivableCostList.add(receivableCostMap.get(costCode).getId());
                            continue;
                        }
                        OrderReceivableCost orderReceivableCost = receivableCostMap.get(costCode);
                        orderReceivableCost.setUnitPrice(price);
                        orderReceivableCost.setAmount(price);
                        orderReceivableCost.setChangeAmount(price);
                        orderReceivableCost.setOptTime(LocalDateTime.now());
                        saveOrUpdateReceivableCostList.add(orderReceivableCost);
                        continue;
                    }

                    // 费用不存在处理->保存
                    if (Objects.equals("0.00", entry.getValue().toString())) {
                        continue;
                    }
                    OrderReceivableCost orderReceivableCost = new OrderReceivableCost();
                    orderReceivableCost.setMainOrderNo(subOrderCustoms.getMainOrderNo());
                    orderReceivableCost.setOrderNo(subOrderCustoms.getOrderNo());
                    orderReceivableCost.setCustomerCode(subOrderCustoms.getUnitCode());

                    String[] costTypeIds = costInfo.getCids().split(",");
                    if (Objects.nonNull(costType) && Arrays.stream(costTypeIds).anyMatch(e -> e.equals(costType.getId().toString()))) {
                        orderReceivableCost.setCostTypeId(costType.getId());
                    } else {
                        orderReceivableCost.setCostTypeId(Long.valueOf(costTypeIds[0]));
                    }
                    orderReceivableCost.setCostGenreId(productBiz.getCostGenreDefault());
                    orderReceivableCost.setUnit(UnitEnum.PCS.getCode());
                    orderReceivableCost.setCostCode(costCode);

                    orderReceivableCost.setUnitPrice(price);
                    orderReceivableCost.setNumber(BigDecimal.ONE);
                    orderReceivableCost.setCurrencyCode("CNY");
                    orderReceivableCost.setAmount(price);
                    orderReceivableCost.setExchangeRate(BigDecimal.ONE);
                    orderReceivableCost.setChangeAmount(price);
                    orderReceivableCost.setIsBill("0");
                    orderReceivableCost.setSubType(ReceivableAndPayableOrderTypeEnum.CBG.getCode());
                    if (orderInfo.getLegalEntityId().equals(subOrderCustoms.getLegalEntityId()) && orderInfo.getUnitCode().equals(subOrderCustoms.getUnitCode())) {
                        orderReceivableCost.setIsSumToMain(Boolean.TRUE);
                        orderReceivableCost.setLegalName(orderInfo.getLegalName());
                        orderReceivableCost.setLegalId(orderInfo.getLegalEntityId().intValue());
                    } else {
                        orderReceivableCost.setIsSumToMain(Boolean.FALSE);
                        orderReceivableCost.setLegalName(subOrderCustoms.getLegalName());
                        orderReceivableCost.setLegalId(subOrderCustoms.getLegalEntityId().intValue());
                    }

                    orderReceivableCost.setStatus(Integer.valueOf(OrderStatusEnum.COST_3.getCode()));
                    orderReceivableCost.setOptName("system");
                    orderReceivableCost.setOptTime(LocalDateTime.now());
                    orderReceivableCost.setCreatedUser("system");
                    orderReceivableCost.setCreatedTime(LocalDateTime.now());
                    saveOrUpdateReceivableCostList.add(orderReceivableCost);
                }
                receivableCostService.removeByIds(deleteReceivableCostList);
                receivableCostService.saveOrUpdateBatch(saveOrUpdateReceivableCostList);
                orderInfoService.auditCost(getAuditCostForm(subOrderCustoms, saveOrUpdateReceivableCostList));
            }
        } else {
            log.error("应收单异常:第一条数据==>{}", first.toString());
            return false;
        }
        log.info("OMS处理应收费用完成...");
        return true;
    }

    // 组装
    private AuditCostForm getAuditCostForm(InputSubOrderCustomsVO subOrderCustoms, List<OrderReceivableCost> saveOrUpdateReceivableCostList) {
        AuditCostForm auditCostForm = new AuditCostForm();
        auditCostForm.setPaymentCosts(new ArrayList<>());
        auditCostForm.setReceivableCosts(saveOrUpdateReceivableCostList);
        auditCostForm.setSubUnitCode(subOrderCustoms.getUnitCode());
        auditCostForm.setSubLegalName(subOrderCustoms.getLegalName());
        auditCostForm.setSubOrderNo(subOrderCustoms.getOrderNo());
        auditCostForm.setStatus(OrderStatusEnum.COST_3.getCode());
        return auditCostForm;
    }

    private <T> T getRelationMapItem(String key, Map<String, T> sourceMap, Class<T> returnType) {
        String jsonStr = JSONUtil.toJsonStr(sourceMap.get(key));
        return JSONObject.parseObject(jsonStr, returnType);
    }

    public static void main(String[] args) {
        BigDecimal decimal = new BigDecimal("0.00");
        System.out.println("decimal = " + decimal);
    }
}
