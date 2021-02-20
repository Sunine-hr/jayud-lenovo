package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SystemUserMapper;
import com.jayud.mall.mapper.SystemUserRoleRelationMapper;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.SaveSystemUserForm;
import com.jayud.mall.model.bo.SystemUserLoginForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.po.SystemUserRoleRelation;
import com.jayud.mall.model.vo.SystemUserVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
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
        //从nacos中获取，新增用户，初始化密码
        String pwd = pass;
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bcryptPasswordEncoder.encode(pwd.trim());

        SystemUser systemUser = ConvertUtil.convert(form, SystemUser.class);
        //systemUser.setPassword(Md5Utils.getMD5("123456".getBytes()).toUpperCase()); // MD5 加密
        systemUser.setPassword(password); // BCryptPasswordEncoder 加密

        systemUser.setUserName(form.getName());
        systemUser.setStatus(0);
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
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<SystemUserVO> updateUser(SaveSystemUserForm form) {
        //修改用户，验证
        Long id = form.getId();
        SystemUser sysUser = systemUserMapper.selectById(id);
        if(sysUser == null){
            return CommonResult.error(-1, "用户不存在，不能修改用户");
        }
        String phone = form.getPhone();//手机号
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.ne("id", id);//不等于<>
        Integer phoneCount = systemUserMapper.selectCount(queryWrapper);
        if(phoneCount > 0){
            return CommonResult.error(-1, "手机号已存在，不能使用");
        }
        String email = form.getEmail();//邮箱
        queryWrapper = new QueryWrapper<>();//3.3.1.9-SNAPSHOT, wrapper加入了对clear的支持,目前的版本是3.1.2
        queryWrapper.eq("email", email);
        queryWrapper.ne("id", id);//不等于<>
        Integer emailCount = systemUserMapper.selectCount(queryWrapper);
        if(emailCount > 0){
            return CommonResult.error(-1, "邮箱已存在，不能使用");
        }
        String name = form.getName();//用户名，登录名
        queryWrapper = new QueryWrapper<>();//3.3.1.9-SNAPSHOT, wrapper加入了对clear的支持,目前的版本是3.1.2
        queryWrapper.eq("name", name);
        queryWrapper.ne("id", id);//不等于<>
        Integer nameCount = systemUserMapper.selectCount(queryWrapper);
        if(nameCount > 0){
            return CommonResult.error(-1, "用户名已存在，不能使用");
        }

        //systemUserMapper.updateUser(user);    //原始的mybatis修改，不用这个，太麻烦了，还要写sql update 语句
        //systemUserMapper.updateById(systemUser);
        AuthUser user = baseService.getUser();
        SystemUser systemUser = ConvertUtil.convert(form, SystemUser.class);

        systemUser.setUserName(form.getName());
        LocalDateTime nowTime = LocalDateTime.now();
        systemUser.setUpdatedUser(user.getId().intValue());//修改人
        systemUser.setUpdatedTime(nowTime);//修改时间
        //1.保存用户
        this.saveOrUpdate(systemUser);

        Long userId = systemUser.getId();
        List<Long> roleIds = form.getRoleIds();
        if(roleIds.size() > 0){
            QueryWrapper<SystemUserRoleRelation> systemUserRoleRelationQueryWrapper = new QueryWrapper<>();
            systemUserRoleRelationQueryWrapper.eq("user_id", userId);
            //2.删除用户关联角色
            systemUserRoleRelationService.remove(systemUserRoleRelationQueryWrapper);

            List<SystemUserRoleRelation> systemUserRoleRelationList = new ArrayList<>();
            roleIds.forEach(roleId -> {
                SystemUserRoleRelation systemUserRoleRelation = new SystemUserRoleRelation();
                systemUserRoleRelation.setUserId(userId.intValue());
                systemUserRoleRelation.setRoleId(roleId.intValue());
                systemUserRoleRelationList.add(systemUserRoleRelation);
            });
            //3.保存用户关联的角色
            systemUserRoleRelationService.saveOrUpdateBatch(systemUserRoleRelationList);
        }

        SystemUserVO systemUserVO = ConvertUtil.convert(systemUser, SystemUserVO.class);
        return CommonResult.success(systemUserVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult deleteUser(Long id) {
        SystemUser systemUser = systemUserMapper.selectById(id);
        //用户删除，验证
        if(systemUser == null){
            return CommonResult.error(-1, "用户不存在，无法删除");
        }
        Integer status = systemUser.getStatus();//帐号启用状态：0-> 启用；1-> 停用
        if(!status.equals(1)){
            return CommonResult.error(-1, "用户未停用，无法删除");
        }
        Long userId = systemUser.getId();
        //删除用户
        systemUserMapper.deleteById(userId);
        //删除用户关联的角色
        QueryWrapper<SystemUserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        systemUserRoleRelationMapper.delete(queryWrapper);
        return CommonResult.success("用户删除成功");
    }

    @Override
    public SystemUserVO getUser(Long id) {
        return systemUserMapper.getUser(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult disableUser(Long id) {
        SystemUser user = systemUserMapper.selectById(id);
        if(user == null){
            return CommonResult.error(-1, "用户不存在，无法禁用。");
        }
        if(user.getStatus().equals(1)){
            return CommonResult.error(-1, "用户已禁用，无须再次操作。");
        }
        //帐号启用状态：0->Off 启用；1->On 停用
        user.setStatus(1);
        AuthUser authUser = baseService.getUser();
        LocalDateTime nowTime = LocalDateTime.now();
        user.setUpdatedUser(authUser.getId().intValue());//修改人
        user.setUpdatedTime(nowTime);//修改时间
        this.saveOrUpdate(user);

        return CommonResult.success("禁用用户成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult enableUser(Long id) {
        SystemUser user = systemUserMapper.selectById(id);
        if(user == null){
            return CommonResult.error(-1, "用户不存在，无法启用。");
        }
        if(user.getStatus().equals(0)){
            return CommonResult.error(-1, "用户已启用，无须再次操作。");
        }
        //帐号启用状态：0->Off 启用；1->On 停用
        user.setStatus(0);
        AuthUser authUser = baseService.getUser();
        LocalDateTime nowTime = LocalDateTime.now();
        user.setUpdatedUser(authUser.getId().intValue());//修改人
        user.setUpdatedTime(nowTime);//修改时间
        systemUserMapper.updateById(user);
        return CommonResult.success("启用用户成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult resetPassword(Long id) {
        SystemUser user = systemUserMapper.selectById(id);
        if(user == null){
            return CommonResult.error(-1, "用户不存在");
        }
        String pwd = pass;
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bcryptPasswordEncoder.encode(pwd.trim());
        user.setPassword(password);
        AuthUser authUser = baseService.getUser();
        LocalDateTime nowTime = LocalDateTime.now();
        user.setUpdatedUser(authUser.getId().intValue());//修改人
        user.setUpdatedTime(nowTime);//修改时间
        systemUserMapper.updateById(user);
        return CommonResult.success("重置用户密码成功");
    }

    @Override
    public IPage<SystemUserVO> findUserByPage(QueryUserForm form) {
        //定义分页参数
        Page<SystemUserVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<SystemUserVO> pageInfo = baseMapper.findUserByPage(page, form);
        return pageInfo;
    }


}
