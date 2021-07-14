package com.jayud.finance.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.bo.QueryProfitStatementForm;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.ProfitStatementMapper;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.service.IProfitStatementService;
import com.jayud.finance.vo.ProfitStatementBasicData;
import com.jayud.finance.vo.ProfitStatementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static void main(String[] args) {
        Map<String, BigDecimal> map = new HashMap<>(3);
        map.put("CNY", new BigDecimal(2));
        map.put("RMB", new BigDecimal(3));
        map.merge("GB", new BigDecimal(3), BigDecimal::add);
        System.out.println(new JSONObject(map));
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
        Map<String, String> customerMap = this.omsClient.getCustomerName().getData();
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

            for (ProfitStatementBasicData data : v) {
                data.setCurrencyName(currencyMap.get(data.getCurrencyCode()));
                data.setDepartment(departmentMap.get(data.getDepartmentId()));
                data.setUnitName(customerMap.get(data.getUnitCode()));
                data.setBizType(bizService.get(data.getBizCode()));

                //主订单统计维度
                if (data.getIsSumToMain()) {
                    this.doStatisticalProfitReport(data.getMainOrderNo(), data, map, reCost, payCost);
                } else {
                    //子订单统计维度
                    this.doStatisticalProfitReport(data.getOrderNo(), data, map, reCost, payCost);
                }
            }
            map.forEach((k1, v1) -> {
                v1.setReAmount(this.assemblyCost(reCost.get(k1).get("cost")))
                        .setReInAmount(this.assemblyCost(reCost.get(k1).get("inCost")))
                        .setPayAmount(this.assemblyCost(payCost.get(k1).get("cost")))
                        .setPayInAmount(this.assemblyCost(payCost.get(k1).get("inCost")));
            });
            list.addAll(new ArrayList<>(map.values()));
        });

        return list;
    }

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

    /**
     * 查询利润报表数据
     *
     * @param form
     * @return
     */
    @Override
    public List<ProfitStatementVO> list(QueryProfitStatementForm form) {
        List<ProfitStatementVO> list = this.baseMapper.list(form);
        list.forEach(e -> {
            e.totalInternalExpenses(form.getIsOpenInternal());
        });
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
            profitStatement.setDepartment(data.getDepartment());
        }
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

}
