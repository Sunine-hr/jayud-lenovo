package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.vo.SystemMenuNode;
import com.jayud.oauth.model.po.SystemMenu;
import com.jayud.oauth.model.vo.QueryMenuStructureVO;

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
     * 获取该角色下的所有菜单
     * @param roleIds
     * @return
     */
    List<SystemMenuNode> roleTreeList(List<Long> roleIds);

    /**
     * 获取所有可显示的菜单
     * @return
     */
    List<QueryMenuStructureVO> findAllMenuNode();



}
