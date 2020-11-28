package com.jayud.mall.service.impl;

import com.jayud.mall.model.bo.TaskMemberRelationForm;
import com.jayud.mall.model.po.TaskMemberRelation;
import com.jayud.mall.mapper.TaskMemberRelationMapper;
import com.jayud.mall.model.vo.TaskMemberRelationVO;
import com.jayud.mall.service.ITaskMemberRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务成员关系表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Service
public class TaskMemberRelationServiceImpl extends ServiceImpl<TaskMemberRelationMapper, TaskMemberRelation> implements ITaskMemberRelationService {

    @Autowired
    TaskMemberRelationMapper taskMemberRelationMapper;

    @Override
    public List<TaskMemberRelationVO> findTaskMemberRelation(TaskMemberRelationForm form) {
        List<TaskMemberRelationVO> taskMemberRelationVOS = taskMemberRelationMapper.findTaskMemberRelation(form);
        return taskMemberRelationVOS;
    }
}
