package com.jayud.mall.service;

import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.po.TaskGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.TaskGroupVO;

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
     * 查询任务分组List
     * @param form
     * @return
     */
    List<TaskGroupVO> findTaskGroup(TaskGroupForm form);

    /**
     * 查询提单任务分组List
     * @return
     */
    List<TaskGroupVO> findTaskGroupByTd();

    /**
     * 查询运单任务分组List
     * @return
     */
    List<TaskGroupVO> findTaskGroupByYd();
}
