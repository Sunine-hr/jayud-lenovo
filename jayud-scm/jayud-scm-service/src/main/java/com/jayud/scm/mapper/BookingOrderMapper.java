package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryBookingOrderForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.model.vo.QtyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 委托订单主表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Mapper
public interface BookingOrderMapper extends BaseMapper<BookingOrder> {

    /**
     * 出口委托单，分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<BookingOrderVO> findBookingOrderByPage(@Param("page") Page<BookingOrderVO> page, @Param("form") QueryBookingOrderForm form);

    /**
     * 出口委托单，查看
     * @param id
     * @return
     */
    BookingOrderVO getBookingOrderById(@Param("id") Integer id);

    IPage<BookingOrderVO> findByPage(@Param("page") Page<BookingOrderVO> page, @Param("form") QueryBookingOrderForm form);

    void upOrderCheckValidate(@Param("map")Map map);

    QtyVO isCommplete(@Param("bookingId")Integer bookingId);

    void settlement(@Param("map") Map<String, Object> map);

    void estimatedUnitPrice(@Param("map")Map<String, Object> map);

    void reverseCalculation(@Param("map")Map<String, Object> map);
}
