package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemRole;
import com.jayud.scm.model.po.SystemRoleAction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.SystemRoleActionVO;

import java.util.List;

/**
 * <p>
 * 角色权限设置（按钮） 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
public interface ISystemRoleActionService extends IService<SystemRoleAction> {

    SystemRoleAction getSystemRoleActionByRoleIdAndActionCode(Long id, String actionCode);

    IPage<SystemRoleActionVO> findByPage(QueryForm form);

    List<SystemRoleAction> findSystemRoleActionByRoleId(int parseInt);

    boolean removeSystemRoleActionByRoleId(List<Long> roleIds);

    void createSystemRoleAction(SystemRole systemRole, List<String> menuIds);

    List<SystemRoleAction> getSystemRoleActionByRoleIdsAndActionCode(List<Long> longs, String actionCode);
}
