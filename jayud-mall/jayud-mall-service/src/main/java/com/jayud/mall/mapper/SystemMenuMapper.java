package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.SystemMenu;
import com.jayud.mall.model.vo.SystemMenuVO;
import org.apache.ibatis.annotations.Mapper;
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

    List<SystemMenuVO> findAllMenuVO();
}
