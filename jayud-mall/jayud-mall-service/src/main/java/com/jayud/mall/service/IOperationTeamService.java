package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.OperationTeamForm;
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
}
