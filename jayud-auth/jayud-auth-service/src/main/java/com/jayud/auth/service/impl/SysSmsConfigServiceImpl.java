package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.po.SysEnterpriseWechatConfig;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysSmsConfig;
import com.jayud.auth.mapper.SysSmsConfigMapper;
import com.jayud.auth.service.ISysSmsConfigService;
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
 * 系统短信配置表 服务实现类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Service
public class SysSmsConfigServiceImpl extends ServiceImpl<SysSmsConfigMapper, SysSmsConfig> implements ISysSmsConfigService {


    @Autowired
    private SysSmsConfigMapper sysSmsConfigMapper;

    @Override
    public IPage<SysSmsConfig> selectPage(SysSmsConfig sysSmsConfig,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysSmsConfig> page=new Page<SysSmsConfig>(currentPage,pageSize);
        IPage<SysSmsConfig> pageList= sysSmsConfigMapper.pageList(page, sysSmsConfig);
        return pageList;
    }

    @Override
    public List<SysSmsConfig> selectList(SysSmsConfig sysSmsConfig){
        return sysSmsConfigMapper.list(sysSmsConfig);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysSmsConfigMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysSmsConfigMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveConfig(SysSmsConfig sysSmsConfig) {
        if (sysSmsConfig.getId() == null){
            this.save(sysSmsConfig);
        }else {
            this.updateById(sysSmsConfig);
        }
        return BaseResult.ok(SysTips.SAVE_SUCCESS);
    }

    @Override
    public BaseResult<SysSmsConfig> selectByTenantCode(String tenantCode) {
        LambdaQueryWrapper<SysSmsConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysSmsConfig::getTenantCode,tenantCode);
        lambdaQueryWrapper.eq(SysSmsConfig::getIsDeleted,false);
        SysSmsConfig sysSmsConfig = this.getOne(lambdaQueryWrapper);
        if (sysSmsConfig == null){
            sysSmsConfig = new SysSmsConfig();
        }
        return BaseResult.ok(sysSmsConfig);
    }

}
