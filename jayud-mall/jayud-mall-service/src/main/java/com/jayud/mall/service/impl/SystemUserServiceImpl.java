package com.jayud.mall.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.SystemUserMapper;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-23
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    @Autowired
    SystemUserMapper systemUserMapper;

    @Override
    public SystemUserVO login(SystemUserLoginForm loginForm) {

        // TODO 后面要使用Security框架

        String loginname = loginForm.getLoginname();
        String password = loginForm.getPassword();
        SystemUserVO userVO = systemUserMapper.findSystemUserByLoginname(loginname);

        if(userVO != null){
            String dbPassword = userVO.getPassword();
            if(dbPassword.equalsIgnoreCase(Md5Utils.getMD5(password.getBytes()))){
                return userVO;
            }
        }
        return null;
    }
}
