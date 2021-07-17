package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.HttpUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.finance.bo.QueryProfitStatementForm;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.ProfitStatementMapper;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.service.*;
import com.jayud.finance.vo.ProfitStatementBasicData;
import com.jayud.finance.vo.ProfitStatementBillDetailsVO;
import com.jayud.finance.vo.ProfitStatementBillVO;
import com.jayud.finance.vo.ProfitStatementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 利润报表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@Service
public class ProfitStatementServiceImpl extends ServiceImpl<ProfitStatementMapper, ProfitStatement> implements IProfitStatementService {
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CommonService commonService;
    @Autowired
    private IOrderReceivableBillDetailService receivableBillDetailService;
    @Autowired
    private IOrderPaymentBillDetailService paymentBillDetailService;
    @Value("${proStatement.depart}")
    private String depart;
    @Value("${proStatement.oprDepart}")
    private String oprDepart;



    /**
     * 同步数据
     *
     * @param list
     */
    @Override
    @Transactional
    public void synchronizeData(List<ProfitStatement> list) {
        LocalDateTime now = LocalDateTime.now();
        //同步时间
        list.forEach(e -> e.setSyncTime(now));
        //获取现有数据
        List<ProfitStatement> cacheDatas = redisUtils.getList("profit_statement", ProfitStatement.class);
        if (cacheDatas == null) {
            cacheDatas = this.list();
        }
        //比较数据
        Map<String, ProfitStatement> cacheDataMap = new HashMap<>();
        if (cacheDatas.size() > 0) {
            cacheDataMap = cacheDatas.stream().collect(Collectors.toMap(e -> e.getMainOrderNo() + "+" + e.getOrderNo() + "+" + e.getIsMain(), e -> e));
        } else {
            this.saveBatch(list);
            //存入redis
            this.redisUtils.set("profit_statement", com.alibaba.fastjson.JSONArray.toJSONString(list));
            return;
        }

        List<ProfitStatement> datas = new ArrayList<>();
        Map<String, ProfitStatement> finalCacheDataMap = cacheDataMap;
        list.forEach(e -> {
            ProfitStatement remove = finalCacheDataMap.remove(e.getMainOrderNo() + "+" + e.getOrderNo() + "+" + e.getIsMain());
            if (remove == null) {
                datas.add(e);
            } else {
                datas.add(e.setId(remove.getId()));
            }
        });
        //清除多余数据
        List<Long> ids = new ArrayList<>();
        cacheDataMap.forEach((k, v) -> {
            ids.add(v.getId());
        });
        this.removeByIds(ids);
        this.saveOrUpdateBatch(datas);
        this.redisUtils.set("profit_statement", com.alibaba.fastjson.JSONArray.toJSONString(datas));
    }

    @Override
    public List<ProfitStatement> statisticalProfitReport() {
        List<ProfitStatementBasicData> basicDatas = this.baseMapper.getBasicDataOfProfitStatement();

        Map<String, List<ProfitStatementBasicData>> basicDateMap = basicDatas.stream().collect(Collectors.groupingBy(ProfitStatementBasicData::getMainOrderNo));
        //部门
        Map<Long, String> departmentMap = oauthClient.findDepartment().getData().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        //币种
        Map<String, String> currencyMap = this.omsClient.initCurrencyInfo().getData().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
        //结算单位
//        Map<String, String> customerMap = this.omsClient.getCustomerName().getData();
        //业务类型
        Map<String, String> bizService = this.omsClient.initBizService().getData().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));

        List<ProfitStatement> list = new ArrayList<>();
        basicDateMap.forEach((k, v) -> {
            Map<String, ProfitStatement> map = new HashMap<>();
//            Map<String, Map<String, BigDecimal>> internalCost = new HashMap<>();
//            Map<String, Map<String, BigDecimal>> reCost = new HashMap<>();
//            Map<String, Map<String, BigDecimal>> payCost = new HashMap<>();
//            Map<String, Map<String, BigDecimal>> reInCost = new HashMap<>();// 包含内部往来
//            Map<String, Map<String, BigDecimal>> payInCost = new HashMap<>();// 包含内部往来

            Map<String, Map<String, Map<String, BigDecimal>>> reCost = new HashMap<>();
            Map<String, Map<String, Map<String, BigDecimal>>> payCost = new HashMap<>();

            String tmp = null;
            Long bizBelongDepartId = null;
            Long mainLegalId = null;
            String mainLegalName = null;
            for (ProfitStatementBasicData data : v) {
                bizBelongDepartId = data.getBizBelongDepart();
                mainLegalName = data.getMainLegalName();
                mainLegalId = data.getMainLegalId();
                data.setCurrencyName(currencyMap.get(data.getCurrencyCode()));
                data.setDepartment(departmentMap.get(data.getDepartmentId()));
                data.setBizType(bizService.get(data.getBizCode()));

                //主订单统计维度
                if (data.getIsSumToMain()) {
                    tmp = data.getMainOrderNo();
                    this.doStatisticalProfitReport(data.getMainOrderNo(), data, map, reCost, payCost);
                } else {
                    data.setIsInternal(data.getMainLegalName().equals(data.getUnitName()));
                    //子订单统计维度
                    this.doStatisticalProfitReport(data.getOrderNo(), data, map, reCost, payCost);
                }
            }

            String mainOrderNo = tmp;
            Long finalMainLegalId = mainLegalId;
            String finalMainLegalName = mainLegalName;
            Long finalBizBelongDepartId = bizBelongDepartId;
            Map<String, ProfitStatement> tmpProfitStatement = new HashMap<>(map);
            map.forEach((k1, v1) -> {
                ProfitStatement value = tmpProfitStatement.get(k1);
                Map<String, Map<String, BigDecimal>> reCostMap = reCost.get(k1);
                Map<String, Map<String, BigDecimal>> payCostMap = payCost.get(k1);
//                if (reCostMap != null && payCostMap != null) { //追加是没有这个
//                    v1.setReAmount(this.assemblyCost(reCostMap.get("cost")))
//                            .setReInAmount(this.assemblyCost(reCost.get(k1).get("inCost")))
//                            .setPayAmount(this.assemblyCost(payCost.get(k1).get("cost")))
//                            .setPayInAmount(this.assemblyCost(payCost.get(k1).get("inCost")));
//                }

                if (reCostMap != null && payCostMap != null) { //
                    value.setReAmount(this.assemblyCost(reCostMap.get("cost")))
                            .setReInAmount(this.assemblyCost(reCostMap.get("inCost")))
                            .setPayAmount(this.assemblyCost(payCostMap.get("cost")))
                            .setPayInAmount(this.assemblyCost(payCostMap.get("inCost")));
                }

                //主订单(针对主订单):内部往来取子订单应付费用
                this.internalTransactionProcessing(mainOrderNo, k1, value,
                        map, tmpProfitStatement, departmentMap,
                        finalMainLegalId, finalMainLegalName, finalBizBelongDepartId);


            });
            list.addAll(new ArrayList<>(tmpProfitStatement.values()));
        });

        return list;
    }

    /**
     * 查询利润报表数据
     *
     * @param form
     * @return
     */
    @Override
    public List<ProfitStatementVO> list(QueryProfitStatementForm form, Map<String, Object> rollCallback) {
        List<ProfitStatementVO> list = this.baseMapper.list(form);
        List<String> reAmounts = new ArrayList<>();
        List<String> payAmounts = new ArrayList<>();
        BigDecimal reEquivalentAmount = new BigDecimal(0);
        BigDecimal payEquivalentAmount = new BigDecimal(0);
        List<String> reInAmounts = new ArrayList<>();
        List<String> payInAmounts = new ArrayList<>();
        BigDecimal reInEquivalentAmount = new BigDecimal(0);
        BigDecimal payInEquivalentAmount = new BigDecimal(0);

        for (ProfitStatementVO profitStatementVO : list) {
            profitStatementVO.totalInternalExpenses(form.getIsOpenInternal());
            profitStatementVO.setReAmount(this.commonService.calculatingCosts(Arrays.asList(profitStatementVO.getReAmount())));
            profitStatementVO.setReInAmount(this.commonService.calculatingCosts(Arrays.asList(profitStatementVO.getReInAmount())));
            profitStatementVO.setPayAmount(this.commonService.calculatingCosts(Arrays.asList(profitStatementVO.getPayAmount())));
            profitStatementVO.setPayInAmount(this.commonService.calculatingCosts(Arrays.asList(profitStatementVO.getPayInAmount())));
            reAmounts.add(profitStatementVO.getReAmount());
            payAmounts.add(profitStatementVO.getPayAmount());
            reEquivalentAmount = BigDecimalUtil.add(reEquivalentAmount, profitStatementVO.getReEquivalentAmount());
            payEquivalentAmount = BigDecimalUtil.add(payEquivalentAmount, profitStatementVO.getPayEquivalentAmount());

            reInAmounts.add(profitStatementVO.getReInAmount());
            payInAmounts.add(profitStatementVO.getPayInAmount());
            reInEquivalentAmount = BigDecimalUtil.add(reInEquivalentAmount, profitStatementVO.getReInEquivalentAmount());
            payInEquivalentAmount = BigDecimalUtil.add(payInEquivalentAmount, profitStatementVO.getPayInEquivalentAmount());
        }
        rollCallback.put("totalReAmount", this.commonService.calculatingCosts(reAmounts));
        rollCallback.put("totalPayAmounts", this.commonService.calculatingCosts(payAmounts));
        rollCallback.put("totalReEqAmount", reEquivalentAmount);
        rollCallback.put("totalPayEqAmount", payEquivalentAmount);
        rollCallback.put("totalProfit", reEquivalentAmount.subtract(payEquivalentAmount));
        rollCallback.put("totalReInAmount", this.commonService.calculatingCosts(reInAmounts));
        rollCallback.put("totalPayInAmounts", this.commonService.calculatingCosts(payInAmounts));
        rollCallback.put("totalReInEqAmount", reInEquivalentAmount);
        rollCallback.put("totalPayInEqAmount", payInEquivalentAmount);
        rollCallback.put("totalInProfit", reInEquivalentAmount.subtract(payInEquivalentAmount));
        return list;
    }

    private String assemblyCost(Map<String, BigDecimal> costMap) {
        if (costMap == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        costMap.forEach((k, v) -> {
            sb.append(v).append(" ").append(k).append(",");
        });
        return sb.toString();
    }

    private void doStatisticalProfitReport(String orderNo, ProfitStatementBasicData data,
                                           Map<String, ProfitStatement> map,
                                           Map<String, Map<String, Map<String, BigDecimal>>> reCost,
                                           Map<String, Map<String, Map<String, BigDecimal>>> payCost) {
        ProfitStatement profitStatement = map.get(orderNo);
        Map<String, Map<String, BigDecimal>> reCostMap = reCost.get(orderNo);
        Map<String, Map<String, BigDecimal>> payCostMap = payCost.get(orderNo);
        if (profitStatement == null) {
            reCostMap = new HashMap<>();
            payCostMap = new HashMap<>();
            profitStatement = data.convert(ProfitStatement.class);
        }
        profitStatement.setDepartment(data.getDepartment());
        profitStatement.setCustomerName(data.getMainOrderCustomerName());
        profitStatement = this.calculateAmount(profitStatement, data, reCostMap, payCostMap);
        map.put(orderNo, profitStatement);
        reCost.put(orderNo, reCostMap);
        payCost.put(orderNo, payCostMap);
    }

    /**
     * 计算金额
     *
     * @param profitStatement
     * @param data
     * @param costMap
     * @param payCost
     * @return
     */
    private ProfitStatement calculateAmount(ProfitStatement profitStatement, ProfitStatementBasicData data,
                                            Map<String, Map<String, BigDecimal>> costMap,
                                            Map<String, Map<String, BigDecimal>> payCost) {

        //内部费用
        BigDecimal localMoney = data.getLocalMoney() == null ? data.getChangeAmount() : data.getLocalMoney();

        BigDecimal reEAmount = profitStatement.getReEquivalentAmount();
        BigDecimal payEAmount = profitStatement.getPayEquivalentAmount();

        if (data.getIsInternal()) {
            BigDecimal reInEAmount = profitStatement.getReInEquivalentAmount();
            BigDecimal payInEAmount = profitStatement.getPayInEquivalentAmount();
            if (data.getType() == 2) {
                profitStatement.setReInEquivalentAmount(BigDecimalUtil.add(reInEAmount, localMoney));
                costMap.computeIfAbsent("inCost", k -> new HashMap<>()).merge(data.getCurrencyName(), data.getAmount(), BigDecimal::add);
                profitStatement.setReInCostIds(StringUtils.add("", profitStatement.getReInCostIds(), data.getCostId().toString(), ","));
            } else {
                profitStatement.setPayInEquivalentAmount(BigDecimalUtil.add(payInEAmount, localMoney));
                payCost.computeIfAbsent("inCost", k -> new HashMap<>()).merge(data.getCurrencyName(), data.getAmount(), BigDecimal::add);
                profitStatement.setPayInCostIds(StringUtils.add("", profitStatement.getPayInCostIds(), data.getCostId().toString(), ","));
            }
            profitStatement.setInProfit(BigDecimalUtil.subtract(profitStatement.getReInEquivalentAmount(), profitStatement.getPayInEquivalentAmount()));
        } else {
            if (data.getType() == 2) {
                profitStatement.setReEquivalentAmount(BigDecimalUtil.add(reEAmount, localMoney));
                costMap.computeIfAbsent("cost", k -> new HashMap<>()).merge(data.getCurrencyName(), data.getAmount(), BigDecimal::add);
                profitStatement.setReCostIds(StringUtils.add("", profitStatement.getReCostIds(), data.getCostId().toString(), ","));
            } else {
                profitStatement.setPayEquivalentAmount(BigDecimalUtil.add(payEAmount, localMoney));
                payCost.computeIfAbsent("cost", k -> new HashMap<>()).merge(data.getCurrencyName(), data.getAmount(), BigDecimal::add);
                profitStatement.setPayCostIds(StringUtils.add("", profitStatement.getPayCostIds(), data.getCostId().toString(), ","));
            }
            profitStatement.setProfit(BigDecimalUtil.subtract(profitStatement.getReEquivalentAmount(), profitStatement.getPayEquivalentAmount()));
        }

        return profitStatement;
    }

    @Override
    public ProfitStatementBillVO getProfitStatementBill(List<String> reCostIds, List<String> payCostIds) {
        List<ProfitStatementBillDetailsVO> reBills = this.receivableBillDetailService.statisticsBillByCostIds(reCostIds);
        List<ProfitStatementBillDetailsVO> payBills = this.paymentBillDetailService.statisticsBillByCostIds(payCostIds);
        Map<String, String> currencyMap = this.omsClient.initCurrencyInfo().getData().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
        reBills.forEach(e -> e.setMoney(e.getSettlementAmount() + " " + currencyMap.get(e.getCurrencyCode())));
        payBills.forEach(e -> e.setMoney(e.getSettlementAmount() + " " + currencyMap.get(e.getCurrencyCode())));
        return new ProfitStatementBillVO().setReBills(reBills).setPayBills(payBills);
    }


    private void internalTransactionProcessing(String mainOrderNo, String orderNo, ProfitStatement profitStatement,
                                               Map<String, ProfitStatement> map, Map<String, ProfitStatement> tmpProfitStatement,
                                               Map<Long, String> departmentMap, Long finalMainLegalId,
                                               String finalMainLegalName, Long finalBizBelongDepartId) {
        if (!profitStatement.getIsMain() &&
                (!StringUtils.isEmpty(profitStatement.getReInCostIds())) || !StringUtils.isEmpty(profitStatement.getPayInCostIds())) {
            ProfitStatement tmpMain = tmpProfitStatement.get(mainOrderNo);
            if (tmpMain == null && !StringUtils.isEmpty(profitStatement.getPayInCostIds())) {
                ProfitStatement tmp = new ProfitStatement().setMainOrderId(profitStatement.getMainOrderId())
                        .setClassCode(profitStatement.getClassCode()).setMainOrderNo(profitStatement.getMainOrderNo())
                        .setIsMain(true).setBizType(profitStatement.getBizType()).setLegalName(finalMainLegalName)
                        .setLegalId(finalMainLegalId).setDepartment(departmentMap.get(finalBizBelongDepartId)).setDepartmentId(finalBizBelongDepartId)
                        .setCustomerName(profitStatement.getCustomerName()).setBizUname(profitStatement.getBizUname())
                        .setCreateTime(profitStatement.getCreateTime()).setPaySubCostIds(profitStatement.getPayInCostIds())
                        .setPaySubAmount(profitStatement.getPayInAmount()).setPaySubEquivalentAmount(profitStatement.getPayInEquivalentAmount())
                        .setSubProfit(profitStatement.getPayInEquivalentAmount())
                        .setCreateUser(profitStatement.getCreateUser());

//                ProfitStatement convert = ConvertUtil.convert(profitStatement, ProfitStatement.class);
//                convert.setOrderNo(null).setIsMain(true).setLegalName(finalMainLegalName).setLegalId(finalMainLegalId)
//                        .setDepartment(departmentMap.get(finalBizBelongDepartId)).setDepartmentId(finalBizBelongDepartId)
//                        .setUnitName(null).setUnitCode(null).setReAmount(null).setReEquivalentAmount(null)
//                        .setPayAmount(null).setPayEquivalentAmount(null).setProfit(null)


                tmpProfitStatement.put(tmp.getMainOrderNo(), tmp);
            } else if (tmpMain != null) {
                tmpMain.setPaySubCostIds(StringUtils.add(tmpMain.getPaySubCostIds(), profitStatement.getPayInCostIds()))
                        .setPaySubAmount(StringUtils.add(tmpMain.getPaySubAmount(), profitStatement.getPayInAmount()))
                        .setPaySubEquivalentAmount(BigDecimalUtil.add(tmpMain.getPaySubEquivalentAmount(), profitStatement.getPayInEquivalentAmount()))
                        .setSubProfit(BigDecimalUtil.add(tmpMain.getSubProfit(), profitStatement.getPayInEquivalentAmount()));
            }


        }

    }


    @Override
    public void exportData(JSONArray jsonArray, Boolean isOpenInternal) {
        if (jsonArray.size() == 0) {
            return;
        }
        List<String> reAmounts = new ArrayList<>();
        List<String> payAmounts = new ArrayList<>();
        BigDecimal reEquivalentAmount = new BigDecimal(0);
        BigDecimal payEquivalentAmount = new BigDecimal(0);
        BigDecimal profit = new BigDecimal(0);
        Boolean isMain = jsonArray.getJSONObject(0).getBool("isMain");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            reAmounts.add(jsonObject.getStr("reAmount"));
            payAmounts.add(jsonObject.getStr("payAmount"));
            if (isMain && isOpenInternal) {
                reEquivalentAmount = BigDecimalUtil.add(reEquivalentAmount, jsonObject.getBigDecimal("reInEquivalentAmount"));
                payEquivalentAmount = BigDecimalUtil.add(profit, jsonObject.getBigDecimal("payInEquivalentAmount"));
                jsonObject.putOnce("reEquivalentAmount", jsonObject.getBigDecimal("reInEquivalentAmount"));
                jsonObject.putOnce("payEquivalentAmount", jsonObject.getBigDecimal("payInEquivalentAmount"));
                jsonObject.putOnce("profit", jsonObject.getBigDecimal("inProfit"));
            } else {
                reEquivalentAmount = BigDecimalUtil.add(reEquivalentAmount, jsonObject.getBigDecimal("reEquivalentAmount"));
                payEquivalentAmount = BigDecimalUtil.add(payEquivalentAmount, jsonObject.getBigDecimal("payEquivalentAmount"));
            }
            profit = BigDecimalUtil.add(profit, jsonObject.getBigDecimal("profit"));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("re", jsonArray);
        Map<String, List<Object>> response = new HashMap<>();
        response.put("pro", jsonObject.get("re", List.class));
        JSONObject json = new JSONObject();

        json.putOnce("totalReAmount", this.commonService.calculatingCosts(reAmounts));
        json.putOnce("totalReEquivalentAmount", reEquivalentAmount);
        json.putOnce("totalPayAmount", this.commonService.calculatingCosts(payAmounts));
        json.putOnce("totalPayEquivalentAmount", payEquivalentAmount);
        json.putOnce("totalProfit", profit);

        String path;
        String fileName;
        if (isMain) {
            path = depart;
            fileName = "所属部门利润报表";
        } else {
            path = oprDepart;
            fileName = "操作部门利润报表";
        }
        EasyExcelUtils.fillTemplate1(json, response, path,
                null, fileName, HttpUtils.getHttpResponseServletContext());

    }
}
