package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.SystemMenu;
import com.jayud.mall.model.vo.SystemMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-24
 */
@Mapper
@Component
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    /**
     * 查询所有菜单
     * @return
     */
    List<SystemMenuVO> findAllMenuVO();

    /**
     * 根据用户id，查询用户菜单
     * @param userId
     * @return
     */
    List<SystemMenuVO> findSystemMenuByUserId(@Param("userId") Long userId);
}
