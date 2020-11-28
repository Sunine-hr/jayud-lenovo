package com.jayud.mall.mapper;

import com.jayud.mall.model.bo.OperationTeamMemberForm;
import com.jayud.mall.model.po.OperationTeamMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OperationTeamMemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 运营(服务)小组成员 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Mapper
@Component
public interface OperationTeamMemberMapper extends BaseMapper<OperationTeamMember> {

    /**
     * 查询-运营(服务)小组成员
     * @param form
     * @return
     */
    List<OperationTeamMemberVO> findOperationTeamMember(@Param(value = "form") OperationTeamMemberForm form);
}
