package com.jayud.mall.service;

import com.jayud.mall.model.po.ActionCombinationItemRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ActionCombinationItemRelationVO;

import java.util.List;

/**
 * <p>
 * 操作组合操作项目关联表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
public interface IActionCombinationItemRelationService extends IService<ActionCombinationItemRelation> {

    /**
     * 根据操作项组合id，查询关联信息
     * @param actionCombinationId 操作项组合id(action_combination id)
     * @return
     */
    List<ActionCombinationItemRelationVO> findActionCombinationItemRelationByActionCombinationId(Integer actionCombinationId);
}
