package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.TaskMapper;
import com.jayud.mall.model.bo.QueryTaskForm;
import com.jayud.mall.model.bo.TaskForm;
import com.jayud.mall.model.bo.TaskQueryForm;
import com.jayud.mall.model.po.Task;
import com.jayud.mall.model.vo.TaskVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ITaskService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 基础任务表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-03
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    BaseService baseService;

    @Override
    public IPage<TaskVO> findTaskByPage(QueryTaskForm form) {
        //定义分页参数
        Page<TaskVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<TaskVO> pageInfo = taskMapper.findTaskByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<TaskVO> findTask(TaskQueryForm form) {
        List<TaskVO> taskVOS = taskMapper.findTask(form);
        return taskVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<TaskVO> saveTask(TaskForm form) {
        Task task = ConvertUtil.convert(form, Task.class);
        Long id = task.getId();
        String taskName = task.getTaskName();
        if(id == null){
            //新增
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_name", taskName);
            List<Task> list = this.list(queryWrapper);
            if(CollectionUtil.isNotEmpty(list)){
                return CommonResult.error(-1, "任务名称["+taskName+"],已存在");
            }
            AuthUser user = baseService.getUser();
            String taskCode = NumberGeneratedUtils.getOrderNoByCode2("task_code");
            task.setTaskCode(taskCode);
            Long userId = user.getId();
            task.setUserId(Math.toIntExact(userId));
            task.setUserName(user.getName());
        }else{
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("id", id);
            queryWrapper.eq("task_name", taskName);
            List<Task> list = this.list(queryWrapper);
            if(CollectionUtil.isNotEmpty(list)){
                return CommonResult.error(-1, "任务名称["+taskName+"],已存在");
            }
        }
        this.saveOrUpdate(task);
        TaskVO taskVO = ConvertUtil.convert(task, TaskVO.class);
        return CommonResult.success(taskVO);
    }

    @Override
    public CommonResult<TaskVO> findTaskById(Long id) {
        TaskVO taskVO = taskMapper.findTaskById(id);
        if(ObjectUtil.isNull(taskVO)){
            return CommonResult.error(-1, "任务不存在");
        }
        return CommonResult.success(taskVO);
    }


}