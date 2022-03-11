package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.po.SysUser;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.enums.RoleCodeEnum;
import com.jayud.common.utils.ListUtil;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.model.bo.ComCustomerForm;
import com.jayud.crm.model.bo.CrmCustomerManagerForm;
import com.jayud.crm.model.bo.CrmCustomerForm;
import com.jayud.crm.model.po.CrmCustomer;
import com.jayud.crm.service.ICrmCustomerRiskService;
import com.jayud.crm.service.ICrmCustomerService;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private ICrmCustomerService crmCustomerService;
    @Autowired
    private ICrmCustomerRiskService crmCustomerRiskService;

    @Autowired
    private CrmCustomerManagerMapper crmCustomerManagerMapper;

    @Autowired
    private AuthClient authClient;

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
    public void logicDelByIds(List<Long> ids) {
        LambdaQueryWrapper<CrmCustomerManager> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CrmCustomerManager::getId,ids);
        List<CrmCustomerManager> managerList = this.list(lambdaQueryWrapper);
        List<Long> delIds = new ArrayList<>();
        for (CrmCustomerManager manager:managerList){
            if (!manager.getIsCharger()){
                delIds.add(manager.getId());
            }
        }
        crmCustomerManagerMapper.logicDelByIds(delIds,CurrentUserUtil.getUsername());
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
                if (!crmCustomerManager1.getManageUserId().equals(authUserDetail.getId())) {
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
            if (crmCustomerForm.getManagerUserId() != null) {
                //对接人信息
                crmCustomerManager.setManageUserId(crmCustomerForm.getManagerUserId());
                crmCustomerManager.setManageUsername(crmCustomerForm.getManagerUsername());
                crmCustomerManager.setManageRoles(RoleCodeEnum.SALE_ROLE.getRoleCode());
                crmCustomerManager.setManagerRolesName(RoleCodeEnum.SALE_ROLE.getRoleName());
                //客户业务类型
                crmCustomerManager.setManagerBusinessCode(crmCustomerForm.getBusinessTypes());
                crmCustomerManager.setManagerBusinessName(crmCustomerForm.getBusinessTypesNames());
                crmCustomerManager.setIsSale(true);
                crmCustomerManager.setGenerateDate(LocalDateTime.now());
                this.save(crmCustomerManager);
            }
        }
        if (isUpdate){
            if (onlyManager.getId()!=null) {
                onlyManager.setManagerBusinessCode(crmCustomerForm.getBusinessTypes());
                onlyManager.setManagerBusinessName(crmCustomerForm.getBusinessTypesNames());
                this.updateById(onlyManager);
            }
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult<CrmCustomerManagerForm> saveManager(CrmCustomerManagerForm crmCustomerManagerForm) {
        boolean isAdd = false;
        if (CollUtil.isNotEmpty(crmCustomerManagerForm.getBusinessTypesList())) {
            crmCustomerManagerForm.setManagerBusinessCode(StringUtils.join(crmCustomerManagerForm.getBusinessTypesList(), StrUtil.C_COMMA));
            crmCustomerManagerForm.setManagerBusinessName(crmCustomerService.changeBusinessType(crmCustomerManagerForm.getBusinessTypesList()));
        }
        if (crmCustomerManagerForm.getId() == null){
            isAdd = true;
        }else {
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
        if (StrUtil.isNotBlank(crmCustomerManager.getManagerBusinessCode())){
            crmCustomerManagerForm.setBusinessTypesList(Arrays.asList(crmCustomerManager.getManagerBusinessCode().split(StrUtil.COMMA)));
        }else {
            crmCustomerManagerForm.setBusinessTypesList(new ArrayList<>());
        }
        return crmCustomerManagerForm;
    }

    @Override
    public BaseResult changeCustChangerManager(ComCustomerForm comCustomerForm) {
        Map<Long, CrmCustomer> custMap = new HashMap<>(16);
        List<Long> custList = new ArrayList<>();
        //新增
        List<Long> addList = new ArrayList<>();
        //删除
        List<Long> delList = new ArrayList<>();
        //错误
        List<Long> errList = new ArrayList<>();
        crmCustomerRiskService.checkIsRiskByCutsIds(comCustomerForm);
        if (CollUtil.isNotEmpty(comCustomerForm.getCrmCustomerList())){
            comCustomerForm.getCrmCustomerList().forEach(crmCustomer -> {
                custMap.put(crmCustomer.getId(),crmCustomer);
                custList.add(crmCustomer.getId());
            });
            Long currentUserId = CurrentUserUtil.getUserDetail().getId();
            CrmCustomerManager crmCustomerManager = new CrmCustomerManager();
            crmCustomerManager.setCustIdList(custList);
            List<CrmCustomerManager> managerList = selectList(crmCustomerManager);
            if (CollUtil.isNotEmpty(managerList)){
                managerList.forEach(x->{
                    if (comCustomerForm.getIsTranfer()){
                        if (currentUserId.equals(x.getManageUserId())){
                            addList.add(x.getCustId());
                            delList.add(x.getCustId());
                        }else {
                            errList.add(x.getCustId());
                        }
                    }else {
                        addList.add(x.getCustId());
                        delList.add(x.getCustId());
                    }

                });
            }
            if (CollUtil.isNotEmpty(delList)){
                delChargerManager(delList);
            }
            if (CollUtil.isNotEmpty(addList)){
                addChargerManager(addList,custMap,comCustomerForm.getChangerUserId(),comCustomerForm.getChangeUserName(),RoleCodeEnum.SALE_ROLE.getRoleCode(), RoleCodeEnum.SALE_ROLE.getRoleName());
                crmCustomerService.updateManagerMsg(addList,comCustomerForm.getChangerUserId(),comCustomerForm.getChangeUserName());
            }
        }
        if (CollUtil.isNotEmpty(errList)){
            List<CrmCustomer> errCustList = new ArrayList<>();
            errList.forEach(x->{
                errCustList.add(custMap.get(x));
            });
            comCustomerForm.setErrList(errCustList);
        }
        BaseResult errResult = crmCustomerService.getErrMsg(comCustomerForm);
        if (!errResult.isSuccess()){
            return errResult;
        }
        return BaseResult.ok();
    }

    /**
     * @description 根据客户id集合删除负责人
     * @author  ciro
     * @date   2022/3/4 16:24
     * @param: custIds
     * @return: void
     **/
    @Override
    public void delChargerManager(List<Long> custIds){
        this.baseMapper.delChargerManager(custIds,CurrentUserUtil.getUsername());
    }

    /**
     * @description 根据客户id集合新增负责人
     * @author  ciro
     * @date   2022/3/4 16:37
     * @param: custIdList   客户id集合
     * @param: custMap      客户信息对象
     * @param: managerUserId      负责人id
     * @param: managerUsername      负责人名称
     * @param: roleCodes            角色编码
     * @param: roleNames            角色名称
     * @return: void
     **/
    @Override
    public void addChargerManager(List<Long> custIdList, Map<Long, CrmCustomer> custMap, Long managerUserId, String managerUsername, String roleCodes, String roleNames){
        List<CrmCustomerManager> customerManagerList = new ArrayList<>();
        custIdList.forEach(custId->{
            CrmCustomerManager crmCustomerManager1 = new CrmCustomerManager();
            crmCustomerManager1.setCustId(custId);
            crmCustomerManager1.setManageUserId(managerUserId);
            crmCustomerManager1.setManageUsername(managerUsername);
            crmCustomerManager1.setManageRoles(roleCodes);
            crmCustomerManager1.setManagerRolesName(roleNames);
            String businessTypes = custMap.get(custId).getBusinessTypes();
            crmCustomerManager1.setManagerBusinessCode(businessTypes);
            if (StrUtil.isNotBlank(businessTypes)) {
                crmCustomerManager1.setManagerBusinessName(crmCustomerService.changeBusinessType(Arrays.asList(businessTypes.split(StrUtil.COMMA))));
            }
            crmCustomerManager1.setGenerateDate(LocalDateTime.now());
            crmCustomerManager1.setIsCharger(true);
            crmCustomerManager1.setIsSale(true);
            customerManagerList.add(crmCustomerManager1);
        });
        this.saveBatch(customerManagerList);
    }


    @Override
    public void getChangeCustManager(ComCustomerForm comCustomerForm) {
        List<Long> custIdList = comCustomerForm.getCrmCustomerList().stream().map(x->x.getId()).collect(Collectors.toList());
        Map<Long, CrmCustomer> custMap = comCustomerForm.getCrmCustomerList().stream().collect(Collectors.toMap(x->x.getId(),x->x));
        //负责人对象
        Map<Long, CrmCustomerManager> custManagerMap = new HashMap<>(16);
        //查询所有负责人
        CrmCustomerManager crmCustomerManager = new CrmCustomerManager();
        crmCustomerManager.setCustIdList(custIdList);
        crmCustomerManager.setIsCharger(true);
        List<CrmCustomerManager> managerList = selectList(crmCustomerManager);
        Long currentUserId = CurrentUserUtil.getUserDetail().getId();

        if (CollUtil.isNotEmpty(managerList)){
            custIdList = managerList.stream().map(x->x.getId()).collect(Collectors.toList());
            for (CrmCustomerManager manager : managerList){
                custManagerMap.put(manager.getCustId(),manager) ;
            }
        }
        List<CrmCustomer> changelList = new ArrayList<>();
        List<CrmCustomer> errList = new ArrayList<>();
        for (Long custId:custMap.keySet()){
            CrmCustomer crmCustomer = custMap.get(custId);
            if (custManagerMap.containsKey(custId)){
                if (custManagerMap.get(custId).getManageUserId().equals(currentUserId)){
                    changelList.add(crmCustomer);
                }else {
                    errList.add(crmCustomer);
                }
            }else {
                errList.add(crmCustomer);
            }
        }
        comCustomerForm.setChangeList(changelList);
        comCustomerForm.setErrList(errList);
    }

    @Override
    public void logicDelByCustIds(List<Long> custIds) {
        crmCustomerManagerMapper.logicDelByCustIds(custIds,CurrentUserUtil.getUsername());
    }


}
