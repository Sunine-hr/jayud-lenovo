package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.bo.SaveRoleForm;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.vo.SystemRoleVO;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
public interface ISystemRoleService extends IService<SystemRole> {

    /**
     * 保存角色（新增/修改）
     * @param from
     */
    void saveRole(SaveRoleForm from);

    /**
     * 删除角色
     * @param id
     */
    void deleteRole(Long id);

    /**
     * 根据id，获取角色信息
     * @param id
     * @return
     */
    SystemRoleVO getRole(Long id);

    /**
     * 查询角色分页
     * @param form
     * @return
     */
    IPage<SystemRoleVO> findRoleByPage(QueryRoleForm form);
}
