package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryNotPaidBillForm;
import com.jayud.finance.bo.QueryPaymentBillForm;
import com.jayud.finance.bo.QueryPaymentBillNumForm;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderPaymentBillMapper extends BaseMapper<OrderPaymentBill> {

    /**
     * 主订单应付出账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentBillByPage(Page page, @Param("form") QueryPaymentBillForm form);

    /**
     * 子订单应付出账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentSubBillByPage(Page page, @Param("form") QueryPaymentBillForm form);

    /**
     * 已生成对账单列表
     * @param form
     * @return
     */
    List<OrderPaymentBillNumVO> findPaymentBillNum(@Param("form") QueryPaymentBillNumForm form);

    /**
     * 应付未出账单列表分页查询
     * @param form
     * @return
     */
    IPage<PaymentNotPaidBillVO> findNotPaidBillByPage(Page page,@Param("form") QueryNotPaidBillForm form);

    /**
     * 获取已出账订单数
     * @param legalName
     * @param supplierChName
     * @return
     */
    Integer getBillOrderNum(@Param("legalName") String legalName,@Param("supplierChName") String supplierChName,@Param("subType") String subType);

    /**
     * 统计已出账金额alreadyPaidAmount
     * @param legalName
     * @param supplierChName
     * @return
     */
    BigDecimal getAlreadyPaidAmount(@Param("legalName") String legalName, @Param("supplierChName") String supplierChName, @Param("subType") String subType);

    /**
     * 统计账单数billNum
     * @param legalName
     * @param supplierChName
     * @return
     */
    Integer getBillNum(@Param("legalName") String legalName,@Param("supplierChName") String supplierChName,@Param("subType") String subType);

    /**
     * 预览账单表头
     * @param costIds
     * @return
     */
    List<SheetHeadVO> findSheetHead(@Param("costIds") List<Long> costIds);

    /**
     * 预览账单分页查询
     * @param costIds
     * @return
     */
    List<ViewFBilToOrderVO> viewPaymentBill(@Param("costIds") List<Long> costIds);

    /**
     * 查询账单明细
     * @param costIds
     * @return
     */
    List<ViewBillToCostClassVO> findCostClass(@Param("costIds") List<Long> costIds);

    /**
     * 预览账单全局数据
     * @param costIds
     * @param cmd
     * @return
     */
    ViewBillVO getViewBillByCostIds(@Param("costIds") List<Long> costIds,@Param("cmd") String cmd);

    /**
     * 从删除的costIds里面挑出那种保存确定的数据
     * @param costIds
     * @return
     */
    List<Long> findSaveConfirmData(@Param("costIds") List<Long> costIds);




    /**
     * 主订单应付出账单分页查询
     * @param page
     * @param form
     * @param legalIds
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentBillByPage(Page<OrderPaymentBillVO> page,@Param("form") QueryPaymentBillForm form,@Param("legalIds") List<Long> legalIds);

    /**
     * 子订单应付出账单分页查询
     * @param page
     * @param form
     * @param sqlParam
     * @param legalIds
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentSubBillByPage(Page<OrderPaymentBillVO> page, @Param("form") QueryPaymentBillForm form, Map<String, Object> dynamicSqlParam, @Param("legalIds") List<Long> legalIds);

}
