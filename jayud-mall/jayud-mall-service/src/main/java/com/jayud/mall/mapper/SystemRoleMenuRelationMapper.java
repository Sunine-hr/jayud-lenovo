package com.jayud.mall.mapper;

import com.jayud.mall.model.po.SystemRoleMenuRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Mapper
@Component
public interface SystemRoleMenuRelationMapper extends BaseMapper<SystemRoleMenuRelation> {

    /**
     * 移除角色菜单关联信息
     * @param roleIds
     */
    void removeRoleMenuRelation(List<Long> roleIds);
}
