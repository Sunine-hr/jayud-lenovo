package com.jayud.mall.service.impl;

import com.jayud.mall.model.bo.OperationTeamMemberForm;
import com.jayud.mall.model.po.OperationTeamMember;
import com.jayud.mall.mapper.OperationTeamMemberMapper;
import com.jayud.mall.model.vo.OperationTeamMemberVO;
import com.jayud.mall.service.IOperationTeamMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 运营(服务)小组成员 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Service
public class OperationTeamMemberServiceImpl extends ServiceImpl<OperationTeamMemberMapper, OperationTeamMember> implements IOperationTeamMemberService {

    @Autowired
    OperationTeamMemberMapper operationTeamMemberMapper;

    @Override
    public List<OperationTeamMemberVO> findOperationTeamMember(OperationTeamMemberForm form) {
        List<OperationTeamMemberVO> operationTeamMemberVOS = operationTeamMemberMapper.findOperationTeamMember(form);
        return operationTeamMemberVOS;
    }
}
