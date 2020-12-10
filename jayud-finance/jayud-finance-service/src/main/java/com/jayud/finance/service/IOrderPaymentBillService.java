package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderPaymentBillService extends IService<OrderPaymentBill> {

    /**
     * 查询应付出账单列表
     * @param form
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form);

    /**
     * 已生成对账单列表
     * @param form
     * @return
     */
    Map<String,Object> findPaymentBillNum(QueryPaymentBillNumForm form);

    /**
     * 查询应付未出账单列表
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form);

    /**
     * 生成应付账单
     * @param form
     * @return
     */
    CommonResult createPaymentBill(CreatePaymentBillForm form);

    /**
     * 预览账单
     * @param costIds
     * @return
     */
    List<ViewFBilToOrderVO> viewPaymentBill(List<Long> costIds);

    /**
     * 预览账单表头
     * @return
     */
    List<SheetHeadVO> findSheetHead(List<Long> costIds);

    /**
     * 预览账单全局数据
     * @return
     */
    ViewBillVO getViewBillByCostIds(List<Long> costIds);

    /**
     * 已出账订单数
     * @param legalName
     * @param supplierChName
     * @param subType
     * @return
     */
    Integer getBillOrderNum(String legalName,String supplierChName,String subType);

    /**
     * 统计已出账金额alreadyPaidAmount
     * @param legalName
     * @param supplierChName
     * @param subType
     * @return
     */
    BigDecimal getAlreadyPaidAmount(String legalName, String supplierChName, String subType);

    /**
     * 统计账单数billNum
     * @param legalName
     * @param supplierChName
     * @param subType
     * @return
     */
    Integer getBillNum(String legalName, String supplierChName, String subType);

    /**
     * 账单号是否存在
     * @param billNo
     * @return
     */
    Boolean isExistBillNo(String billNo);

    /**
     * 从删除的costIds里面挑出那种保存确定的数据
     * @param costIds
     * @return
     */
    List<Long> findSaveConfirmData(List<Long> costIds);

}
