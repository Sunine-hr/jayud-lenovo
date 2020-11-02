package com.jayud.mall.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SystemUserMapper;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.ResetUserPwdForm;
import com.jayud.mall.model.bo.SaveUserForm;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.service.ISystemUserRoleRelationService;
import com.jayud.mall.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    ISystemUserRoleRelationService userRoleRelationService;

    @Override
    public SystemUserVO login(SystemUserLoginForm loginForm) {

        // TODO 后面要使用Security框架

        String loginname = loginForm.getLoginname();
        String password = loginForm.getPassword();
        SystemUserVO userVO = systemUserMapper.findSystemUserByLoginname(loginname);
        // TODO 验证密码 在Security框架里面验证
//        if(userVO != null){
//            String dbPassword = userVO.getPassword();
//            if(dbPassword.equalsIgnoreCase(Md5Utils.getMD5(password.getBytes()))){
//                return userVO;
//            }
//        }
        return userVO;
    }

    @Override
    public List<SystemUserVO> getUserList() {
        return systemUserMapper.getUserList();
    }

    @Override
    public void insertUser(SaveUserForm user) {
        SystemUser systemUser = ConvertUtil.convert(user, SystemUser.class);
        systemUser.setName(systemUser.getUserName());
        systemUser.setPassword(Md5Utils.getMD5("123456".getBytes()).toUpperCase());

        systemUserMapper.insert(systemUser);
        systemUser.setId(systemUser.getId());
        if(user.getRoleIds() != null){
            userRoleRelationService.createUserRoleRelation(systemUser,user.getRoleIds());
        }
    }

    @Override
    public void updateUser(SaveUserForm user) {
        SystemUser systemUser = ConvertUtil.convert(user, SystemUser.class);

//        systemUserMapper.updateUser(user);    //原始的mybatis修改，不用这个，太麻烦了，还要写sql update 语句
        systemUserMapper.updateById(systemUser);

        //先删除用户的角色
        List<Long> userIds = new ArrayList<>();
        userIds.add(systemUser.getId());
        userRoleRelationService.removeUserRoleRelation(userIds);

        //在重新绑定角色
        if(user.getRoleIds() != null){
            userRoleRelationService.createUserRoleRelation(systemUser,user.getRoleIds());
        }

    }

    @Override
    public void deleteUser(Long id) {
        systemUserMapper.deleteUser(id);
        //删除用户的角色
        List<Long> userIds = new ArrayList<>();
        userIds.add(id);
        userRoleRelationService.removeUserRoleRelation(userIds);
    }

    @Override
    public SystemUserVO getUser(Long id) {
        return systemUserMapper.getUser(id);
    }

    @Override
    public void disableUser(Long id) {
        systemUserMapper.disableUser(id);
    }

    @Override
    public void enableUser(Long id) {
        systemUserMapper.enableUser(id);
    }

    @Override
    public void resetPassword(Long id) {
        String password = "123456";
        ResetUserPwdForm resetUserPwdForm = new ResetUserPwdForm();
        resetUserPwdForm.setId(id);
        resetUserPwdForm.setPassword(Md5Utils.getMD5(password.getBytes()).toUpperCase());
        systemUserMapper.resetPassword(resetUserPwdForm);
    }

    @Override
    public IPage<SystemUserVO> findUserByPage(QueryUserForm form) {
        //定义分页参数
        Page<SystemUserVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SystemUserVO> pageInfo = baseMapper.findUserByPage(page, form);
        return pageInfo;
    }


}
