package com.jayud.oauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oauth.model.po.SystemMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-03
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    /**
     * 根据用户ID有权限的菜单
     *
     * @param roleIds 用户ID
     * @param hidden  是否隐藏
     * @return
     */
    List<SystemMenu> selectByUserId(@Param("roleIds") List<Long> roleIds,
                                    @Param("hidden") Integer hidden,
                                    @Param("type") String type);
}
