package com.jayud.oms.security.service.impl;

import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.security.enums.MiniResultEnums;
import com.jayud.oms.service.IDriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限操作
 */
@Service
public class PermissionOperationServiceImpl extends AbstractPermissionOperationServiceImpl {

    @Autowired
    private IDriverInfoService driverInfoService;

    @Override
    public void customCheck(String id) throws SecurityException {
        //校验用户状态是否是禁用状态
        DriverInfo driverInfo = driverInfoService.getById(id);
        if (driverInfo == null) throw new SecurityException(MiniResultEnums.ERROR_ACCOUNT_OR_PASSWORD.getMsg());
        if (StatusEnum.INVALID.getCode().equals(driverInfo.getStatus()))
            throw new SecurityException(MiniResultEnums.USER_HAS_BEEN_DISABLED.getMsg()); //禁用状态
    }
}
