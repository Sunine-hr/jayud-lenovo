package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CustomerGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 客户商品表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface CustomerGoodsMapper extends BaseMapper<CustomerGoods> {

}
