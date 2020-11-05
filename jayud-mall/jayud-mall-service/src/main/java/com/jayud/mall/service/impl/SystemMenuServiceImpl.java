package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.SystemMenuMapper;
import com.jayud.mall.model.po.SystemMenu;
import com.jayud.mall.model.vo.SystemMenuVO;
import com.jayud.mall.service.ISystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-24
 */
@Service
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements ISystemMenuService {

    @Autowired
    SystemMenuMapper menuMapper;

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<SystemMenuVO> findAllMenuVO() {

        List<SystemMenuVO> menuVOList = menuMapper.findAllMenuVO();

        //0L 代表一级菜单
        List<SystemMenuVO> systemMenuVOList = convertMenuTree(menuVOList, 0L);
        return systemMenuVOList;
    }

    /**
     * 转换为菜单树
     * @param menuVOList
     * @return
     */
    private List<SystemMenuVO> convertMenuTree(List<SystemMenuVO> menuVOList, long parentId) {

        List<SystemMenuVO> collect = menuVOList.stream()
                .filter(menuvo -> Long.valueOf(menuvo.getParentId()).equals(parentId))
                .map(menuvo -> convertMenuNode(menuvo, menuVOList)).collect(Collectors.toList());
        return collect;

    }

    /**
     * 转换为菜单节点
     * @param menuvo
     * @param menuList
     * @return
     */
    private SystemMenuVO convertMenuNode(SystemMenuVO menuvo, List<SystemMenuVO> menuList) {
        //设置菜单
        menuvo.setChildren(convertMenuTree(menuList, menuvo.getId()));
        return menuvo;
    }

}
