package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.auth.model.po.SysTenant;
import com.jayud.auth.mapper.SysTenantMapper;
import com.jayud.auth.service.ISysTenantService;
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
 * 多租户信息表 服务实现类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {


    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Override
    public IPage<SysTenant> selectPage(SysTenant sysTenant,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<SysTenant> page=new Page<SysTenant>(currentPage,pageSize);
        IPage<SysTenant> pageList= sysTenantMapper.pageList(page, sysTenant);
        return pageList;
    }

    @Override
    public List<SysTenant> selectList(SysTenant sysTenant){
        return sysTenantMapper.list(sysTenant);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        sysTenantMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        sysTenantMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveTenant(SysTenant sysTenant) {
        boolean isAdd = false;
        if (sysTenant.getId() == null){
            isAdd = true;
        }
        if (!checkSameCode(isAdd,sysTenant)){
            if (isAdd){
                this.save(sysTenant);
                return BaseResult.ok(SysTips.ADD_SUCCESS);
            }else {
                this.updateById(sysTenant);
                return BaseResult.ok(SysTips.EDIT_SUCCESS);
            }
        }
        return BaseResult.ok(SysTips.TENANT_CODE_SAME);
    }


    /**
     * @description 判断是否有相同编码
     * @author  ciro
     * @date   2022/2/22 10:50
     * @param: isAdd
     * @param: sysTenant
     * @return: boolean
     **/
    private boolean checkSameCode(boolean isAdd,SysTenant sysTenant){
        SysTenant checks = new SysTenant();
        checks.setTenantCode(sysTenant.getTenantCode());
        List<SysTenant> tenantList = selectList(checks);
        if (isAdd){
            if (CollUtil.isNotEmpty(tenantList)){
                return true;
            }else {
                return false;
            }
        }else {
            if (CollUtil.isEmpty(tenantList)){
                return false;
            }else {
                if (tenantList.get(0).getId().equals(sysTenant.getId())){
                    return false;
                }
            }
        }
        return true;
    }

}
