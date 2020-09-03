package com.jayud.oauth.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.po.SystemUser;
import com.jayud.model.po.SystemUserLoginLog;
import com.jayud.model.vo.SystemUserVO;
import com.jayud.model.vo.UserLoginToken;
import com.jayud.oauth.mapper.SystemUserMapper;
import com.jayud.oauth.service.ISystemRoleService;
import com.jayud.oauth.service.ISystemUserLoginLogService;
import com.jayud.oauth.service.ISystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Slf4j
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {


    @Autowired
    ISystemRoleService roleService;

    @Autowired
    ISystemUserLoginLogService loginLogService;


    @Override
    public SystemUser selectByName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);
        //校验用户名是否重复
        return getOne(queryWrapper);
    }


    @Override
    public SystemUserVO login(UserLoginToken token) {

        // 获取Subject实例对象，用户实例
        Subject subject = SecurityUtils.getSubject();

        SystemUserVO cacheUser =  null;
        // 认证
        // 传到 MyShiroRealm 类中的方法进行认证
        try {
            subject.login(token);
        }catch (ApiException e){
            throw e;
        }catch(Exception e){
            Asserts.fail(ResultEnum.LOGIN_FAIL);
        }
        // 构建缓存用户信息返回给前端
        SystemUser user = (SystemUser) subject.getPrincipals().getPrimaryPrincipal();
        //响应前端数据
        cacheUser = convert(user);
        log.warn("CacheUser is {}", JSONUtil.toJsonStr(cacheUser));
        //保存登录记录
        insertLoginLog(user);

        return cacheUser;
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public SystemUserVO getLoginUser(){
        Subject subject = SecurityUtils.getSubject();
        SystemUser user = (SystemUser) subject.getPrincipals().getPrimaryPrincipal();
        //查询最新用户信息
        user = getById(user.getId());
        //用户
        SystemUserVO userVO = convert(user);

        userVO.setRoles(roleService.getRoleList(user.getId()));

        return userVO;
    }

    /**
     * 添加登录记录
     * @param user 用户
     */
    private void insertLoginLog(SystemUser user) {
        SystemUserLoginLog loginLog = new SystemUserLoginLog();
        loginLog.setSystemUserId(user.getId());
        loginLog.setCreateTime(LocalDateTime.now());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogService.save(loginLog);
    }

    /**
     * 参数转换
     * @param user
     * @return
     */
    private SystemUserVO convert(SystemUser user){
        return ConvertUtil.convert(user,SystemUserVO.class);
    }

    /**
     * 参数转换
     * @param userVO
     * @return
     */
    private SystemUser convert(SystemUserVO userVO){
        return ConvertUtil.convert(userVO,SystemUser.class);
    }
}
