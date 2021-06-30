package com.jayud.finance.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderReceivableBill;
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
public interface IOrderReceivableBillService extends IService<OrderReceivableBill> {

    /**
     * 查询应付出账单列表
     *
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveBillByPage(QueryReceiveBillForm form);

    /**
     * 已生成对账单列表
     *
     * @param form
     * @return
     */
    Map<String, Object> findReceiveBillNum(QueryReceiveBillNumForm form);

    /**
     * 查询应收未出账单列表
     *
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form);

    /**
     * 生成应付账单
     *
     * @param form
     * @return
     */
    CommonResult createReceiveBill(CreateReceiveBillForm form);

    /**
     * 预览账单
     *
     * @param form
     * @param costIds
     * @return
     */
    JSONArray viewReceiveBill(ViewSBillForm form, List<Long> costIds);


    /**
     * 预览账单 TODO 改版,等全部修改完成弃用viewReceiveBill
     *
     * @param form
     * @param costIds
     * @return
     */
    public JSONArray viewReceiveBillInfo(ViewSBillForm form, List<Long> costIds);

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
    public List<SheetHeadVO> findSheetHeadInfo(List<Long> costIds, Map<String, Object> callbackArg, String cmd);

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
     * @param unitAccount
     * @param subType
     * @return
     */
    Integer getSBillOrderNum(String legalName, String unitAccount, String subType);

    /**
     * 统计已出账金额alreadyPaidAmount
     *
     * @param legalName
     * @param unitAccount
     * @param subType
     * @return
     */
    BigDecimal getSAlreadyPaidAmount(String legalName, String unitAccount, String subType);


    /**
     * TODO 根据法人id和结算code
     * 统计已出账金额alreadyPaidAmount
     *
     * @return
     */
    public BigDecimal getSAlreadyPaidAmountByLegalId(@Param("legalId") Long legalId, @Param("unitCode") String unitCode, @Param("subType") String subType);

    /**
     * 统计账单数billNum
     *
     * @param legalName
     * @param unitAccount
     * @param subType
     * @return
     */
    Integer getSBillNum(String legalName, String unitAccount, String subType);

    /**
     * 从删除的costIds里面挑出那种保存确定的数据
     *
     * @param costIds
     * @return
     */
    List<Long> findSaveConfirmData(List<Long> costIds);

    /**
     * 根据主订单获取中转仓地址
     *
     * @param orderNo
     * @return
     */
    String getWarehouseAddress(String orderNo);


    /**
     * 配置汇率
     *
     * @return
     */
    List<OrderBillCostTotalVO> configureExchangeRate(List<Long> costIds, String settlementCurrency, String accountTermStr,
                                                     Boolean isCustomExchangeRate, List<InitComboxStrVO> customExchangeRates);

    /**
     * 统计账单
     *
     * @param orderBillCostTotalVOS
     * @param orderReceivableBill
     * @param orderNos
     * @param subType
     * @return
     */
    OrderReceivableBill statisticsBill(List<OrderBillCostTotalVO> orderBillCostTotalVOS, OrderReceivableBill orderReceivableBill, List<String> orderNos, String subType);


    /**
     * 根据条件查询信息
     *
     * @param orderReceivableBill
     * @return
     */
    List<OrderReceivableBill> getByCondition(OrderReceivableBill orderReceivableBill);

    /**
     * 订单维度展示未出账单
     *
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findNotPaidOrderBillByPage(QueryNotPaidBillForm form);

    /**
     * 根据创建账单时间查询数量
     *
     * @param makeTime
     * @param format
     * @return
     */
    int getCountByMakeTime(String makeTime, String format);

    /**
     * 统计账单数据(根据账单id)
     *
     * @param billId
     */
    public void statisticsBill(Long billId);

    /**
     * 拼接动态sql
     *
     * @param map
     * @return
     */
    public Map<String, Object> dynamicSQLFindReceiveBillByPageParam(Map<String, Object> map);
}
