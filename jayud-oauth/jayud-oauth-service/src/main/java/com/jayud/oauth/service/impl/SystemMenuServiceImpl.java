package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.po.SystemMenu;
import com.jayud.model.vo.SystemMenuNode;
import com.jayud.model.vo.SystemMenuVO;
import com.jayud.oauth.mapper.SystemMenuMapper;
import com.jayud.oauth.service.ISystemMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Service
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements ISystemMenuService {




    @Override
    public List<SystemMenuNode> treeList() {
        return convertMenuTree(list(), 0L);
    }

    @Override
    public List<SystemMenuNode> roleTreeList(List<Long> roleIds){
        return convertMenuTree(selectByUserId(roleIds,0), 0L);
    }

    private List<SystemMenu> selectByUserId(List<Long> roleIds,Integer hidden){
        return baseMapper.selectByUserId(roleIds,hidden);
    }



    /**
     * 将SystemMenu转化为USystemMenuNode并设置children属性
     */
    private SystemMenuNode covertMenuNode(SystemMenu menu, List<SystemMenu> menuList) {

        SystemMenuNode node = ConvertUtil.convert(menu,SystemMenuNode.class);
        //设置菜单
        node.setChildren(convertMenuTree(menuList, menu.getId()));

        return node;
    }

    /**
     * 对象类型转换 ：SystemMenuDTO -> SystemMenu
     * @param menuVO
     * @return
     */
    private SystemMenu convert(SystemMenuVO menuVO){
        return ConvertUtil.convert(menuVO,SystemMenu.class);
    }

    /**
     * 对象类型转换 ：SystemMenu -> SystemMenuVO
     * @param menu
     * @return
     */
    private SystemMenuVO convert(SystemMenu menu){
        return ConvertUtil.convert(menu,SystemMenuVO.class);
    }


    /**
     * 菜单列表转树
     * @param menuList
     * @param parentId
     * @return
     */
    private List<SystemMenuNode> convertMenuTree(List<SystemMenu> menuList, long parentId) {
        return menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
    }

}
