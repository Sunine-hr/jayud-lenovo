package com.jayud.oauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.SystemUserVO;
import com.jayud.model.vo.UserLoginToken;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
public interface ISystemUserService extends IService<SystemUser> {


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
     * 查询登录用户
     * @return
     */
    SystemUserVO getLoginUser();
}
