package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.finance.bo.*;
import com.jayud.finance.po.OrderReceivableBill;
import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param form
     * @return
     */
    List<ViewBilToOrderVO> viewReceiveBill(ViewBillForm form);

    /**
     * 预览账单表头
     * @return
     */
    List<SheetHeadVO> findSheetHead(ViewBillForm form);
}
