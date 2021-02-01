package com.jayud.oceanship.mapper;

import com.jayud.oceanship.po.SeaOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oceanship.vo.SeaOrderVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 海运订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface SeaOrderMapper extends BaseMapper<SeaOrder> {

    /**
     * 根据订单id获取订单信息
     * @param id
     * @return
     */
    SeaOrderVO getSeaOrder(@Param("id") Long id);
}
