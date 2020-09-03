package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.po.SystemUserLoginLog;
import com.jayud.oauth.mapper.SystemUserLoginLogMapper;
import com.jayud.oauth.service.ISystemUserLoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户登录日志表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-03
 */
@Service
public class SystemUserLoginLogServiceImpl extends ServiceImpl<SystemUserLoginLogMapper, SystemUserLoginLog> implements ISystemUserLoginLogService {

}
