package com.jayud.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.AddSysRoleActionDataForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.po.SysRoleActionData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.vo.SysRoleActionDataVO;

import java.util.List;

/**
 * <p>
 * 角色数据权限 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
public interface ISysRoleActionDataService extends IService<SysRoleActionData> {

    IPage<SysRoleActionDataVO> findByPage(QueryForm form);

    boolean addSystemRoleActionData(AddSysRoleActionDataForm form);

    List<Integer> getRoleData(String actionCode);
}
