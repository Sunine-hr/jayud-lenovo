package com.jayud.trailer.mapper;

import com.jayud.trailer.po.TrailerOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.trailer.vo.TrailerOrderVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 拖车订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Mapper
public interface TrailerOrderMapper extends BaseMapper<TrailerOrder> {

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    TrailerOrderVO getTrailerOrder(Long id);
}
