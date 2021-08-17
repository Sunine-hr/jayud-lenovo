package com.jayud.scm.mapper;

import com.jayud.scm.model.po.BookingOrderEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 委托订单明细表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Mapper
public interface BookingOrderEntryMapper extends BaseMapper<BookingOrderEntry> {

    /**
     * 商品明细表，list查询
     * @param bookingId
     * @return
     */
    List<BookingOrderEntryVO> findBookingOrderEntryByBookingId(@Param("bookingId") Integer bookingId);

    /**
     * 商品明细表，查看
     * @param id
     * @return
     */
    BookingOrderEntryVO getBookingOrderEntryById(@Param("id") Integer id);

}
