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
import com.jayud.auth.model.po.SysEmamilConfig;
import com.jayud.auth.mapper.SysEmamilConfigMapper;
import com.jayud.auth.service.ISysEmamilConfigService;
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
 * 系统邮箱配置表 服务实现类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Service
public class SysEmamilConfigServiceImpl extends ServiceImpl<SysEmamilConfigMapper, SysEmamilConfig> implements ISysEmamilConfigService {


    @Autowired
    private SysEmamilConfigMapper sysEmamilConfigMapper;

    @Override
    public IPage<SysEmamilConfig> selectPage(SysEmamilConfig sysEmamilConfig,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysEmamilConfig> page=new Page<SysEmamilConfig>(currentPage,pageSize);
        IPage<SysEmamilConfig> pageList= sysEmamilConfigMapper.pageList(page, sysEmamilConfig);
        return pageList;
    }

    @Override
    public List<SysEmamilConfig> selectList(SysEmamilConfig sysEmamilConfig){
        return sysEmamilConfigMapper.list(sysEmamilConfig);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysEmamilConfigMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysEmamilConfigMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveConfig(SysEmamilConfig sysEmamilConfig) {
        if (sysEmamilConfig.getId() == null){
            this.save(sysEmamilConfig);
        }else {
            this.updateById(sysEmamilConfig);
        }
        return BaseResult.ok(SysTips.SAVE_SUCCESS);
    }

    @Override
    public BaseResult<SysEmamilConfig> selectByTenantCode(String tenantCode) {
        LambdaQueryWrapper<SysEmamilConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysEmamilConfig::getTenantCode,tenantCode);
        lambdaQueryWrapper.eq(SysEmamilConfig::getIsDeleted,false);
        SysEmamilConfig sysEmamilConfig = this.getOne(lambdaQueryWrapper);
        if (sysEmamilConfig == null){
            sysEmamilConfig = new SysEmamilConfig();
        }
        return BaseResult.ok(sysEmamilConfig);
    }

}
