package com.jayud.mall.service;

import com.jayud.mall.model.po.SystemMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.SystemMenuVO;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-24
 */
public interface ISystemMenuService extends IService<SystemMenu> {

    List<SystemMenuVO> findAllMenuVO();
}
