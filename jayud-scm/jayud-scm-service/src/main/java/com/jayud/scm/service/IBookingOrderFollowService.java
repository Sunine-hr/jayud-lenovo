package com.jayud.scm.service;

import com.jayud.scm.model.bo.BookingOrderFollowForm;
import com.jayud.scm.model.po.BookingOrderFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BookingOrderFollowVO;

import java.util.List;

/**
 * <p>
 * 委托单跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
public interface IBookingOrderFollowService extends IService<BookingOrderFollow> {

    /**
     * 跟踪记录，查询
     * @param bookingId
     * @return
     */
    List<BookingOrderFollowVO> findBookingOrderFollow(Integer bookingId);

    /**
     * 跟踪记录，新增
     * @param form
     */
    void saveBookingOrderFollow(BookingOrderFollowForm form);
}
