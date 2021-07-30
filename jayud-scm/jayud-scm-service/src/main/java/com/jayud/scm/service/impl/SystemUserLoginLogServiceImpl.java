package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.mapper.SystemUserLoginLogMapper;
import com.jayud.scm.model.po.SystemUserLoginLog;
import com.jayud.scm.service.ISystemUserLoginLogService;
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
