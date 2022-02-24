package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.po.SysEnterpriseDingdingConfig;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysEnterpriseWechatConfig;
import com.jayud.auth.mapper.SysEnterpriseWechatConfigMapper;
import com.jayud.auth.service.ISysEnterpriseWechatConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统企业微信配置表 服务实现类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Service
public class SysEnterpriseWechatConfigServiceImpl extends ServiceImpl<SysEnterpriseWechatConfigMapper, SysEnterpriseWechatConfig> implements ISysEnterpriseWechatConfigService {


    @Autowired
    private SysEnterpriseWechatConfigMapper sysEnterpriseWechatConfigMapper;

    @Override
    public IPage<SysEnterpriseWechatConfig> selectPage(SysEnterpriseWechatConfig sysEnterpriseWechatConfig,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysEnterpriseWechatConfig> page=new Page<SysEnterpriseWechatConfig>(currentPage,pageSize);
        IPage<SysEnterpriseWechatConfig> pageList= sysEnterpriseWechatConfigMapper.pageList(page, sysEnterpriseWechatConfig);
        return pageList;
    }

    @Override
    public List<SysEnterpriseWechatConfig> selectList(SysEnterpriseWechatConfig sysEnterpriseWechatConfig){
        return sysEnterpriseWechatConfigMapper.list(sysEnterpriseWechatConfig);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysEnterpriseWechatConfigMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysEnterpriseWechatConfigMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public BaseResult saveConfig(SysEnterpriseWechatConfig sysEnterpriseWechatConfig) {
        if (sysEnterpriseWechatConfig.getId() == null){
            this.save(sysEnterpriseWechatConfig);
        }else {
            this.updateById(sysEnterpriseWechatConfig);
        }
        return BaseResult.ok(SysTips.SAVE_SUCCESS);
    }

    @Override
    public BaseResult<SysEnterpriseWechatConfig> selectByTenantCode(String tenantCode) {
        LambdaQueryWrapper<SysEnterpriseWechatConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysEnterpriseWechatConfig::getTenantCode,tenantCode);
        lambdaQueryWrapper.eq(SysEnterpriseWechatConfig::getIsDeleted,false);
        SysEnterpriseWechatConfig sysEnterpriseWechatConfig = this.getOne(lambdaQueryWrapper);
        if (sysEnterpriseWechatConfig == null){
            sysEnterpriseWechatConfig = new SysEnterpriseWechatConfig();
        }
        return BaseResult.ok(sysEnterpriseWechatConfig);
    }



}
