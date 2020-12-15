package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OceanCounterWaybillRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 货柜对应运单(订单)关联表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-15
 */
@Mapper
@Component
public interface OceanCounterWaybillRelationMapper extends BaseMapper<OceanCounterWaybillRelation> {

}
