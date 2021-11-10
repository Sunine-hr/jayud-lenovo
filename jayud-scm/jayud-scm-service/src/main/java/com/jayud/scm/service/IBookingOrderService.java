package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.BookingOrderForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryBookingOrderForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.vo.BookingOrderVO;

import java.util.List;

/**
 * <p>
 * 委托订单主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IBookingOrderService extends IService<BookingOrder> {

    /**
     * 出口委托单，分页查询
     * @param form
     * @return
     */
    IPage<BookingOrderVO> findBookingOrderByPage(QueryBookingOrderForm form);

    /**
     * 出口委托单，保存(新增、修改)
     * @param form
     */
    void saveBookingOrder(BookingOrderForm form);

    /**
     * 出口委托单，删除
     * @param id
     */
    void delBookingOrder(Integer id);

    /**
     * 出口委托单，查看
     * @param id
     * @return
     */
    BookingOrderVO getBookingOrderById(Integer id);

    /**
     * 出口委托单，审核
     * @param form
     */
    void auditBookingOrder(PermissionForm form);

    /**
     * 出口委托单，反审
     * @param form
     */
    void cancelAuditBookingOrder(PermissionForm form);

    /**
     * 出口委托单，打印
     * @param id
     */
    void printBookingOrder(Integer id);

    /**
     * 出口委托单，复制
     * @param id
     * @return
     */
    BookingOrderVO copyBookingOrder(Integer id);

    /**
     * 出口委托单，准备新增数据
     * @param modelType
     * @return
     */
    BookingOrderVO prepareBookingOrder(Integer modelType);

    BookingOrder getBookingOrderByBillId(Integer id);

    List<BookingOrder> getBookingOrderByHgTrackId(Integer id);

    IPage<BookingOrderVO> findByPage(QueryBookingOrderForm form);

    CommonResult upOrderCheckValidate(Integer id,Integer type);

    boolean isCommplete(Integer bookingId);

    CommonResult settlement(QueryCommonForm form);

    CommonResult importSettlement(QueryCommonForm form);

    CommonResult estimatedUnitPrice(Integer id);

    //出口核销反算人民币单价
    CommonResult reverseCalculation(Integer id);

    void temporaryStorageBookingOrder(BookingOrderForm bookingOrderForm);
}
