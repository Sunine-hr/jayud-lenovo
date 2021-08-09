package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddSystemRoleActionCheckForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemRoleActionCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.SystemRoleActionCheckVO;

/**
 * <p>
 * 角色审核级别权限 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
public interface ISystemRoleActionCheckService extends IService<SystemRoleActionCheck> {

    IPage<SystemRoleActionCheckVO> findByPage(QueryForm form);

    boolean delete(DeleteForm deleteForm);

    boolean addSystemRoleAction(AddSystemRoleActionCheckForm form);
}
