package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.TaskExecutionRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 任务执行规则表(from where to where) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-13
 */
@Mapper
@Component
public interface TaskExecutionRuleMapper extends BaseMapper<TaskExecutionRule> {

    /**
     * 根据fromTaskId，查询 任务执行规则 list
     * @param fromTaskId
     * @return
     */
    List<TaskExecutionRule> findTaskExecutionRuleByFromTaskId(@Param("fromTaskId") Long fromTaskId);
}
