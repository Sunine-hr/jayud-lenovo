package com.jayud.finance.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.vo.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderPaymentBillService extends IService<OrderPaymentBill> {

    /**
     * 查询应付出账单列表
     *
     * @param form
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form);

    /**
     * 已生成对账单列表
     *
     * @param form
     * @return
     */
    Map<String, Object> findPaymentBillNum(QueryPaymentBillNumForm form);

    /**
     * 查询应付未出账单列表
     *
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form);

    /**
     * 生成应付账单
     *
     * @param form
     * @return
     */
    CommonResult createPaymentBill(CreatePaymentBillForm form);

    /**
     * 预览账单
     *
     * @param form
     * @param costIds
     * @return
     */
    JSONArray viewPaymentBill(ViewFBillForm form, List<Long> costIds);

    /**
     * 预览账单 预览账单表头 TODO 改版,等全部修改完成弃用viewPaymentBill
     *
     * @param form
     * @param costIds
     * @return
     */
    JSONArray viewPaymentBillInfo(ViewFBillForm form, List<Long> costIds);

    /**
     * 预览账单表头
     *
     * @return
     */
    List<SheetHeadVO> findSheetHead(List<Long> costIds);

    /**
     * 预览账单表头 TODO 改版,等全部修改完成弃用findSheetHead
     *
     * @return
     */
    List<SheetHeadVO> findSheetHeadInfo(List<Long> costIds, Map<String, Object> callbackArg, String cmd);

    /**
     * 预览账单全局数据
     *
     * @return
     */
    ViewBillVO getViewBillByCostIds(List<Long> costIds, String cmd);

    /**
     * 已出账订单数
     *
     * @param legalName
     * @param supplierChName
     * @param subType
     * @return
     */
    Integer getBillOrderNum(String legalName, String supplierChName, String subType);

    /**
     * 统计已出账金额alreadyPaidAmount
     *
     * @param legalName
     * @param supplierChName
     * @param subType
     * @return
     */
    BigDecimal getAlreadyPaidAmount(String legalName, String supplierChName, String subType);

    /**
     * 统计账单数billNum
     *
     * @param legalName
     * @param supplierChName
     * @param subType
     * @return
     */
    Integer getBillNum(String legalName, String supplierChName, String subType);

    /**
     * 账单号是否存在
     *
     * @param billNo
     * @return
     */
    Boolean isExistBillNo(String billNo);

    /**
     * 从删除的costIds里面挑出那种保存确定的数据
     *
     * @param costIds
     * @return
     */
    List<Long> findSaveConfirmData(List<Long> costIds);

    /**
     * 配置汇率
     *
     * @param costIds
     * @param settlementCurrency
     * @param accountTermStr
     * @param isCustomExchangeRate
     * @param customExchangeRate
     */
    List<OrderBillCostTotalVO> configureExchangeRate(List<Long> costIds, String settlementCurrency, String accountTermStr,
                                                     Boolean isCustomExchangeRate, List<InitComboxStrVO> customExchangeRate);

    /**
     * 统计账单
     *
     * @param orderBillCostTotalVOS
     * @param orderPaymentBill
     * @param orderNos
     * @param subType
     * @return
     */
    OrderPaymentBill statisticsBill(List<OrderBillCostTotalVO> orderBillCostTotalVOS, OrderPaymentBill orderPaymentBill, List<String> orderNos, String subType);

    /**
     * 根据条件查询账单
     * @param paymentBill
     * @return
     */
    List<OrderPaymentBill> getByCondition(OrderPaymentBill paymentBill);

    /**
     * 根据创建账单时间查询数量
     * @param makeTime
     * @param format
     * @return
     */
    int getCountByMakeTime(@Param("makeTime") String makeTime, String format);

    /**
     * 订单维度展示未出账单
     *
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findNotPaidOrderBillByPage(QueryNotPaidBillForm form);
}
