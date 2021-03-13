package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryTaskForm;
import com.jayud.mall.model.po.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.TaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 基础任务表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-03
 */
@Mapper
@Component
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 基础任务分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<TaskVO> findTaskByPage(Page<TaskVO> page, @Param("form") QueryTaskForm form);

    /**
     * 根据id，查询任务
     * @param id
     * @return
     */
    TaskVO findTaskById(@Param("id") Long id);

    /**
     * 根据任务代码，查询任务
     * @param taskCode
     * @return
     */
    TaskVO findTaskByTaskCode(@Param("taskCode") String taskCode);
}
