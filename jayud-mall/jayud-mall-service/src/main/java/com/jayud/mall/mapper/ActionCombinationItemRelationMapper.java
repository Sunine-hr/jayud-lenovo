package com.jayud.mall.mapper;

import com.jayud.mall.model.po.ActionCombinationItemRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 操作组合操作项目关联表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Mapper
@Component
public interface ActionCombinationItemRelationMapper extends BaseMapper<ActionCombinationItemRelation> {

}
