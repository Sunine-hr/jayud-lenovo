package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.mapper.SysUserRoleMapper;
import com.jayud.auth.mapper.SysUserToWarehouseMapper;
import com.jayud.auth.model.bo.SysUserForm;
import com.jayud.auth.model.dto.SysUserDTO;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysUserRole;
import com.jayud.auth.model.po.SysUserToWarehouse;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.ISysDepartService;
import com.jayud.auth.service.ISysUserToWarehouseService;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.mapper.SysUserMapper;
import com.jayud.auth.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台用户表 服务实现类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ISysUserToWarehouseService sysUserToWarehouseService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserToWarehouseMapper sysUserToWarehouseMapper;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public IPage<SysUserVO> selectPage(SysUserForm sysUserForm,
                                       Integer currentPage,
                                       Integer pageSize,
                                       HttpServletRequest req) {

        Page<SysUserForm> page = new Page<SysUserForm>(currentPage, pageSize);
        IPage<SysUserVO> pageList = sysUserMapper.pageList(page, sysUserForm);
        return pageList;
    }

    @Override
    public List<SysUserVO> selectList(SysUser sysUser) {
        return sysUserMapper.list(sysUser);
    }

    @Override
    public List<SysUserVO> selectIdsList(SysUserForm sysUserForm) {
        return sysUserMapper.findSelectIdsList(sysUserForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateSysUser(SysUserForm sysUserForm) {
        Boolean result = null;
        SysUser sysUser = ConvertUtil.convert(sysUserForm, SysUser.class);
        if (sysUser.getId() != null) {

            //先删除掉之前的信息关联信息
            SysUserRole sysUserRole1 = new SysUserRole();
            sysUserRole1.setUserId(sysUser.getId());
            sysUserRoleMapper.updateSysUserRoleMultiRow(sysUserRole1);
            //修改

            //截取集合拼接成字符串  未完 。。。。
            //所属部门id 的节点   departIdLists
            String fileNameString = null;
            if (sysUserForm.getDepartIdLists().size() != 0) {
                fileNameString = com.jayud.common.utils.StringUtils.getFileNameString(sysUserForm.getDepartIdLists());
            }
            //负责部门节点id集合
            sysUser.setDepartmentList(fileNameString);

            sysUser.setUpdateBy(CurrentUserUtil.getUsername());
            sysUser.setUpdateTime(new Date());
            //后续待定
//            if(!sysUserForm.getNewPassword().equals("")&&sysUserForm.getNewPassword()!=null){
//                //修改秘钥的密码
//                sysUser.setPassword(sysUserForm.getNewPassword());
//            }else {
//                //修改新密码
//                sysUser.setPassword(encoder.encode(sysUser.getPassword())); //密码
//            }

            result = this.updateById(sysUser);
            Long id = sysUser.getId();
            // 然后再次添加角色和菜单关联表
            for (int i = 0; i < sysUserForm.getRoleIds().size(); i++) {
                SysUserRole sysUserRole = new SysUserRole();
                //用户id
                sysUserRole.setUserId(id);
                //角色id
                sysUserRole.setRoleId(sysUserForm.getRoleIds().get(i));
                //创建人
                sysUserRole.setCreateBy(CurrentUserUtil.getUsername());
                sysUserRole.setCreateTime(new Date());
                sysUserRoleMapper.insert(sysUserRole);
            }

            //所选仓库非必填
            if (sysUserForm.getSysUserToWarehouselist() != null) {

                //先删除用户和仓库关联表
                SysUserToWarehouse sysUserToWarehouse1 = new SysUserToWarehouse();
                sysUserToWarehouse1.setUserId(sysUser.getId());
                sysUserToWarehouse1.setUpdateBy(CurrentUserUtil.getUsername());
                sysUserToWarehouseMapper.updateSysUserToWarehouseTwo(sysUserToWarehouse1);

                //在把仓库数据添加进去

                for (int i = 0; i < sysUserForm.getSysUserToWarehouselist().size(); i++) {

                    SysUserToWarehouse sysUserToWarehouse = new SysUserToWarehouse();
                    sysUserToWarehouse.setUserId(id);//用户id
                    sysUserToWarehouse.setWarehouseId(sysUserForm.getSysUserToWarehouselist().get(i).getWarehouseId());
                    sysUserToWarehouse.setWarehouseName(sysUserForm.getSysUserToWarehouselist().get(i).getWarehouseName());
                    sysUserToWarehouse.setWarehouseCode(sysUserForm.getSysUserToWarehouselist().get(i).getWarehouseCode());
                    sysUserToWarehouseMapper.insert(sysUserToWarehouse);
                }

            }
        } else {
            //新增
            sysUser.setCreateBy(CurrentUserUtil.getUsername());
            sysUser.setCreateTime(new Date());

            //截取集合拼接成字符串  未完 。。。。
            //所属部门id 的节点   departIdLists
            String fileNameString = null;
            if (sysUserForm.getDepartIdLists().size() != 0) {
                fileNameString = com.jayud.common.utils.StringUtils.getFileNameString(sysUserForm.getDepartIdLists());
            }

            //负责部门节点id集合
            sysUser.setDepartmentList(fileNameString);
            if (sysUser.getPassword().equals("")) {
                //设置默认密码
                sysUser.setPassword(encoder.encode("666888"));
            } else {
                //反之保存自己填写的密码
                sysUser.setPassword(encoder.encode(sysUser.getPassword()));
            }
            result = this.saveOrUpdate(sysUser);
            Long id = sysUser.getId();
            //再次添加角色和菜单关联表
            for (int i = 0; i < sysUserForm.getRoleIds().size(); i++) {
                SysUserRole sysUserRole = new SysUserRole();
                //用户id
                sysUserRole.setUserId(id);
                //角色id
                sysUserRole.setRoleId(sysUserForm.getRoleIds().get(i));
                //创建人
                sysUserRole.setCreateBy(CurrentUserUtil.getUsername());
                sysUserRole.setCreateTime(new Date());
                sysUserRoleMapper.insert(sysUserRole);
            }

            if (sysUserForm.getSysUserToWarehouselist() != null) {
                //在把仓库数据添加进去
                for (int i = 0; i < sysUserForm.getSysUserToWarehouselist().size(); i++) {

                    SysUserToWarehouse sysUserToWarehouse = new SysUserToWarehouse();
                    //用户id
                    sysUserToWarehouse.setUserId(id);
                    sysUserToWarehouse.setWarehouseId(sysUserForm.getSysUserToWarehouselist().get(i).getWarehouseId());
                    sysUserToWarehouse.setWarehouseName(sysUserForm.getSysUserToWarehouselist().get(i).getWarehouseName());
                    sysUserToWarehouse.setWarehouseCode(sysUserForm.getSysUserToWarehouselist().get(i).getWarehouseCode());
                    sysUserToWarehouseMapper.insert(sysUserToWarehouse);
                }

            }

        }
//        sysUserToWarehouseService.saveWarehouse(sysUser.getId(),sysUserForm.getWarehouseList());
        if (result) {
            log.warn("新增或修改库区成功");
            return true;
        }
        return false;
    }

    @Override
    public SysUserVO findSysUserName(SysUserForm sysUserForm) {

        return sysUserMapper.findSysUserNameOne(sysUserForm);
    }

    @Override
    public SysUser findSysUserNameOne(SysUserForm sysUserForm) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getName, sysUserForm.getName());
        lambdaQueryWrapper.eq(SysUser::getIsDeleted, false);
        SysUser one = this.getOne(lambdaQueryWrapper);
        return one;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        sysUserMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteSysUser(List<Long> ids) {

        List<SysUser> sysUsers = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            //判断是不是 在职 在职不可删除
            SysUser sysUser1 = new SysUser();
            sysUser1.setId(ids.get(i));
            sysUser1.setJobStatus(1);
            List<SysUserVO> list = sysUserMapper.list(sysUser1);
            if (list.size() != 0) {
                return BaseResult.error(SysTips.SYS_USER_ON_JOB_REMIND);
            }
            SysUser sysUser = new SysUser();
            sysUser.setId(ids.get(i));
            sysUser.setIsDeleted(true);
            sysUsers.add(sysUser);
        }
        this.updateBatchById(sysUsers);
        return BaseResult.ok();
    }

    @Override
    public SysUser getUserByUserName(String tenantCode, String name) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tenantCode)) {
            lambdaQueryWrapper.eq(SysUser::getCode, tenantCode);
        }
        lambdaQueryWrapper.eq(SysUser::getName, name);
        lambdaQueryWrapper.eq(SysUser::getIsDeleted, false);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public BaseResult checkUserStatus(String tenantCode, String name, String password) {
        SysUser sysUser = getUserByUserName(tenantCode, name);
        if (sysUser == null) {
            return BaseResult.error(SysTips.ACCOUNT_NON_EXISTENT);
        }
        if (!encoder.matches(password, sysUser.getPassword())) {
            return BaseResult.error(SysTips.LOGIN_ERROR);
        }
        if (sysUser.getJobStatus().equals(0)) {
            return BaseResult.error(SysTips.ACCOUNT_RESIGNED);
        }
        if (sysUser.getStatus().equals(0)) {
            return BaseResult.error(SysTips.ACCOUNT_FROZEN);
        }
        return BaseResult.ok();
    }


    @Override
    public SysUserVO findSysUserIdOne(SysUserForm sysUserForm) {

        return sysUserMapper.findSysUserIdOne(sysUserForm);
    }

    @Override
    public BaseResult findUpdateUserPassword(SysUserForm sysUserForm) {
        Boolean result = null;
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getName, CurrentUserUtil.getUsername());
        SysUser one = this.getOne(lambdaQueryWrapper);

        //旧密码校验
        if (one.getPassword().equals(encoder.encode(sysUserForm.getPassword()))) {
            return BaseResult.error("当前密码错误！");
        }
        //旧密码和新密码 不能相同
        if (one.getPassword().equals(encoder.encode(sysUserForm.getNewPassword()))) {
            return BaseResult.error("修改密码不能和之前密码相同!");
        }
        if (!one.getPassword().equals(encoder.encode(sysUserForm.getPassword()))) {
            if (one.getId() != null) {
                one.setUpdateBy(CurrentUserUtil.getUsername());
                one.setUpdateTime(new Date());
                //新密码
                one.setPassword(encoder.encode(sysUserForm.getNewPassword())); //新密码
                result = this.updateById(one);
            }
        }
        if (result) {
            log.warn("修改成功!");
            return BaseResult.error("修改成功!");
        }
        return BaseResult.ok();
    }

    @Override
    public SysUserVO getSystemUserByName(String token) {
        return sysUserMapper.getSystemUserByName(token);
    }

    @Override
    public List<SysUserDTO> selectUserByRoleCode(String roleCode) {
        if (roleCode.contains(StrUtil.COMMA)) {
            String[] codes = roleCode.split(StrUtil.COMMA);
            roleCode = codes[0];
        }
        List<SysUserDTO> userDtoList = new ArrayList<>();
        String tenantCode = CurrentUserUtil.getUserTenantCode();
        List<SysUser> sysUserList = sysUserMapper.selectUserByRoleCode(roleCode, tenantCode);
        SysDepart sysDepart = new SysDepart();
        sysDepart.setTenantCode(tenantCode);
        List<SysDepart> sysDepartList = sysDepartService.selectList(sysDepart);
        Map<String, String> departMap = new HashMap<>();
        if (CollUtil.isNotEmpty(sysDepartList)) {
            departMap = sysDepartList.stream().collect(Collectors.toMap(x -> String.valueOf(x.getId()), x -> x.getDepartName()));
        }
        if (CollUtil.isNotEmpty(sysUserList)) {
            Map<String, String> finalDepartMap = departMap;
            sysUserList.forEach(sysUser -> {
                SysUserDTO userDto = new SysUserDTO();
                BeanUtils.copyProperties(sysUser, userDto);
                if (StringUtils.isNotBlank(sysUser.getDepartmentList())) {
                    String[] departIds = sysUser.getDepartmentList().split(StrUtil.COMMA);
                    List<String> departNameList = new ArrayList<>();
                    for (String id : departIds) {
                        if (finalDepartMap.containsKey(id)) {
                            departNameList.add(finalDepartMap.get(id));
                        } else {
                            departNameList.add(id);
                        }
                    }
                    userDto.setDepartNames(StringUtils.join(departNameList.toArray(), "-"));
                }
                userDtoList.add(userDto);
            });
        }
        return userDtoList;
    }

    @Override
    @Cacheable(value = "sys:cache:username", key = "#username")
    public SysUserDTO selectByUsername(String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getName, username);
        SysUser sysUser = this.getOne(lambdaQueryWrapper);
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(sysUser, sysUserDTO);
        return sysUserDTO;
    }

    @Override
    @Cacheable(value = "sys:cache:userId", key = "#userId")
    public SysUser selectByUserId(Long userId) {
        return this.getById(userId);
    }

    @Override
    public List<SysUser> getSysUserByDepartmentId(Long departId) {
        QueryWrapper<SysUser> condition = new QueryWrapper<>();
        condition.lambda().eq(SysUser::getDepartId, departId);
        condition.lambda().eq(SysUser::getIsDeleted, 0);
        return this.list(condition);
    }

    @Override
    public List<SysUser> getUserByUserIds(Set<Long> userIds) {
        QueryWrapper<SysUser> condition = new QueryWrapper<>();
        condition.lambda().in(SysUser::getId, userIds);
        condition.lambda().eq(SysUser::getIsDeleted, 0);
        return this.list(condition);
    }

    @Override
    public List<SysUser> getUserByWarehouse(SysUserToWarehouse sysUserToWarehouse) {
        sysUserToWarehouse.setTenantCode(CurrentUserUtil.getUserTenantCode());
        List<SysUserToWarehouse> warehouseList = sysUserToWarehouseService.selectList(sysUserToWarehouse);
        List<SysUser> userList = new ArrayList<>();
        if (CollUtil.isNotEmpty(warehouseList)) {
            List<Long> userIdList = warehouseList.stream().map(x -> x.getUserId()).collect(Collectors.toList());
            LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SysUser::getIsDeleted, false);
            lambdaQueryWrapper.eq(SysUser::getStatus, 1);
            lambdaQueryWrapper.eq(SysUser::getJobStatus, 1);
            lambdaQueryWrapper.eq(SysUser::getIsWarehouseOperator, true);
            lambdaQueryWrapper.in(SysUser::getId, userIdList);
            userList = this.list(lambdaQueryWrapper);
        }
        return userList;
    }

    public static void main(String[] args) {

        String encode = encoder.encode("666888");
        System.out.println(encode);
    }
}
