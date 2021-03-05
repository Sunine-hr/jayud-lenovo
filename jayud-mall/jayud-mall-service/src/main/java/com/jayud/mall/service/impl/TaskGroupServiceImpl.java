package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.TaskGroupMapper;
import com.jayud.mall.model.bo.QueryTaskGroupForm;
import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.bo.TaskGroupQueryForm;
import com.jayud.mall.model.po.BillTask;
import com.jayud.mall.model.po.TaskGroup;
import com.jayud.mall.model.po.WaybillTask;
import com.jayud.mall.model.vo.TaskGroupVO;
import com.jayud.mall.model.vo.TaskItemVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IBillTaskService;
import com.jayud.mall.service.ITaskGroupService;
import com.jayud.mall.service.IWaybillTaskService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    BaseService baseService;
    @Autowired
    IBillTaskService billTaskService;
    @Autowired
    IWaybillTaskService waybillTaskService;

    @Override
    public List<TaskGroupVO> findTaskGroup(TaskGroupQueryForm form) {
        QueryWrapper<TaskGroup> queryWrapper = new QueryWrapper<>();
        String groupCode = form.getGroupCode();
        if(!StrUtil.isBlank(groupCode)){
            queryWrapper.eq("group_code", groupCode);
        }
        String groupName = form.getGroupName();
        if(!StrUtil.isBlank(groupName)){
            queryWrapper.like("group_name", groupName);
        }
        String status = form.getStatus();
        if(!StrUtil.isBlank(status)){
            queryWrapper.eq("status", status);
        }
        Integer types = form.getTypes();
        if(types != null){
            queryWrapper.eq("types", types);
        }
        List<TaskGroup> list = taskGroupMapper.selectList(queryWrapper);
        List<TaskGroupVO> taskGroupVOS = ConvertUtil.convertList(list, TaskGroupVO.class);
        return taskGroupVOS;
    }

    @Override
    public List<TaskGroupVO> findTaskGroupByTd() {
        QueryWrapper<TaskGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("types", 1);//类型(1提单任务分组 2运单任务分组)
        List<TaskGroup> list = taskGroupMapper.selectList(queryWrapper);
        List<TaskGroupVO> taskGroupVOS = ConvertUtil.convertList(list, TaskGroupVO.class);
        return taskGroupVOS;
    }

    @Override
    public List<TaskGroupVO> findTaskGroupByYd() {
        QueryWrapper<TaskGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("types", 2);//类型(1提单任务分组 2运单任务分组)
        List<TaskGroup> taskGroups = taskGroupMapper.selectList(queryWrapper);
        List<TaskGroupVO> taskGroupVOS = ConvertUtil.convertList(taskGroups, TaskGroupVO.class);
        return taskGroupVOS;
    }

    @Override
    public IPage<TaskGroupVO> findTaskGroupByPage(QueryTaskGroupForm form) {
        //定义分页参数
        Page<TaskGroupVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("t.id"));
        IPage<TaskGroupVO> pageInfo = taskGroupMapper.findTaskGroupByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<TaskGroupVO> saveTaskGroup(TaskGroupForm form) {
        TaskGroup taskGroup = ConvertUtil.convert(form, TaskGroup.class);
        AuthUser user = baseService.getUser();
        Long id = taskGroup.getId();
        String groupName = form.getGroupName();
        if(ObjectUtil.isEmpty(id)){
            //id为空，代表新增
            QueryWrapper<TaskGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("group_name", groupName);
            List<TaskGroup> list = this.list(queryWrapper);
            if(!ObjectUtil.isEmpty(list)){
                return CommonResult.error(-1, "["+groupName+"],任务组名称已存在");
            }
            String groupCode = NumberGeneratedUtils.getOrderNoByCode2("task_group_code");
            taskGroup.setGroupCode(groupCode);
            taskGroup.setUserId(user.getId().intValue());
            taskGroup.setUserName(user.getName());
        }else{
            QueryWrapper<TaskGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("id", id);
            queryWrapper.eq("group_name", groupName);
            List<TaskGroup> list = this.list(queryWrapper);
            if(!ObjectUtil.isEmpty(list)){
                return CommonResult.error(-1, "["+groupName+"],任务组名称已存在");
            }
        }
        //1.保存任务组
        this.saveOrUpdate(taskGroup);
        Long taskGroupId = taskGroup.getId();//分组id
        Integer types = taskGroup.getTypes();//类型(1提单任务分组 2运单任务分组)
        String groupCode = taskGroup.getGroupCode();//分组代码
        List<TaskItemVO> taskItemVOS = form.getTaskItemVOS();
        //2.保存任务组关联的任务项
        if(types == 1){
            //提单任务列表：bill_task
            List<BillTask> billTasks = ConvertUtil.convertList(taskItemVOS, BillTask.class);
            billTasks.forEach(billTask -> {
                billTask.setGroupCode(groupCode);
                billTask.setUserId(user.getId().intValue());
                billTask.setUserName(user.getName());
            });
            QueryWrapper<BillTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("group_code", groupCode);
            billTaskService.remove(queryWrapper);
            billTaskService.saveOrUpdateBatch(billTasks);
        }else if(types == 2){
            //运单任务列表：waybill_task
            List<WaybillTask> waybillTasks = ConvertUtil.convertList(taskItemVOS, WaybillTask.class);
            waybillTasks.forEach(waybillTask -> {
                waybillTask.setGroupCode(groupCode);
                waybillTask.setUserId(user.getId().intValue());
                waybillTask.setUserName(user.getName());
            });
            QueryWrapper<WaybillTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("group_code", groupCode);
            waybillTaskService.remove(queryWrapper);
            waybillTaskService.saveOrUpdateBatch(waybillTasks);
        }
        TaskGroupVO taskGroupVO = ConvertUtil.convert(taskGroup, TaskGroupVO.class);
        return CommonResult.success(taskGroupVO);
    }

    @Override
    public CommonResult<TaskGroupVO> findTaskGroupById(Long id) {
        TaskGroupVO taskGroupVO = taskGroupMapper.findTaskGroupById(id);
        if(ObjectUtil.isEmpty(taskGroupVO)){
            return CommonResult.error(-1, "任务组不存在");
        }
        Integer types = taskGroupVO.getTypes();//类型(1提单任务分组 2运单任务分组)
        String groupCode = taskGroupVO.getGroupCode();
        if(types == 1){
            //提单任务列表：bill_task
            List<TaskItemVO> taskItemVOS = taskGroupMapper.findBillTaskByGroupCode(groupCode);
            taskGroupVO.setTaskItemVOS(taskItemVOS);
        }else if(types == 2){
            //运单任务列表：waybill_task
            List<TaskItemVO> taskItemVOS = taskGroupMapper.findWaybillTaskByGroupCode(groupCode);
            taskGroupVO.setTaskItemVOS(taskItemVOS);
        }
        return CommonResult.success(taskGroupVO);
    }
}
