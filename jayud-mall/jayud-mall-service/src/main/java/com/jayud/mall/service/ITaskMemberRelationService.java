package com.jayud.mall.service;

import com.jayud.mall.model.bo.TaskMemberRelationForm;
import com.jayud.mall.model.po.TaskMemberRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.TaskMemberRelationVO;

import java.util.List;

/**
 * <p>
 * 任务成员关系表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
public interface ITaskMemberRelationService extends IService<TaskMemberRelation> {

    /**
     * 查看-任务成员关系表list
     * @param form
     * @return
     */
    List<TaskMemberRelationVO> findTaskMemberRelation(TaskMemberRelationForm form);
}
