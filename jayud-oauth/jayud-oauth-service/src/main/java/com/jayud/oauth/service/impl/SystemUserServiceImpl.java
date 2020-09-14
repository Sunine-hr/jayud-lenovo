package com.jayud.oauth.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.model.bo.AuditSystemUserForm;
import com.jayud.model.bo.OprSystemUserForm;
import com.jayud.model.bo.QuerySystemUserForm;
import com.jayud.model.enums.StatusEnum;
import com.jayud.model.po.SystemUser;
import com.jayud.model.po.SystemUserLoginLog;
import com.jayud.model.vo.*;
import com.jayud.oauth.mapper.SystemUserMapper;
import com.jayud.oauth.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    ISystemMenuService systemMenuService;

    @Autowired
    ISystemWorkService systemWorkService;

    @Autowired
    ISystemCompanyService companyService;

    @Autowired
    ISystemDepartmentService departmentService;

    @Autowired
    ISystemUserRoleRelationService roleRelationService;

    @Override
    public SystemUser selectByName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);
        queryWrapper.eq("audit_status", StatusEnum.AUDIT_SUCCESS.getCode());
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
        cacheUser = ConvertUtil.convert(user,SystemUserVO.class);
        //缓存用户ID larry 2020年8月13日11:21:11
        String uid = redisUtils.get(user.getId().toString());
        if(uid == null){
            redisUtils.set(user.getId().toString(),subject.getSession().getId().toString());
        }
        redisUtils.set("loginUser",user.getName());
        cacheUser.setToken(subject.getSession().getId().toString());
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
    public SystemUserLoginInfoVO findLoginUserInfo() {
        SystemUserLoginInfoVO loginInfoVO = new SystemUserLoginInfoVO();
        //构建用户拥有角色和菜单
        Long userId = getLoginUser().getId();
        List<SystemRoleVO> roleVOS = roleService.getRoleList(userId);
        List<Long> roleIds = new ArrayList<>();
        for (SystemRoleVO systemRoleVO:roleVOS) {
            roleIds.add(systemRoleVO.getId());
        }
        List<SystemMenuNode> systemMenuNodes = systemMenuService.roleTreeList(roleIds);
        loginInfoVO.setRoles(roleVOS);
        loginInfoVO.setRoleIds(roleIds);
        loginInfoVO.setMenuNodeList(systemMenuNodes);
        return loginInfoVO;
    }


    @Override
    public IPage<SystemUserVO> getPageList(QuerySystemUserForm form) {
        //定义分页参数
        Page<SystemUser> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("su.id"));
        IPage<SystemUserVO> pageInfo = this.baseMapper.getPageList(page, form);
        return pageInfo;
    }

    @Override
    public UpdateSystemUserVO getSystemUser(Long id) {
        UpdateSystemUserVO updateSystemUserVO = baseMapper.getSystemUser(id);
        return updateSystemUserVO;
    }

    @Override
    public void oprSystemUser(OprSystemUserForm form) {
        if("update".equals(form.getCmd())) {//修改
            SystemUser systemUser = ConvertUtil.convert(form,SystemUser.class);
            systemUser.setPassword("E10ADC3949BA59ABBE56E057F20F883E");//默认密码为:123456
            systemUser.setStatus(1);//账户为启用状态
            systemUser.setAuditStatus(1);
            systemUser.setUpdatedUser(getLoginName());
            baseMapper.updateById(systemUser);
            //创建角色
            roleRelationService.createRelation(form.getRoleId(),form.getId());
        }else if("delete".equals(form.getCmd())){
            SystemUser systemUser = ConvertUtil.convert(form,SystemUser.class);
            systemUser.setStatus(0);
            baseMapper.updateById(systemUser);
        }
    }

    @Override
    public void auditSystemUser(AuditSystemUserForm form) {
        SystemUser systemUser = ConvertUtil.convert(form,SystemUser.class);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("audit_status", "1");
        baseMapper.update(systemUser,queryWrapper);
    }

    @Override
    public SystemUser getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        SystemUser user = (SystemUser) subject.getPrincipals().getPrimaryPrincipal();
        return user;
    }

    @Override
    public List<QueryOrgStructureVO> findOrgStructure() {
        List<QueryOrgStructureVO> queryOrgStructureVOS = new ArrayList<>();
        List<DepartmentVO> departmentVOS = departmentService.findDepartment(null);
        for (DepartmentVO departmentVO : departmentVOS) {
            QueryOrgStructureVO orgStructureVO = new QueryOrgStructureVO();
            orgStructureVO.setId(departmentVO.getId());
            orgStructureVO.setFId(departmentVO.getFId());
            orgStructureVO.setLabel(departmentVO.getName());
            queryOrgStructureVOS.add(orgStructureVO);
        }
        return convertDepartTree(queryOrgStructureVOS, 0L);
    }

    /**
     * 生成组织架构树
     * @param orgStructureVOS
     * @param parentId
     * @return
     */
    private List<QueryOrgStructureVO> convertDepartTree(List<QueryOrgStructureVO> orgStructureVOS, long parentId) {
        return orgStructureVOS.stream()
                .filter(org -> org.getFId().equals(parentId))
                .map(org -> covertMenuNode(org, orgStructureVOS)).collect(Collectors.toList());
    }
    private QueryOrgStructureVO covertMenuNode(QueryOrgStructureVO org, List<QueryOrgStructureVO> orgList) {
        //设置菜单
        org.setChildren(convertDepartTree(orgList, org.getId()));
        return org;
    }


    @Override
    public List<DepartmentChargeVO> findOrgStructureCharge(Long departmentId) {
        return baseMapper.findOrgStructureCharge(departmentId);
    }

    @Override
    public void saveOrUpdateSystemUser(SystemUser systemUser) {
        saveOrUpdate(systemUser);
    }

    @Override
    public List<SystemUser> findUserByCondition(Map<String, Object> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        for(String key : param.keySet()){
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key,value);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateIsCharge(Long departmentId) {
        SystemUser systemUser = new SystemUser();
        systemUser.setUpdatedUser(getLoginName());
        systemUser.setUpdatedTime(DateUtils.getNowTime());
        systemUser.setIsDepartmentCharge("0");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("department_id",departmentId);
        baseMapper.update(systemUser,queryWrapper);
    }


    /**
     * 获取created_user和updated_user
     * @return
     */
    private String getLoginName(){
        return getLoginUser().getName();
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

}
