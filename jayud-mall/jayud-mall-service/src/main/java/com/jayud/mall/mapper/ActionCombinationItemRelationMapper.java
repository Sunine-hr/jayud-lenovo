package com.jayud.mall.mapper;

import com.jayud.mall.model.po.ActionCombinationItemRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.ActionCombinationItemRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * 根据操作项组合id，查询关联信息
     * @param actionCombinationId 操作项组合id(action_combination id)
     * @return
     */
    List<ActionCombinationItemRelationVO> findActionCombinationItemRelationByActionCombinationId(@Param("actionCombinationId") Integer actionCombinationId);
}
