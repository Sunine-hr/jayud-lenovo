package com.jayud.oms.security.service.impl;

import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.security.entity.Certificate;
import com.jayud.oms.security.enums.MiniResultEnums;
import com.jayud.oms.service.IDriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录操作类
 */
@Service
public class LoginOperatingServiceImpl extends AbstractLoginOperatingServiceImpl {

    @Autowired
    private IDriverInfoService driverInfoService;

    /**
     * 登录校验
     *
     * @param account 账号
     * @return
     */
    @Override
    public void customCheck(String account) throws SecurityException {
        //校验用户状态是否是禁用状态
        DriverInfo driverInfo = this.driverInfoService.getByPhone(account);
        //验证不通过要抛出异常
        if (driverInfo == null) throw new SecurityException(MiniResultEnums.ERROR_ACCOUNT_OR_PASSWORD.getMsg()); //账号不存在
        if (StatusEnum.INVALID.getCode().equals(driverInfo.getStatus()))
            throw new SecurityException(MiniResultEnums.USER_HAS_BEEN_DISABLED.getMsg()); //禁用状态
    }

    /**
     * 获取用户凭证,进行密码校验
     *
     * @param account 账号
     * @return
     */
    @Override
    public Certificate getCertificate(String account) {
        DriverInfo driverInfo = this.driverInfoService.getByPhone(account);
        Certificate certificate = new Certificate();
        certificate.setId(String.valueOf(driverInfo.getId()));
        certificate.setCertificate(driverInfo.getPassword());
        return certificate;
    }


    /**
     * 登录成功回调参数，返回给前端参数
     *
     * @param id 登录成功的用户id
     * @return
     */
    @Override
    public Map<String, Object> callback(String id) {
        //例子
        DriverInfo driverInfo = this.driverInfoService.getById(id);
        Map<String, Object> map = new HashMap<>();
//        map.put("name", user.getNickname());
//        map.put("age", user.getAge());
//        map.put("lastLoginTime", user.getLastLoginTime());

        return map;
    }

    //登录成功之后，自定操作（一般使用不上）
    @Override
    public void customOperation(String id) {

    }

}
