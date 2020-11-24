package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.vo.*;

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
    Boolean createPaymentBill(CreatePaymentBillForm form);

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
     * @param cmd
     * @return
     */
    Integer getBillOrderNum(String legalName,String supplierChName,String cmd);

    /**
     * 账单号是否存在
     * @param billNo
     * @return
     */
    Boolean isExistBillNo(String billNo);

}
