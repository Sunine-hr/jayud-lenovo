package com.jayud.oms.mapper;

import com.jayud.oms.model.po.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 货品信息表 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2020-12-16
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
