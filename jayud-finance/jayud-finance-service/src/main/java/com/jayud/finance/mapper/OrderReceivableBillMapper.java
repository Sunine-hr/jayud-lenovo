package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryNotPaidBillForm;
import com.jayud.finance.bo.QueryReceiveBillForm;
import com.jayud.finance.bo.QueryReceiveBillNumForm;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderReceivableBillMapper extends BaseMapper<OrderReceivableBill> {

    /**
     * 主订单应收出账单分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveBillByPage(Page page, @Param("form") QueryReceiveBillForm form);

    /**
     * 子订单应收出账单分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveSubBillByPage(Page page, @Param("form") QueryReceiveBillForm form);

    /**
     * 已生成对账单列表
     *
     * @param form
     * @return
     */
    List<OrderPaymentBillNumVO> findReceiveBillNum(@Param("form") QueryReceiveBillNumForm form);

    /**
     * 应收未出账单列表分页查询
     *
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findNotPaidBillByPage(Page page,
                                                      @Param("form") QueryNotPaidBillForm form,
                                                      @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);

    /**
     * 应收未出账单列表分页查询(订单维度)
     *
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findNotPaidOrderBillByPage(Page page,
                                                           @Param("form") QueryNotPaidBillForm form,
                                                           @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);

    /**
     * 获取已出账订单数
     *
     * @param legalName
     * @param unitAccount
     * @return
     */
    Integer getSBillOrderNum(@Param("legalName") String legalName, @Param("unitAccount") String unitAccount, @Param("subType") String subType);

    /**
     * 统计已出账金额alreadyPaidAmount
     *
     * @param legalName
     * @param unitAccount
     * @return
     */
    BigDecimal getSAlreadyPaidAmount(@Param("legalName") String legalName, @Param("unitAccount") String unitAccount, @Param("subType") String subType);


    /**
     * 统计已出账金额alreadyPaidAmount
     *
     * @return
     */
    BigDecimal getSAlreadyPaidAmountByLegalId(@Param("legalId") Long legalId, @Param("unitCode") String unitCode, @Param("subType") String subType);


    /**
     * 统计账单数billNum
     *
     * @param legalName
     * @param unitAccount
     * @return
     */
    Integer getSBillNum(@Param("legalName") String legalName, @Param("unitAccount") String unitAccount, @Param("subType") String subType);

    /**
     * 预览账单表头
     *
     * @param costIds
     * @return
     */
    List<SheetHeadVO> findSheetHead(@Param("costIds") List<Long> costIds);

    /**
     * 预览账单分页查询
     *
     * @param costIds
     * @return
     */
    List<ViewBilToOrderVO> viewReceiveBill(@Param("costIds") List<Long> costIds);

    /**
     * 预览账单全局数据
     *
     * @param costIds
     * @param cmd
     * @param dynamicSqlParam
     * @return
     */
    ViewBillVO getViewBillByCostIds(@Param("costIds") List<Long> costIds, @Param("cmd") String cmd,
                                    @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);

    /**
     * 查询账单明细
     *
     * @param costIds
     * @return
     */
    List<ViewBillToCostClassVO> findCostClass(@Param("costIds") List<Long> costIds);

    /**
     * 从删除的costIds里面挑出那种保存确定的数据
     *
     * @param costIds
     * @return
     */
    List<Long> findSaveConfirmData(@Param("costIds") List<Long> costIds);

    /**
     * 根据主订单获取中转仓地址
     *
     * @param orderNo
     * @return
     */
    String getWarehouseAddress(@Param("orderNo") String orderNo);


    /**
     * 主订单应收出账单分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveBillByPage(Page<OrderReceiveBillVO> page, @Param("form") QueryReceiveBillForm form, @Param("legalIds") List<Long> legalIds);

    /**
     * 子订单应收出账单分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveSubBillByPage(Page<OrderReceiveBillVO> page, @Param("form") QueryReceiveBillForm form, @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam, @Param("legalIds") List<Long> legalIds);


    /**
     * 应收未出账单基础列表分页查询
     *
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findBaseNotPaidBillByPage(Page page, @Param("form") QueryNotPaidBillForm form, @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);


    List<Map<String, Object>> statisticsNotPaidBillInfo(@Param("isMain") Boolean isMain, @Param("customerCode") String customerCode,
                                                        @Param("legalEntityId") Long legalEntityId,
                                                        @Param("legalName") String legalName,
                                                        @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);

    Integer getSBillOrderNumByLegalId(@Param("legalId") Long legalEntityId, @Param("unitCode") String unitCode, @Param("subType") String subType);

    Integer getSBillNumByLegalId(@Param("legalId") Long legalEntityId, @Param("unitCode") String unitCode, @Param("subType") String subType);

    /**
     * 根据创建账单时间查询数量
     *
     * @param makeTime
     * @param format
     * @return
     */
    Integer getCountByMakeTime(@Param("makeTime") String makeTime, @Param("format") String format);


//    List<Map<String, Object>> statisticsNotPaidBillInfo(@Param("isMain") Boolean isMain, @Param("customerCode") String customerCode,
//                                                        @Param("legalEntityIds") List<Long> legalEntityIds,
//                                                        @Param("legalNames") List<String> legalNames,
//                                                        @Param("dynamicSqlParam") Map<String, Object> dynamicSqlParam);

}
