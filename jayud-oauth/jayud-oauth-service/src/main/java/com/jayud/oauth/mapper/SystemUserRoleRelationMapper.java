package com.jayud.oauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.model.po.SystemRole;
import com.jayud.model.po.SystemUserRoleRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Mapper
public interface SystemUserRoleRelationMapper extends BaseMapper<SystemUserRoleRelation> {

    List<SystemRole> getEnabledRolesByUserId(Long id);

    boolean removeRelationByRoleId(List<Long> roleIds);
}
