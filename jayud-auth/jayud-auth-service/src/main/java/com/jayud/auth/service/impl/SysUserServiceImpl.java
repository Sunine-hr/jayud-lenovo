package com.jayud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysUserForm;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.mapper.SysUserMapper;
import com.jayud.auth.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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
    private SysUserMapper sysUserMapper;

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
    public boolean saveOrUpdateSysUser(SysUserForm sysUserForm) {
        Boolean result = null;
        SysUser convert = ConvertUtil.convert(sysUserForm, SysUser.class);
        if (convert.getId() != null) {
            convert.setUpdateBy(CurrentUserUtil.getUsername());
            convert.setUpdateTime(new Date());
            result = this.updateById(convert);
        } else {
            convert.setCreateBy(CurrentUserUtil.getUsername());
            convert.setCreateTime(new Date());
            result = this.saveOrUpdate(convert);
        }
        if (result) {
            log.warn("新增或修改库区成功");
            return true;
        }
        return false;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        sysUserMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult deleteSysUser(List<Long> ids) {

        for (int i = 0; i < ids.size(); i++) {
            SysUser sysUser = new SysUser();
            sysUser.setId(ids.get(i));
            sysUser.setIsDeleted(true);
            sysUserMapper.updateById(sysUser);
        }

        return BaseResult.ok();
    }

    @Override
    public SysUser getUserByUserName(String tenantCode, String name) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(tenantCode)) {
            lambdaQueryWrapper.eq(SysUser::getTenantCode, tenantCode);
        }
        lambdaQueryWrapper.eq(SysUser::getName,name);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public BaseResult checkUserStatus(String tenantCode, String name,String password) {
        SysUser sysUser = getUserByUserName(tenantCode, name);
        if (sysUser == null){
            return BaseResult.error(SysTips.ACCOUNT_NON_EXISTENT);
        }
        if (sysUser.getPassword().equals(encoder.encode(password))){
            return BaseResult.error(SysTips.LOGIN_ERROR);
        }
        if (sysUser.getJobStatus().equals(0)){
            return BaseResult.error(SysTips.ACCOUNT_RESIGNED);
        }
        if (sysUser.getStatus().equals(0)){
            return BaseResult.error(SysTips.ACCOUNT_FROZEN);
        }
        return BaseResult.ok();
    }

}
