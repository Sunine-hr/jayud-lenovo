package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OperationTeamForm;
import com.jayud.mall.model.bo.QueryOperationTeamForm;
import com.jayud.mall.model.po.OperationTeam;
import com.jayud.mall.model.vo.OperationTeamVO;

import java.util.List;

/**
 * <p>
 * 运营(服务)小组 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
public interface IOperationTeamService extends IService<OperationTeam> {

    /**
     * 查询-运营(服务)小组list
     * @param form
     * @return
     */
    List<OperationTeamVO> findOperationTeam(OperationTeamForm form);

    /**
     * 分页查询运营服务小组
     * @param form
     * @return
     */
    IPage<OperationTeamVO> findOperationTeamByPage(QueryOperationTeamForm form);

    /**
     * 保存运营服务小组以及关联的小组人员
     * @param form
     * @return
     */
    CommonResult<OperationTeamVO> saveOperationTeam(OperationTeamForm form);

    /**
     * 根据id，获取任务组以及任务组关联的任务项
     * @param id
     * @return
     */
    CommonResult<OperationTeamVO> findOperationTeamById(Long id);
}
