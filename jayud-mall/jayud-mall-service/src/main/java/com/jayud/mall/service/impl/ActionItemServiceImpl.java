package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.model.bo.ActionItemForm;
import com.jayud.mall.model.bo.ActionItemQueryForm;
import com.jayud.mall.model.po.ActionItem;
import com.jayud.mall.mapper.ActionItemMapper;
import com.jayud.mall.model.vo.ActionItemVO;
import com.jayud.mall.service.IActionItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 操作项 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Service
public class ActionItemServiceImpl extends ServiceImpl<ActionItemMapper, ActionItem> implements IActionItemService {

    @Autowired
    BaseService baseService;

    @Override
    public List<ActionItemVO> findActionItem(ActionItemQueryForm form) {
        QueryWrapper<ActionItem> queryWrapper = new QueryWrapper<>();
        String itemName = form.getItemName();
        if(itemName != null && itemName != ""){
            queryWrapper.like("item_name", itemName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        String userName = form.getUserName();
        if(userName != null && userName != ""){
            queryWrapper.eq("user_name", userName);
        }
        List<ActionItem> list = this.list(queryWrapper);
        List<ActionItemVO> actionItemVOS = ConvertUtil.convertList(list, ActionItemVO.class);
        return actionItemVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<ActionItemVO> saveActionItem(ActionItemForm form) {
        ActionItem actionItem = ConvertUtil.convert(form, ActionItem.class);
        Integer id = actionItem.getId();
        String itemName = actionItem.getItemName();
        if(id == null){
            AuthUser user = baseService.getUser();
            actionItem.setStatus("1");
            actionItem.setUserId(user.getId().intValue());
            actionItem.setUserName(user.getName());
            actionItem.setCreateTime(LocalDateTime.now());
            QueryWrapper<ActionItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_name", itemName);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "操作项名称已存在");
            }
        }else{
            QueryWrapper<ActionItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_name", itemName);
            queryWrapper.ne("id", id);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "操作项名称已存在");
            }
        }
        this.saveOrUpdate(actionItem);
        ActionItemVO actionItemVO = ConvertUtil.convert(actionItem, ActionItemVO.class);
        return CommonResult.success(actionItemVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<ActionItemVO> disabledActionItem(Integer id) {
        ActionItem actionItem = this.getById(id);
        actionItem.setStatus("0");
        this.saveOrUpdate(actionItem);
        ActionItemVO actionItemVO = ConvertUtil.convert(actionItem, ActionItemVO.class);
        return CommonResult.success(actionItemVO);
    }
}
