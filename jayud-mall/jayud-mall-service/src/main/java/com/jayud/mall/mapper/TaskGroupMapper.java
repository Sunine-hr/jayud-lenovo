package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryTaskGroupForm;
import com.jayud.mall.model.po.TaskGroup;
import com.jayud.mall.model.vo.TaskGroupVO;
import com.jayud.mall.model.vo.TaskItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单任务分组 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface TaskGroupMapper extends BaseMapper<TaskGroup> {

    /**
     * 分页查询任务分组
     * @param page
     * @param form
     * @return
     */
    IPage<TaskGroupVO> findTaskGroupByPage(Page<TaskGroupVO> page, @Param("form") QueryTaskGroupForm form);

    /**
     * 根据id，获取任务组以及任务组关联的任务项
     * @param id
     * @return
     */
    TaskGroupVO findTaskGroupById(@Param("id") Long id);

    /**
     * 根据分组代码，查询提单任务列表
     * @param groupCode
     * @return
     */
    List<TaskItemVO> findBillTaskByGroupCode(@Param("groupCode") String groupCode);

    /**
     * 根据分组代码，查询运单任务列表
     * @param groupCode
     * @return
     */
    List<TaskItemVO> findWaybillTaskByGroupCode(@Param("groupCode") String groupCode);
}
