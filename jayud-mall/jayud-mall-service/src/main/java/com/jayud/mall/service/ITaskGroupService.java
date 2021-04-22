package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTaskGroupForm;
import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.bo.TaskGroupQueryForm;
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
    List<TaskGroupVO> findTaskGroup(TaskGroupQueryForm form);

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

    /**
     * 分页查询任务分组
     * @param form
     * @return
     */
    IPage<TaskGroupVO> findTaskGroupByPage(QueryTaskGroupForm form);

    /**
     * 保存任务组以及任务组关联的任务项
     * @param form
     * @return
     */
    CommonResult<TaskGroupVO> saveTaskGroup(TaskGroupForm form);

    /**
     * 根据id，获取任务组以及任务组关联的任务项
     * @param id
     * @return
     */
    CommonResult<TaskGroupVO> findTaskGroupById(Long id);
}
