package com.jayud.mall.mapper;

import com.jayud.mall.model.po.SystemUserRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户对应角色 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Mapper
@Component
public interface SystemUserRoleRelationMapper extends BaseMapper<SystemUserRoleRelation> {

}
