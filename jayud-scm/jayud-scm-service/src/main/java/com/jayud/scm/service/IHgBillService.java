package com.jayud.scm.service;

import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.HgBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.*;

import java.util.List;

/**
 * <p>
 * 报关单主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
public interface IHgBillService extends IService<HgBill> {

    List<HgBillVO> getHgBillByBookingId(QueryCommonForm form);

    boolean delete(DeleteForm deleteForm);

    boolean entryCustomDate(QueryCommonForm form);

    Integer addHgBill(Integer bookingId);

    List<SingleWindowData> getSingleWindowData(QueryCommonForm form);

    List<HgBillVO> getHgBillDataByDeclareState(QueryCommonForm form);

    boolean submitSingleWindow(QueryCommonForm form);

    boolean updateHgBill(QueryCommonForm form);

    boolean submitYunBaoGuan(HgBill hgBill);

    List<YunBaoGuanData> getYunBaoGuanData(Integer id);

    List<HgBill> getHgBillDataByCustomsState();

    boolean updateHgBillByYunBaoGuan(HgBill hgBill);
}
