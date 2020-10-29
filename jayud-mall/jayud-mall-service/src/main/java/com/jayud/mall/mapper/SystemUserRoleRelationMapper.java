package com.jayud.mall.mapper;

import com.jayud.mall.model.po.SystemUserRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * 根据userIds，删除用户关联的角色信息
     * @param userIds
     */
    void removeUserRoleRelation(List<Long> userIds);
}
