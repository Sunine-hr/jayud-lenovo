package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OperationTeamMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

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

}
