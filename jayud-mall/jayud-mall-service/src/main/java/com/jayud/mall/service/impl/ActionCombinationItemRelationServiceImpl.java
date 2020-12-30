package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.ActionCombinationItemRelation;
import com.jayud.mall.mapper.ActionCombinationItemRelationMapper;
import com.jayud.mall.model.vo.ActionCombinationItemRelationVO;
import com.jayud.mall.service.IActionCombinationItemRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作组合操作项目关联表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Service
public class ActionCombinationItemRelationServiceImpl extends ServiceImpl<ActionCombinationItemRelationMapper,
        ActionCombinationItemRelation> implements IActionCombinationItemRelationService {

    @Autowired
    ActionCombinationItemRelationMapper actionCombinationItemRelationMapper;

    @Override
    public List<ActionCombinationItemRelationVO> findActionCombinationItemRelationByActionCombinationId(Integer actionCombinationId) {
        return actionCombinationItemRelationMapper.findActionCombinationItemRelationByActionCombinationId(actionCombinationId);
    }
}
