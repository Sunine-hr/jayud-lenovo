package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.SaveSystemUserForm;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.vo.SystemUserVO;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-23
 */
public interface ISystemUserService extends IService<SystemUser> {

    /**
     * 用户登录
     * @param loginForm
     * @return
     */
    SystemUserVO login(SystemUserLoginForm loginForm);

    /**
     * 查询用户list
     * @return
     */
    List<SystemUserVO> getUserList();

    /**
     * 新增用户
     * @param form
     */
    CommonResult<SystemUserVO> insertUser(SaveSystemUserForm form);

    /**
     * 修改用户
     * @param form
     */
    CommonResult<SystemUserVO> updateUser(SaveSystemUserForm form);

    /**
     * 删除用户
     * @param userId
     */
    CommonResult deleteUser(Long userId);

    /**
     * 通过id获取用户
     * @param id
     * @return
     */
    SystemUserVO getUser(Long id);

    /**
     * <p>根据用户Id，禁用用户</p>
     * <p>帐号启用状态：0->Off；1->On</p>
     * @param id
     */
    CommonResult disableUser(Long id);

    /**
     * 启用用户
     * @param id
     */
    CommonResult enableUser(Long id);

    /**
     * 重置用户密码
     * @param id
     */
    CommonResult resetPassword(Long id);

    /**
     * 查询用户分页
     * @param form
     * @return
     */
    IPage<SystemUserVO> findUserByPage(QueryUserForm form);
}
