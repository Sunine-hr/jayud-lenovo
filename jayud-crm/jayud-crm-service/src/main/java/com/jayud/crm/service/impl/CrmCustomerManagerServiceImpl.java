package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.crm.model.form.CrmCustomerForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerManager;
import com.jayud.crm.mapper.CrmCustomerManagerMapper;
import com.jayud.crm.service.ICrmCustomerManagerService;
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
 * 基本档案_客户_客户维护人(crm_customer_manager) 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCustomerManagerServiceImpl extends ServiceImpl<CrmCustomerManagerMapper, CrmCustomerManager> implements ICrmCustomerManagerService {


    @Autowired
    private CrmCustomerManagerMapper crmCustomerManagerMapper;

    @Override
    public IPage<CrmCustomerManager> selectPage(CrmCustomerManager crmCustomerManager,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCustomerManager> page=new Page<CrmCustomerManager>(currentPage,pageSize);
        IPage<CrmCustomerManager> pageList= crmCustomerManagerMapper.pageList(page, crmCustomerManager);
        return pageList;
    }

    @Override
    public List<CrmCustomerManager> selectList(CrmCustomerManager crmCustomerManager){
        return crmCustomerManagerMapper.list(crmCustomerManager);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerManagerMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerManagerMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerManagerForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerManagerForExcel(paramMap);
    }

    @Override
    public void saveByCustomer(CrmCustomerForm crmCustomerForm) {
        CrmCustomerManager crmCustomerManager = new CrmCustomerManager();
        crmCustomerManager.setCustId(crmCustomerForm.getId());
        crmCustomerManager.setIsCharger(true);
        List<CrmCustomerManager> managerList = selectList(crmCustomerManager);
        boolean isAdd = false;
        if (CollUtil.isNotEmpty(managerList)){
            if (managerList.size() == 1){
                CrmCustomerManager crmCustomerManager1 = managerList.get(0);
                //不同负责人
                if (!crmCustomerManager1.getManageUserId().equals(crmCustomerForm.getManagerUserId())){
                    this.logicDel(crmCustomerManager1.getId());
                    isAdd = true;
                }
            }
        }else {
            isAdd = true;
        }
        if (isAdd){
            //对接人信息
            crmCustomerManager.setManageUserId(crmCustomerForm.getManagerUserId());
            crmCustomerManager.setManageRoles(crmCustomerForm.getManagerUserRoleCode());
            crmCustomerManager.setManagerRolesName(crmCustomerForm.getManagerUserRoleName());
            //客户业务类型
            crmCustomerManager.setManagerBusinessCode(crmCustomerForm.getBusinessTypes());
            crmCustomerManager.setManagerBusinessName(crmCustomerForm.getBusinessTypesName());
            crmCustomerManager.setIsSale(true);
            this.save(crmCustomerManager);
        }
    }

    @Override
    public BaseResult saveManager(CrmCustomerManager crmCustomerManager) {
        boolean isAdd = false;
        if (crmCustomerManager.getId() == null){
            isAdd = true;
            CrmCustomerManager crmCustomerManager1 = this.getById(crmCustomerManager.getId());
            if (crmCustomerManager1.getIsCharger()){
                return BaseResult.error(SysTips.NOT_EDIT_CHARGER_ERROR);
            }
        }
        if (isAdd){
            this.save(crmCustomerManager);
        }else {
            this.updateById(crmCustomerManager);
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else {
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
    }

}
