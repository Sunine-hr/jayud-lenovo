package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OperationTeamMapper;
import com.jayud.mall.model.bo.OperationTeamForm;
import com.jayud.mall.model.po.OperationTeam;
import com.jayud.mall.model.vo.OperationTeamVO;
import com.jayud.mall.service.IOperationTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 运营(服务)小组 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Service
public class OperationTeamServiceImpl extends ServiceImpl<OperationTeamMapper, OperationTeam> implements IOperationTeamService {

    @Autowired
    OperationTeamMapper operationTeamMapper;

    @Override
    public List<OperationTeamVO> findOperationTeam(OperationTeamForm form) {
        Long id = form.getId();
        QueryWrapper<OperationTeam> queryWrapper = new QueryWrapper<>();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        String groupCode = form.getGroupCode();
        if(groupCode != null && groupCode != ""){
            queryWrapper.like("group_code", groupCode);
        }
        String groupName = form.getGroupName();
        if(groupName != null && groupName != ""){
            queryWrapper.like("group_name", groupName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<OperationTeam> operationTeams = operationTeamMapper.selectList(queryWrapper);
        List<OperationTeamVO> operationTeamVOS = ConvertUtil.convertList(operationTeams, OperationTeamVO.class);
        return operationTeamVOS;
    }
}
