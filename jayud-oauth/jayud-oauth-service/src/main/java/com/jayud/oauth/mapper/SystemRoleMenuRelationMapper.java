package com.jayud.oauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oauth.model.po.SystemRoleMenuRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Mapper
public interface SystemRoleMenuRelationMapper extends BaseMapper<SystemRoleMenuRelation> {

    /**
     * 批量删除
     * @param roleIds
     * @return
     */
    boolean removeRelationByRoleId(List<Long> roleIds);
}
