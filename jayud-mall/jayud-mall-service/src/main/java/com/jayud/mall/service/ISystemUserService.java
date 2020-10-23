package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.vo.SystemUserVO;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-23
 */
public interface ISystemUserService extends IService<SystemUser> {

    SystemUserVO login(SystemUserLoginForm loginForm);
}
