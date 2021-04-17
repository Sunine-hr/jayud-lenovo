package com.jayud.mall.service;

import com.jayud.mall.model.bo.OperationTeamMemberForm;
import com.jayud.mall.model.po.OperationTeamMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OperationTeamMemberVO;

import java.util.List;

/**
 * <p>
 * 运营(服务)小组成员 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
public interface IOperationTeamMemberService extends IService<OperationTeamMember> {

    /**
     * 查询-运营(服务)小组成员
     * @param form
     * @return
     */
    List<OperationTeamMemberVO> findOperationTeamMember(OperationTeamMemberForm form);
}
