package com.jayud.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.bo.SysTenantForm;
import com.jayud.auth.service.ISysTenantToSystemService;
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
    private ISysTenantToSystemService sysTenantToSystemService;

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
//        sysTenantMapper.deleteById(id);
        sysTenantMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult saveTenant(SysTenantForm sysTenantForm) {
        boolean isAdd = false;
        if (sysTenantForm.getId() == null){
            isAdd = true;
        }
        if (!checkSameCode(isAdd,sysTenantForm)){
            if (isAdd){
                this.save(sysTenantForm);
            }else {
                this.updateById(sysTenantForm);
            }
            sysTenantToSystemService.saveTenantSystemRelation(sysTenantForm);
            if (isAdd){
                return BaseResult.ok(SysTips.ADD_SUCCESS);
            }else {
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
     * @param: sysTenantForm
     * @return: boolean
     **/
    private boolean checkSameCode(boolean isAdd,SysTenantForm sysTenantForm){
        SysTenant checks = new SysTenant();
        checks.setTenantCode(sysTenantForm.getTenantCode());
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
                if (tenantList.get(0).getId().equals(sysTenantForm.getId())){
                    return false;
                }
            }
        }
        return true;
    }

}
