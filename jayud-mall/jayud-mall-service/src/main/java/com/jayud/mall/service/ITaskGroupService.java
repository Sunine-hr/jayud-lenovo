package com.jayud.mall.service;

import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.po.TaskGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 提单任务分组 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ITaskGroupService extends IService<TaskGroup> {

    /**
     * 查询提单任务分组list
     * @param form
     * @return
     */
    List<TaskGroup> findTaskGroup(TaskGroupForm form);
}
