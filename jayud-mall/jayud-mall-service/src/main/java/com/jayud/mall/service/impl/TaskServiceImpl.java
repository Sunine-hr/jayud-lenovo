package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.TaskExecutionRuleMapper;
import com.jayud.mall.mapper.TaskMapper;
import com.jayud.mall.model.bo.QueryTaskForm;
import com.jayud.mall.model.bo.TaskForm;
import com.jayud.mall.model.bo.TaskQueryForm;
import com.jayud.mall.model.po.Task;
import com.jayud.mall.model.po.TaskExecutionRule;
import com.jayud.mall.model.vo.TaskVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ITaskExecutionRuleService;
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
    TaskExecutionRuleMapper taskExecutionRuleMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    ITaskExecutionRuleService taskExecutionRuleService;

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
        //1.保存基础任务
        this.saveOrUpdate(task);

        List<TaskExecutionRule> taskExecutionRuleList = form.getTaskExecutionRuleList();
        if(CollUtil.isNotEmpty(taskExecutionRuleList)){
            taskExecutionRuleList.forEach(taskExecutionRule -> {
                Integer fromTaskType = task.getTypes();
                Integer toTaskType = taskExecutionRule.getToTaskType();
                if(!fromTaskType.equals(toTaskType)){
                    //任务类型不同，进行判断是否为fromTaskType=1 即 提单任务
                    if(fromTaskType != 1){
                        Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务执行规则设置错误，运单任务不能关联提单任务");
                    }
                }
                taskExecutionRule.setFromTaskType(task.getTypes());
                taskExecutionRule.setFromTaskId(task.getId());
                taskExecutionRule.setFromTaskCode(task.getTaskCode());
                taskExecutionRule.setFromTaskName(task.getTaskName());

                Long toTaskId = taskExecutionRule.getToTaskId();
                if(ObjectUtil.isEmpty(toTaskId)){
                    Asserts.fail(ResultEnum.UNKNOWN_ERROR, "关联任务id不能为空");
                }
                TaskVO toTaskVO = taskMapper.findTaskById(toTaskId);
                taskExecutionRule.setToTaskType(taskExecutionRule.getToTaskType());
                taskExecutionRule.setToTaskId(taskExecutionRule.getToTaskId());
                taskExecutionRule.setToTaskCode(toTaskVO.getTaskCode());
                taskExecutionRule.setToTaskName(toTaskVO.getTaskName());
            });
        }
        //2.保存 基础任务关联的 任务执行规则
        QueryWrapper<TaskExecutionRule> qw = new QueryWrapper<>();
        qw.eq("from_task_id", task.getId());
        taskExecutionRuleService.remove(qw);
        if(CollUtil.isNotEmpty(taskExecutionRuleList)){
            taskExecutionRuleService.saveOrUpdateBatch(taskExecutionRuleList);
        }

        TaskVO taskVO = ConvertUtil.convert(task, TaskVO.class);
        return CommonResult.success(taskVO);
    }

    @Override
    public CommonResult<TaskVO> findTaskById(Long id) {
        TaskVO taskVO = taskMapper.findTaskById(id);
        if(ObjectUtil.isNull(taskVO)){
            return CommonResult.error(-1, "任务不存在");
        }
        Long fromTaskId = taskVO.getId();
        List<TaskExecutionRule> taskExecutionRuleList = taskExecutionRuleMapper.findTaskExecutionRuleByFromTaskId(fromTaskId);
        taskVO.setTaskExecutionRuleList(taskExecutionRuleList);
        return CommonResult.success(taskVO);
    }


}