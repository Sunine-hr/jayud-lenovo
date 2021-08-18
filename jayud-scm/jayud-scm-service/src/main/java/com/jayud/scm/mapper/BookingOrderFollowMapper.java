package com.jayud.scm.mapper;

import com.jayud.scm.model.po.BookingOrderFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
