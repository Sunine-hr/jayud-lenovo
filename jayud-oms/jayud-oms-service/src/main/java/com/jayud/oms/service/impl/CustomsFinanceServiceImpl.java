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
import com.jayud.oms.feign.OauthClient;
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
    OauthClient oauthClient;
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
    @Autowired
    private ICustomerInfoService customerInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveReceivable(List<CustomsReceivable> customsReceivable) {
        Optional<CustomsReceivable> first = customsReceivable.stream().filter(Objects::nonNull).findFirst();
        if (first.isPresent()) {
            InputSubOrderCustomsVO subOrderCustoms = customsClient.getOrderNoByYunCustomsNo(first.get().getCustomApplyNo()).getData();

            if (Objects.isNull(subOrderCustoms)) {
                return true;
            }
            // ??????????????????????????????
            QueryWrapper<OrderReceivableCost> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(OrderReceivableCost::getOrderNo, subOrderCustoms.getOrderNo());
            Map<String, OrderReceivableCost> receivableCostMap = new HashMap<>();
            try {
                receivableCostMap = receivableCostService.list(queryWrapper)
                        .stream().collect(Collectors.toMap(OrderReceivableCost::getCostCode, e -> e));
            } catch (Exception e) {
                log.warn("??????code?????? ????????????:{}", e.getMessage());
                return false;
            }


            // ???????????????-?????????????????????????????????
            Map<String, CustomsFinanceFeeRelation> feeRelationMap = financeClient.getCustomsFinanceFeeRelation().getData()
                    .values()
                    .stream().collect(Collectors.toMap(CustomsFinanceFeeRelation::getYunbaoguanCode, e -> e));

            // ????????????code
            Map<String, CostInfo> costInfoMap = costInfoService.list().stream().collect(Collectors.toMap(CostInfo::getName, e -> e));

            // ???????????????????????????
            CostType costType = costTypeService.getOne(Wrappers.<CostType>lambdaQuery().eq(CostType::getCodeName, "????????????"));
            OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getOrderNo, subOrderCustoms.getMainOrderNo()));
            ProductBiz productBiz = productBizService.getOne(Wrappers.<ProductBiz>lambdaQuery().eq(ProductBiz::getIdCode, orderInfo.getBizCode()));
            if (Objects.isNull(productBiz)) {
                return false;
            }

            //??????
            Map<String, String> customerNameMap = this.customerInfoService.getCustomerName();

            //???????????????
            log.info("OMS??????????????????????????????...");
            for (CustomsReceivable item : customsReceivable) {
                List<Long> deleteReceivableCostList = new ArrayList<>();
                List<OrderReceivableCost> saveOrUpdateReceivableCostList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : item.getOriginData().getInnerMap().entrySet()) {

                    //?????????entry?????????????????????,??????????????????????????????code????????????????????????????????????????????????
                    if (!feeRelationMap.containsKey(entry.getKey())) {
                        continue;
                    }

                    CustomsFinanceFeeRelation customsFinanceFeeRelation = getRelationMapItem(entry.getKey(), feeRelationMap, CustomsFinanceFeeRelation.class);
                    if (Objects.isNull(customsFinanceFeeRelation)) {
                        log.error("??????????????????{}???????????????????????????;", entry.getKey());
                        continue;
                    }

                    // ?????????????????????OMS??????????????????
                    if (!costInfoMap.containsKey(customsFinanceFeeRelation.getKingdeeName())) {
                        continue;
                    }

                    // ??????code
                    CostInfo costInfo = costInfoMap.get(customsFinanceFeeRelation.getKingdeeName());
                    String costCode = costInfo.getIdCode();
                    BigDecimal price = new BigDecimal(entry.getValue().toString());
                    // ?????????????????????->??????,??????
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

                    // ?????????????????????->??????
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
                        if (orderInfo.getLegalEntityId()==null){
                            orderReceivableCost.setLegalId(Long.parseLong((oauthClient.getLegalEntityByLegalName(orderInfo.getLegalName()).getData().toString())));
                        }else {
                            orderReceivableCost.setLegalId(orderInfo.getLegalEntityId());
                        }
                        orderReceivableCost.setUnitCode(orderInfo.getUnitCode()).setUnitName(customerNameMap.get(orderInfo.getUnitCode()));
                        orderReceivableCost.setDepartmentId(Long.valueOf(orderInfo.getBizBelongDepart()));
                    } else {
                        orderReceivableCost.setIsSumToMain(Boolean.FALSE);
                        orderReceivableCost.setLegalName(subOrderCustoms.getLegalName());
                        if (subOrderCustoms.getLegalEntityId()==null){
                            orderReceivableCost.setLegalId(Long.parseLong((oauthClient.getLegalEntityByLegalName(subOrderCustoms.getLegalName()).getData().toString())));
                        }else {
                            orderReceivableCost.setLegalId(subOrderCustoms.getLegalEntityId());
                        }
                        orderReceivableCost.setUnitCode(subOrderCustoms.getUnitCode()).setUnitName(customerNameMap.get(subOrderCustoms.getUnitCode()));
                        orderReceivableCost.setDepartmentId(subOrderCustoms.getDepartmentId());
                    }

//                    if (orderReceivableCost.getIsSumToMain()) {
//                        orderReceivableCost.setLegalName(orderInfo.getLegalName());
//                    } else {
//                        orderReceivableCost.setLegalName(subOrderCustoms.getLegalName());
//                    }

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
            log.error("???????????????:???????????????==>{}", first.toString());
            return false;
        }
        log.info("OMS????????????????????????...");
        return true;
    }

    // ??????
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
