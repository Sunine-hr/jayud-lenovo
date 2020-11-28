package com.jayud.mall.mapper;

import com.jayud.mall.model.bo.TaskMemberRelationForm;
import com.jayud.mall.model.po.TaskMemberRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.TaskMemberRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 任务成员关系表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Mapper
@Component
public interface TaskMemberRelationMapper extends BaseMapper<TaskMemberRelation> {

    /**
     * 查看-任务成员关系表list
     * @param form
     * @return
     */
    List<TaskMemberRelationVO> findTaskMemberRelation(@Param(value = "form") TaskMemberRelationForm form);
}
