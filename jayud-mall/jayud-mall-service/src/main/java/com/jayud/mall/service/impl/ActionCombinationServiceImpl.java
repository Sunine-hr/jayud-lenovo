package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.ActionCombinationItemRelationMapper;
import com.jayud.mall.mapper.ActionCombinationMapper;
import com.jayud.mall.model.bo.ActionCombinationForm;
import com.jayud.mall.model.bo.ActionCombinationQueryForm;
import com.jayud.mall.model.po.ActionCombination;
import com.jayud.mall.model.po.ActionCombinationItemRelation;
import com.jayud.mall.model.vo.ActionCombinationVO;
import com.jayud.mall.service.IActionCombinationItemRelationService;
import com.jayud.mall.service.IActionCombinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 操作项组合 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Service
public class ActionCombinationServiceImpl extends ServiceImpl<ActionCombinationMapper, ActionCombination> implements IActionCombinationService {

    @Autowired
    ActionCombinationMapper actionCombinationMapper;
    @Autowired
    ActionCombinationItemRelationMapper actionCombinationItemRelationMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    IActionCombinationItemRelationService actionCombinationItemRelationService;

    @Override
    public List<ActionCombinationVO> findActionCombination(ActionCombinationQueryForm form) {
        QueryWrapper<ActionCombination> queryWrapper = new QueryWrapper<>();
        String combinationName = form.getCombinationName();
        if(combinationName != null && combinationName != ""){
            queryWrapper.like("combination_name", combinationName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        String userName = form.getUserName();
        if(userName != null && userName != ""){
            queryWrapper.like("user_name", userName);
        }
        List<ActionCombination> list = this.list(queryWrapper);
        List<ActionCombinationVO> actionCombinationVOS = ConvertUtil.convertList(list, ActionCombinationVO.class);
        return actionCombinationVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<ActionCombinationVO> saveActionCombination(ActionCombinationForm form) {
        ActionCombination actionCombination = ConvertUtil.convert(form, ActionCombination.class);
        Integer id = actionCombination.getId();
        String combinationName = actionCombination.getCombinationName();
        if(id == null){
            AuthUser user = baseService.getUser();
            actionCombination.setStatus("1");
            actionCombination.setUserId(user.getId().intValue());
            actionCombination.setUserName(user.getName());
            actionCombination.setCreateTime(LocalDateTime.now());
            QueryWrapper<ActionCombination> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("combination_name", combinationName);
            Integer count = actionCombinationMapper.selectCount(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "操作项组合名称已存在");
            }
        }else{
            QueryWrapper<ActionCombination> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("combination_name", combinationName);
            queryWrapper.ne("id", id);
            Integer count = actionCombinationMapper.selectCount(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "操作项组合名称已存在");
            }
        }
        //1.保存操作项组合
        this.saveOrUpdate(actionCombination);
        Integer actionCombinationId = actionCombination.getId();

        QueryWrapper<ActionCombinationItemRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("action_combination_id", actionCombinationId);
        //2.删除操作项组合 和 操作项关联关系
        actionCombinationItemRelationService.remove(queryWrapper);

        List<ActionCombinationItemRelation> actionCombinationItemRelationList =
                form.getActionCombinationItemRelationList();
        actionCombinationItemRelationList.forEach(actionCombinationItemRelation -> {
            actionCombinationItemRelation.setActionCombinationId(actionCombinationId);
        });
        //3.保存操作项组合 和 操作项关联关系
        actionCombinationItemRelationService.saveOrUpdateBatch(actionCombinationItemRelationList);
        ActionCombinationVO actionCombinationVO = ConvertUtil.convert(actionCombination, ActionCombinationVO.class);
        return CommonResult.success(actionCombinationVO);
    }

    @Override
    public CommonResult<ActionCombinationVO> findActionCombinationById(Integer id) {
        ActionCombination actionCombination = this.getById(id);
        ActionCombinationVO actionCombinationVO =
                ConvertUtil.convert(actionCombination, ActionCombinationVO.class);
        Integer actionCombinationId = actionCombination.getId();
        QueryWrapper<ActionCombinationItemRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("action_combination_id", actionCombinationId);
        List<ActionCombinationItemRelation> list = actionCombinationItemRelationService.list(queryWrapper);
        actionCombinationVO.setActionCombinationItemRelationList(list);
        return CommonResult.success(actionCombinationVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<ActionCombinationVO> disabledActionCombination(Integer id) {
        ActionCombination actionCombination = this.getById(id);
        actionCombination.setStatus("0");//状态(0无效 1有效)
        this.saveOrUpdate(actionCombination);
        ActionCombinationVO actionCombinationVO = ConvertUtil.convert(actionCombination, ActionCombinationVO.class);
        return CommonResult.success(actionCombinationVO);
    }



}
