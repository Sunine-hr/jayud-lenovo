package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.crm.model.bo.CrmCustomerManagerForm;
import com.jayud.crm.model.form.CrmCustomerForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    private ICrmCustomerManagerService crmCustomerManagerService;

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
    public BaseResult saveByCustomer(CrmCustomerForm crmCustomerForm) {
        boolean isAdd = false;
        boolean isUpdate = false;
        CrmCustomerManager crmCustomerManager = new CrmCustomerManager();
        crmCustomerManager.setCustId(crmCustomerForm.getId());
        crmCustomerManager.setIsCharger(true);
        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
        List<CrmCustomerManager> managerList = selectList(crmCustomerManager);
        CrmCustomerManager onlyManager = new CrmCustomerManager();
        if (CollUtil.isNotEmpty(managerList)){
            onlyManager = managerList.get(0);
        }

        //编辑
        if (!crmCustomerForm.getIsCustAdd()) {
            if (CollUtil.isNotEmpty(managerList)) {
                CrmCustomerManager crmCustomerManager1 = managerList.get(0);
                //不是负责人修改数据
                if (!crmCustomerManager1.getId().equals(authUserDetail.getId())) {
                    return BaseResult.error();
                }
                //不同负责人
                if (!crmCustomerManager1.getManageUserId().equals(crmCustomerForm.getManagerUserId())) {
                    this.logicDel(crmCustomerManager1.getId());
                    isAdd = true;
                }else {
                    if (crmCustomerForm.getIsChangeBusniessType()){
                        isUpdate = true;
                    }
                }
            } else {
                isAdd = true;
            }
        }else {
            isAdd = true;
        }
        if (isAdd){
            //对接人信息
            crmCustomerManager.setManageUserId(crmCustomerForm.getManagerUserId());
            crmCustomerManager.setManageUsername(crmCustomerForm.getManagerUsername());
            crmCustomerManager.setManageRoles(crmCustomerForm.getManagerUserRoleCode());
            crmCustomerManager.setManagerRolesName(crmCustomerForm.getManagerUserRoleName());
            //客户业务类型
            crmCustomerManager.setManagerBusinessCode(crmCustomerForm.getBusinessTypes());
            crmCustomerManager.setManagerBusinessName(crmCustomerForm.getBusinessTypesName());
            crmCustomerManager.setIsSale(true);
            this.save(crmCustomerManager);
        }
        if (isUpdate){
            onlyManager.setManagerBusinessCode(crmCustomerForm.getBusinessTypes());
            onlyManager.setManagerBusinessName(crmCustomerForm.getBusinessTypesNames());
            this.updateById(onlyManager);
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult<CrmCustomerManagerForm> saveManager(CrmCustomerManagerForm crmCustomerManagerForm) {
        boolean isAdd = false;
        crmCustomerManagerForm.setManagerBusinessCode(StringUtils.join(crmCustomerManagerForm.getBusinessTypesList(), StrUtil.C_COMMA));
        if (crmCustomerManagerForm.getId() == null){
            isAdd = true;
            CrmCustomerManager crmCustomerManager1 = this.getById(crmCustomerManagerForm.getId());
            if (crmCustomerManager1.getIsCharger()){
                return BaseResult.error(SysTips.NOT_EDIT_CHARGER_ERROR);
            }
        }
        if (isAdd){
            this.save(crmCustomerManagerForm);
        }else {
            this.updateById(crmCustomerManagerForm);
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS,crmCustomerManagerForm);
        }else {
            return BaseResult.ok(SysTips.EDIT_SUCCESS,crmCustomerManagerForm);
        }
    }

    @Override
    public CrmCustomerManagerForm selectById(Long id) {
        CrmCustomerManager crmCustomerManager = this.getById(id);
        CrmCustomerManagerForm crmCustomerManagerForm = new CrmCustomerManagerForm();
        BeanUtils.copyProperties(crmCustomerManager,crmCustomerManagerForm);
        crmCustomerManagerForm.setBusinessTypesList(Arrays.asList(crmCustomerManager.getManagerBusinessCode().split(StrUtil.COMMA)));
        return crmCustomerManagerForm;
    }

}
