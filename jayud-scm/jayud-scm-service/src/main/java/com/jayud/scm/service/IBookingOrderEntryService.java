package com.jayud.scm.service;

import com.jayud.scm.model.bo.BookingOrderEntryForm;
import com.jayud.scm.model.po.BookingOrderEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BookingOrderEntryVO;

import java.util.List;

/**
 * <p>
 * 委托订单明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IBookingOrderEntryService extends IService<BookingOrderEntry> {

    /**
     * 商品明细表，list查询
     * @param bookingId
     * @return
     */
    List<BookingOrderEntryVO> findBookingOrderEntryByBookingId(Integer bookingId);

    /**
     * 商品明细表，保存(新增、修改)
     * @param form
     */
    void saveBookingOrderEntry(BookingOrderEntryForm form);

    /**
     * 商品明细表，删除
     * @param id
     */
    void delBookingOrderEntry(Integer id);

    /**
     * 商品明细表，查看
     * @param id
     * @return
     */
    BookingOrderEntryVO getBookingOrderEntryById(Integer id);

    /**
     * 商品明细表，复制
     * @param id
     * @return
     */
    BookingOrderEntryVO copyBookingOrderEntry(Integer id);
}
