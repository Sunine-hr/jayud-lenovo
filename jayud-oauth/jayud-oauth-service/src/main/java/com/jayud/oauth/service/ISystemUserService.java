package com.jayud.oauth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.bo.AuditSystemUserForm;
import com.jayud.model.bo.OprSystemUserForm;
import com.jayud.model.bo.QuerySystemUserForm;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
public interface ISystemUserService extends IService<SystemUser> {


    /**
     * 判断该用户是否存在
     * @param name
     * @return
     */
    SystemUser selectByName(String name);


    /**
     * 用户登录逻辑
     * @param token
     * @return
     */
    SystemUserVO login(UserLoginToken token);

    /**
     * 登出
     */
    void logout();


    /**
     *获取登录后用户的角色菜单相关信息
     * @return
     */
    SystemUserLoginInfoVO findLoginUserInfo();

    /**
     * 用户列表查询
     * @param form
     * @return
     */
    IPage<SystemUserVO> getPageList(QuerySystemUserForm form);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    UpdateSystemUserVO getSystemUser(Long id);

    /**
     * 账号管理-删除/新增/修改
     * @param form
     */
    void oprSystemUser(OprSystemUserForm form);

    /**
     * 人员审核总经办
     * @param form
     */
    void auditSystemUser(AuditSystemUserForm form);

    /**
     * 获取当前登录用户
     * @return
     */
    SystemUser getLoginUser();

    /**
     * 获取部门结构
     * @return
     */
    List<QueryOrgStructureVO> findOrgStructure();

    /**
     * 根据部门ID获取部门负责人信息
     * @param departmentId
     * @return
     */
    List<DepartmentChargeVO>  findOrgStructureCharge(Long departmentId);

    /**
     * 更新或修改用户
     * @param systemUser
     */
    void saveOrUpdateSystemUser(SystemUser systemUser);

    /**
     * 获取用户信息
     * @param param
     * @return
     */
    List<SystemUser> findUserByCondition(Map<String,Object> param);

}
