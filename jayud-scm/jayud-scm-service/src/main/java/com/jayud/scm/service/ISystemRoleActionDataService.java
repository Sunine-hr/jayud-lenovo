package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddSystemRoleActionDataForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemRoleActionData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.SystemRoleActionDataVO;

/**
 * <p>
 * 角色数据权限 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
public interface ISystemRoleActionDataService extends IService<SystemRoleActionData> {

    IPage<SystemRoleActionDataVO> findByPage(QueryForm form);

    boolean addSystemRoleActionData(AddSystemRoleActionDataForm form);
}
