package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.SystemMenu;
import com.jayud.model.vo.SystemMenuNode;

import java.util.List;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
public interface ISystemMenuService extends IService<SystemMenu> {


    /**
     * 菜单树查询
     * @return
     */
    List<SystemMenuNode> treeList();

    List<SystemMenuNode> roleTreeList(List<Long> roleIds);

}
