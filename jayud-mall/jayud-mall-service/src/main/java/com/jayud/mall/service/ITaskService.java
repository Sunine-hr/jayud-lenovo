package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTaskForm;
import com.jayud.mall.model.bo.TaskForm;
import com.jayud.mall.model.bo.TaskQueryForm;
import com.jayud.mall.model.po.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.TaskVO;

import java.util.List;

/**
 * <p>
 * 基础任务表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-03
 */
public interface ITaskService extends IService<Task> {

    /**
     * 基础任务分页查询
     * @param form
     * @return
     */
    IPage<TaskVO> findTaskByPage(QueryTaskForm form);

    /**
     * 查询基础任务list
     * @param form
     * @return
     */
    List<TaskVO> findTask(TaskQueryForm form);

    /**
     * 保存基础任务
     * @param form
     * @return
     */
    CommonResult<TaskVO> saveTask(TaskForm form);

    /**
     * 根据id，查询基础任务
     * @param id
     * @return
     */
    CommonResult<TaskVO> findTaskById(Long id);

    /**
     * 删除任务
      * @param id
     */
    void delTask(Long id);
}
