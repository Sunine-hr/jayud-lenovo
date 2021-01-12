package com.jayud.oauth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.oauth.model.bo.AuditSystemUserForm;
import com.jayud.oauth.model.bo.OprSystemUserForm;
import com.jayud.oauth.model.bo.QueryAccountForm;
import com.jayud.oauth.model.bo.QuerySystemUserForm;
import com.jayud.oauth.model.po.SystemUser;
import com.jayud.oauth.model.vo.*;

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
     *
     * @param name
     * @return
     */
    SystemUser selectByName(String name);


    /**
     * 用户登录逻辑
     *
     * @param token
     * @return
     */
    SystemUserVO login(UserLoginToken token);

    /**
     * 登出
     */
    void logout();


    /**
     * 获取登录后用户的角色菜单相关信息
     *
     * @return
     */
    SystemUserLoginInfoVO findLoginUserInfo();

    /**
     * 用户列表查询
     *
     * @param form
     * @return
     */
    IPage<SystemUserVO> getPageList(QuerySystemUserForm form);

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    UpdateSystemUserVO getSystemUser(Long id);

    /**
     * 账号管理-删除/新增/修改
     *
     * @param form
     */
    CommonResult oprSystemUser(OprSystemUserForm form);

    /**
     * 人员审核总经办
     *
     * @param form
     */
    void auditSystemUser(AuditSystemUserForm form);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    SystemUser getLoginUser();

    /**
     * 获取部门结构
     *
     * @return
     */
    List<QueryOrgStructureVO> findOrgStructure();

    /**
     * 根据部门ID获取部门负责人信息
     *
     * @param departmentId
     * @return
     */
    List<DepartmentChargeVO> findOrgStructureCharge(Long departmentId);

    /**
     * 更新或修改用户
     *
     * @param systemUser
     */
    void saveOrUpdateSystemUser(SystemUser systemUser);

    /**
     * 获取用户信息
     *
     * @param param
     * @return
     */
    List<SystemUser> findUserByCondition(Map<String, Object> param);

    /**
     * 一个部门只能有一个负责人
     *
     * @param departmentId
     */
    void updateIsCharge(Long departmentId);

    /**
     * 根据id集合查询所用系统用户
     */
    List<SystemUser> getByIds(List<Long> ids);


    /**
     * 分页查询各个模块中账户管理
     */
    IPage<SystemUserVO> findEachModuleAccountByPage(QueryAccountForm form);

    /**
     * 根据业务员姓名获取业务员id
     */
    SystemUser getSystemUserBySystemName(String name);

    SystemUser getLoginUser(String loginName);
}
