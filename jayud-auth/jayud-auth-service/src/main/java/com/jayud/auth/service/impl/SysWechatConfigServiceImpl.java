package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.po.SysSmsConfig;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysWechatConfig;
import com.jayud.auth.mapper.SysWechatConfigMapper;
import com.jayud.auth.service.ISysWechatConfigService;
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
 * 系统微信配置表 服务实现类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Service
public class SysWechatConfigServiceImpl extends ServiceImpl<SysWechatConfigMapper, SysWechatConfig> implements ISysWechatConfigService {


    @Autowired
    private SysWechatConfigMapper sysWechatConfigMapper;

    @Override
    public IPage<SysWechatConfig> selectPage(SysWechatConfig sysWechatConfig,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysWechatConfig> page=new Page<SysWechatConfig>(currentPage,pageSize);
        IPage<SysWechatConfig> pageList= sysWechatConfigMapper.pageList(page, sysWechatConfig);
        return pageList;
    }

    @Override
    public List<SysWechatConfig> selectList(SysWechatConfig sysWechatConfig){
        return sysWechatConfigMapper.list(sysWechatConfig);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysWechatConfigMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysWechatConfigMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveConfig(SysWechatConfig sysWechatConfig) {
        if (sysWechatConfig.getId() == null){
            this.save(sysWechatConfig);
        }else {
            this.updateById(sysWechatConfig);
        }
        return BaseResult.ok(SysTips.SAVE_SUCCESS);
    }

    @Override
    public BaseResult<SysWechatConfig> selectByTenantCode(String tenantCode) {
        LambdaQueryWrapper<SysWechatConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysWechatConfig::getTenantCode,tenantCode);
        lambdaQueryWrapper.eq(SysWechatConfig::getIsDeleted,false);
        SysWechatConfig sysWechatConfig = this.getOne(lambdaQueryWrapper);
        if (sysWechatConfig == null){
            sysWechatConfig = new SysWechatConfig();
        }
        return BaseResult.ok(sysWechatConfig);
    }

}
