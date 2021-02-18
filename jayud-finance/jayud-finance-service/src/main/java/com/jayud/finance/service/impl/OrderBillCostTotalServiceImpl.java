package com.jayud.finance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.mapper.OrderBillCostTotalMapper;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.finance.vo.OrderBillCostTotalVO;
import com.jayud.finance.vo.ViewBilToOrderVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderBillCostTotalServiceImpl extends ServiceImpl<OrderBillCostTotalMapper, OrderBillCostTotal> implements IOrderBillCostTotalService {

    @Override
    public List<OrderBillCostTotalVO> findOrderFBillCostTotal(List<Long> costIds, String settlementCurrency, String accountTermStr) {
        return baseMapper.findOrderFBillCostTotal(costIds, settlementCurrency, accountTermStr);
    }

    @Override
    public List<OrderBillCostTotalVO> findOrderSBillCostTotal(List<Long> costIds, String settlementCurrency, String accountTermStr) {
        return baseMapper.findOrderSBillCostTotal(costIds, settlementCurrency, accountTermStr);
    }


    /**
     * 导出对账单:计算结算币种
     *
     * @param headMap
     * @param dynamicHead
     * @param datas
     * @param moneyType   1-应付 2-应收
     */
    @Override
    public void exportSettlementCurrency(LinkedHashMap<String, String> headMap,
                                         LinkedHashMap<String, String> dynamicHead,
                                         JSONArray datas,
                                         String moneyType) {
        String key = "totalSettlementCurrency";
        String head = "合计结算币种（" + datas.getJSONObject(0).getString("settlementCurrency") + "）";
        //头部结算币种
        dynamicHead.put(key, head);
        headMap.put(key, head);
        //计算结算币种
        this.calculateSettlementCurrency(key, datas, moneyType);
//        for (int i = 0; i < datas.size(); i++) {
//            JSONObject object = datas.getJSONObject(i);
//            QueryWrapper<OrderBillCostTotal> condition = new QueryWrapper<>();
//            condition.lambda().eq(OrderBillCostTotal::getBillNo, object.getString("billNo"))
//                    .eq(OrderBillCostTotal::getOrderNo, object.getString("subOrderNo") == null
//                            ? object.getString("orderNo") : object.getString("subOrderNo"))
//                    .eq(OrderBillCostTotal::getMoneyType, moneyType);
//            List<OrderBillCostTotal> orderBillCostTotals = this.baseMapper.selectList(condition);
//            //合计结算币种
//            BigDecimal money = orderBillCostTotals.stream().map(OrderBillCostTotal::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
//            object.put(key, money);
//        }


    }

    /**
     * 计算结算币种
     */
    @Override
    public void calculateSettlementCurrency(String key, JSONArray datas, String moneyType) {
        //计算结算币种
        for (int i = 0; i < datas.size(); i++) {
            JSONObject object = datas.getJSONObject(i);
            QueryWrapper<OrderBillCostTotal> condition = new QueryWrapper<>();
            condition.lambda().eq(OrderBillCostTotal::getBillNo, object.getString("billNo"))
                    .eq(OrderBillCostTotal::getOrderNo, object.getString("subOrderNo") == null
                            ? object.getString("orderNo") : object.getString("subOrderNo"))
                    .eq(OrderBillCostTotal::getMoneyType, moneyType);
            List<OrderBillCostTotal> orderBillCostTotals = this.baseMapper.selectList(condition);
            //合计结算币种
            BigDecimal money = orderBillCostTotals.stream().map(OrderBillCostTotal::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            object.put(key, money);
        }
    }

    /**
     * 根据账单编号和类型查询出账单详细信息
     */
    @Override
    public List<OrderBillCostTotal> getByBillNo(List<String> billNo, String moneyType) {
        QueryWrapper<OrderBillCostTotal> condition = new QueryWrapper<>();
        condition.lambda().in(OrderBillCostTotal::getBillNo, billNo)
                .eq(OrderBillCostTotal::getMoneyType, moneyType);
        return this.baseMapper.selectList(condition);
    }
}
