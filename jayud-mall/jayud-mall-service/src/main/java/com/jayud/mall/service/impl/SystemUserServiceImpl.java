package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.SystemUserMapper;
import com.jayud.mall.mapper.SystemUserRoleRelationMapper;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.ResetUserPwdForm;
import com.jayud.mall.model.bo.SaveSystemUserForm;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.po.SystemUserRoleRelation;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.service.ISystemUserRoleRelationService;
import com.jayud.mall.service.ISystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
@Slf4j
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    /**
     * baseService，获取admin项目，登录用户
     */
    @Autowired
    BaseService baseService;

    @Autowired
    SystemUserMapper systemUserMapper;

    @Autowired
    SystemUserRoleRelationMapper systemUserRoleRelationMapper;

    @Autowired
    ISystemUserRoleRelationService systemUserRoleRelationService;

    @Value("${mall.system_user.password:}")
    private String pass;

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
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<SystemUserVO> insertUser(SaveSystemUserForm form) {
        //新增用户，验证
        String phone = form.getPhone();//手机号
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        Integer phoneCount = systemUserMapper.selectCount(queryWrapper);
        if(phoneCount > 0){
            return CommonResult.error(-1, "手机号已存在，不能使用");
        }
        String email = form.getEmail();//邮箱
        queryWrapper = new QueryWrapper<>();//3.3.1.9-SNAPSHOT, wrapper加入了对clear的支持,目前的版本是3.1.2
        queryWrapper.eq("email", email);
        Integer emailCount = systemUserMapper.selectCount(queryWrapper);
        if(emailCount > 0){
            return CommonResult.error(-1, "邮箱已存在，不能使用");
        }
        String name = form.getName();//用户名，登录名
        queryWrapper = new QueryWrapper<>();//3.3.1.9-SNAPSHOT, wrapper加入了对clear的支持,目前的版本是3.1.2
        queryWrapper.eq("name", name);
        Integer nameCount = systemUserMapper.selectCount(queryWrapper);
        if(nameCount > 0){
            return CommonResult.error(-1, "用户名已存在，不能使用");
        }

        AuthUser user = baseService.getUser();
        log.info("user:"+user);

        //从nacos中获取，新增用户，初始化密码
        String pwd = pass;
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bcryptPasswordEncoder.encode(pwd.trim());

        SystemUser systemUser = ConvertUtil.convert(form, SystemUser.class);
        //systemUser.setPassword(Md5Utils.getMD5("123456".getBytes()).toUpperCase()); // MD5 加密
        systemUser.setPassword(password); // BCryptPasswordEncoder 加密

        systemUser.setUserName(form.getName());
        systemUser.setStatus(1);
        systemUser.setNote(null);//备注
        LocalDateTime nowTime = LocalDateTime.now();
        systemUser.setCreatedUser(user.getId().intValue());
        systemUser.setCreatedTime(nowTime);
        systemUser.setUpdatedUser(user.getId().intValue());
        systemUser.setUpdatedTime(nowTime);
        //1.保存用户
        this.saveOrUpdate(systemUser);

        Long userId = systemUser.getId();
        List<Long> roleIds = form.getRoleIds();
        if(roleIds.size() > 0){
            List<SystemUserRoleRelation> systemUserRoleRelationList = new ArrayList<>();
            roleIds.forEach(roleId -> {
                SystemUserRoleRelation systemUserRoleRelation = new SystemUserRoleRelation();
                systemUserRoleRelation.setUserId(userId.intValue());
                systemUserRoleRelation.setRoleId(roleId.intValue());
                systemUserRoleRelationList.add(systemUserRoleRelation);
            });
            //2.保存用户关联的角色
            systemUserRoleRelationService.saveOrUpdateBatch(systemUserRoleRelationList);
        }
        //返回保存对象
        SystemUserVO systemUserVO = ConvertUtil.convert(systemUser, SystemUserVO.class);
        return CommonResult.success(systemUserVO);
    }

    @Override
    public CommonResult<SystemUserVO> updateUser(SaveSystemUserForm form) {
        SystemUser systemUser = ConvertUtil.convert(form, SystemUser.class);

//        systemUserMapper.updateUser(user);    //原始的mybatis修改，不用这个，太麻烦了，还要写sql update 语句
        systemUserMapper.updateById(systemUser);

        //先删除用户的角色
        List<Long> userIds = new ArrayList<>();
        userIds.add(systemUser.getId());
        systemUserRoleRelationService.removeUserRoleRelation(userIds);

        //在重新绑定角色
        if(form.getRoleIds() != null){
            systemUserRoleRelationService.createUserRoleRelation(systemUser,form.getRoleIds());
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        systemUserMapper.deleteUser(id);
        //删除用户的角色
        List<Long> userIds = new ArrayList<>();
        userIds.add(id);
        systemUserRoleRelationService.removeUserRoleRelation(userIds);
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
        String pass = "123456";
        ResetUserPwdForm resetUserPwdForm = new ResetUserPwdForm();
        resetUserPwdForm.setId(id);

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bcryptPasswordEncoder.encode(pass.trim());
        //resetUserPwdForm.setPassword(Md5Utils.getMD5(password.getBytes()).toUpperCase()); //MD5
        resetUserPwdForm.setPassword(password);//BCryptPasswordEncoder
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
