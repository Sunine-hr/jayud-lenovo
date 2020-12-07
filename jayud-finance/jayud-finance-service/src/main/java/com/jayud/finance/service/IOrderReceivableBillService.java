package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.bo.CreateReceiveBillForm;
import com.jayud.finance.bo.QueryNotPaidBillForm;
import com.jayud.finance.bo.QueryReceiveBillForm;
import com.jayud.finance.bo.QueryReceiveBillNumForm;
import com.jayud.finance.po.OrderReceivableBill;
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
public interface IOrderReceivableBillService extends IService<OrderReceivableBill> {

    /**
     * 查询应付出账单列表
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveBillByPage(QueryReceiveBillForm form);

    /**
     * 已生成对账单列表
     * @param form
     * @return
     */
    Map<String,Object> findReceiveBillNum(QueryReceiveBillNumForm form);

    /**
     * 查询应收未出账单列表
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form);

    /**
     * 生成应付账单
     * @param form
     * @return
     */
    Boolean createReceiveBill(CreateReceiveBillForm form);

    /**
     * 预览账单
     * @param costIds
     * @return
     */
    List<ViewBilToOrderVO> viewReceiveBill(List<Long> costIds);

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
     * @param unitAccount
     * @param subType
     * @return
     */
    Integer getSBillOrderNum(String legalName,String unitAccount,String subType);

    /**
     * 统计已出账金额alreadyPaidAmount
     * @param legalName
     * @param unitAccount
     * @param subType
     * @return
     */
    BigDecimal getSAlreadyPaidAmount(String legalName, String unitAccount, String subType);

    /**
     * 统计账单数billNum
     * @param legalName
     * @param unitAccount
     * @param subType
     * @return
     */
    Integer getSBillNum(String legalName, String unitAccount, String subType);

    /**
     * 从删除的costIds里面挑出那种保存确定的数据
     * @param costIds
     * @return
     */
    List<Long> findSaveConfirmData(List<Long> costIds);

    /**
     * 根据主订单获取中转仓地址
     * @param orderNo
     * @return
     */
    String getWarehouseAddress(String orderNo);
}
