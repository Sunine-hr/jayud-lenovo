package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.po.TaskGroup;
import com.jayud.mall.mapper.TaskGroupMapper;
import com.jayud.mall.service.ITaskGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提单任务分组 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class TaskGroupServiceImpl extends ServiceImpl<TaskGroupMapper, TaskGroup> implements ITaskGroupService {

    @Autowired
    TaskGroupMapper taskGroupMapper;

    @Override
    public List<TaskGroup> findTaskGroup(TaskGroupForm form) {
        QueryWrapper<TaskGroup> queryWrapper = new QueryWrapper<>();
        String idCode = form.getIdCode();
        if(idCode != null && idCode != ""){
            queryWrapper.eq("id_code", idCode);
        }
        String codeName = form.getCodeName();
        if(codeName != null && codeName != ""){
            queryWrapper.like("code_name", codeName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        Integer types = form.getTypes();
        if(types != null){
            queryWrapper.eq("types", types);
        }
        List<TaskGroup> list = taskGroupMapper.selectList(queryWrapper);
        return list;
    }
}
