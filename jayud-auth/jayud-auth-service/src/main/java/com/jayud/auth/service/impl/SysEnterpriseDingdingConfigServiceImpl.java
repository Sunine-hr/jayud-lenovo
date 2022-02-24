package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysEnterpriseDingdingConfig;
import com.jayud.auth.mapper.SysEnterpriseDingdingConfigMapper;
import com.jayud.auth.service.ISysEnterpriseDingdingConfigService;
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
 * 系统钉钉配置表 服务实现类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Service
public class SysEnterpriseDingdingConfigServiceImpl extends ServiceImpl<SysEnterpriseDingdingConfigMapper, SysEnterpriseDingdingConfig> implements ISysEnterpriseDingdingConfigService {


    @Autowired
    private SysEnterpriseDingdingConfigMapper sysEnterpriseDingdingConfigMapper;

    @Override
    public IPage<SysEnterpriseDingdingConfig> selectPage(SysEnterpriseDingdingConfig sysEnterpriseDingdingConfig,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysEnterpriseDingdingConfig> page=new Page<SysEnterpriseDingdingConfig>(currentPage,pageSize);
        IPage<SysEnterpriseDingdingConfig> pageList= sysEnterpriseDingdingConfigMapper.pageList(page, sysEnterpriseDingdingConfig);
        return pageList;
    }

    @Override
    public List<SysEnterpriseDingdingConfig> selectList(SysEnterpriseDingdingConfig sysEnterpriseDingdingConfig){
        return sysEnterpriseDingdingConfigMapper.list(sysEnterpriseDingdingConfig);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysEnterpriseDingdingConfigMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysEnterpriseDingdingConfigMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveConfig(SysEnterpriseDingdingConfig sysEnterpriseDingdingConfig) {
        if (sysEnterpriseDingdingConfig.getId() == null){
            this.save(sysEnterpriseDingdingConfig);
        }else {
            this.updateById(sysEnterpriseDingdingConfig);
        }
        return BaseResult.ok(SysTips.SAVE_SUCCESS);
    }

    @Override
    public BaseResult<SysEnterpriseDingdingConfig> selectByTenantCode(String tenantCode) {
        LambdaQueryWrapper<SysEnterpriseDingdingConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysEnterpriseDingdingConfig::getTenantCode,tenantCode);
        lambdaQueryWrapper.eq(SysEnterpriseDingdingConfig::getIsDeleted,false);
        SysEnterpriseDingdingConfig sysEnterpriseDingdingConfig = this.getOne(lambdaQueryWrapper);
        if (sysEnterpriseDingdingConfig == null){
            sysEnterpriseDingdingConfig = new SysEnterpriseDingdingConfig();
        }
        return BaseResult.ok(sysEnterpriseDingdingConfig);
    }

}
