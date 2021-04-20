package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.JayudBizException;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderBillCostTotalMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.vo.EditBillDateilVO;
import com.jayud.finance.vo.InitComboxStrVO;
import com.jayud.finance.vo.OrderBillCostTotalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private IOrderReceivableBillDetailService orderReceivableBillDetailService;
    @Autowired
    private IOrderPaymentBillDetailService orderPaymentBillDetailService;
    @Autowired
    private OmsClient omsClient;

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
        String head = "合计结算币种（" + datas.getJSONObject(0).getStr("settlementCurrency") + "）";
        //头部结算币种
        dynamicHead.put(key, head);
        headMap.put(key, head);
        //计算结算币种
        this.calculateSettlementCurrency(key, datas, moneyType);
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
            condition.lambda().eq(OrderBillCostTotal::getBillNo, object.getStr("billNo"))
                    .eq(OrderBillCostTotal::getOrderNo, object.getStr("subOrderNo") == null
                            ? object.getStr("orderNo") : object.getStr("subOrderNo"))
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

    /**
     * 根据账单编号查询编辑对账账单详情
     *
     * @param billNo
     * @param type   类型(0:应收,1:应付)
     * @return
     */
    @Override
    public EditBillDateilVO getEditBillByBillNo(String billNo, Integer type) {
        //根据账单编号查询对账单详情:账单编号,核算期,结算币种,是否自定义汇率
        //自定义汇率
        EditBillDateilVO editBillDateilVO = new EditBillDateilVO();
        editBillDateilVO.setBillNo(billNo);
        String moneyType = null;
        switch (type) {
            case 0: //应收
                List<OrderReceivableBillDetail> receivableBillDetails = this.orderReceivableBillDetailService.getByBillNo(billNo);
                if (receivableBillDetails.size() == 0) {
                    throw new JayudBizException(400, "未找到相应对账单信息");
                }
                OrderReceivableBillDetail orderReceivableBillDetail = receivableBillDetails.get(0);
                editBillDateilVO.setAccountTermStr(orderReceivableBillDetail.getAccountTerm());
                editBillDateilVO.setSettlementCurrency(orderReceivableBillDetail.getSettlementCurrency());
                moneyType = "2";
                break;
            case 1: //应付
                List<OrderPaymentBillDetail> paymentBillDetails = this.orderPaymentBillDetailService.getByCondition(
                        new OrderPaymentBillDetail().setBillNo(billNo));
                if (paymentBillDetails.size() == 0) {
                    throw new JayudBizException(400, "未找到相应对账单信息");
                }
                OrderPaymentBillDetail paymentBillDetail = paymentBillDetails.get(0);
                editBillDateilVO.setAccountTermStr(paymentBillDetail.getAccountTerm());
                editBillDateilVO.setSettlementCurrency(paymentBillDetail.getSettlementCurrency());
                moneyType = "1";
                break;
            default:
                throw new JayudBizException("不存在这种操作类型");
        }

        QueryWrapper<OrderBillCostTotal> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderBillCostTotal::getBillNo, billNo).eq(OrderBillCostTotal::getMoneyType, moneyType);
        List<OrderBillCostTotal> orderBillCostTotals = this.baseMapper.selectList(condition);

        Boolean isCustomExchangeRate = false;
        List<InitComboxStrVO> customExchangeRate = new ArrayList<>();
        List<InitComboxStrVO> data = omsClient.initCurrencyInfo().getData();
        for (OrderBillCostTotal orderBillCostTotal : orderBillCostTotals) {
            if (orderBillCostTotal.getIsCustomExchangeRate() != null) {
                isCustomExchangeRate = orderBillCostTotal.getIsCustomExchangeRate();
            }
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setCode(orderBillCostTotal.getCurrentCurrencyCode());
            initComboxStrVO.setNote(orderBillCostTotal.getExchangeRate() == null ? null : orderBillCostTotal.getExchangeRate().toPlainString());
            customExchangeRate.add(initComboxStrVO);
        }

        customExchangeRate = customExchangeRate.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(InitComboxStrVO::getCode))), ArrayList::new)
        );
        editBillDateilVO.setIsCustomExchangeRate(isCustomExchangeRate);
        editBillDateilVO.setCustomExchangeRate(customExchangeRate);
        editBillDateilVO.assembleCurrencyName(data);

        return editBillDateilVO;
    }

    /**
     * 合计币种
     *
     * @param billNos
     * @return
     */
    @Override
    public List<Map<String, Object>> totalCurrencyAmount(List<String> billNos) {
        return this.baseMapper.totalCurrencyAmount(billNos);
    }
}
