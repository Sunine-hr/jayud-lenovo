package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.po.BookingOrderFollow;
import com.jayud.scm.model.vo.BookingOrderFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 委托单跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Mapper
public interface BookingOrderFollowMapper extends BaseMapper<BookingOrderFollow> {

    /**
     * 跟踪记录，查询
     * @param bookingId
     * @return
     */
    List<BookingOrderFollowVO> findBookingOrderFollow(@Param("bookingId") Integer bookingId);
}
