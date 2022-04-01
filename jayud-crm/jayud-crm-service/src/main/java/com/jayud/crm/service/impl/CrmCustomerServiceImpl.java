package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.auth.model.po.SysUser;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.enums.RoleCodeEnum;
import com.jayud.common.utils.ListUtil;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.bo.*;
import com.jayud.crm.model.constant.CodeNumber;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.enums.CustRiskTypeEnum;
import com.jayud.crm.model.po.CrmCustomerManager;
import com.jayud.crm.model.po.CrmCustomerRelations;
import com.jayud.crm.model.po.CrmCustomerRisk;
import com.jayud.crm.model.vo.CrmCustomerVO;
import com.jayud.crm.service.*;
import com.jayud.crm.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomer;
import com.jayud.crm.mapper.CrmCustomerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基本档案_客户_基本信息(crm_customer) 服务实现类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Service
public class CrmCustomerServiceImpl extends ServiceImpl<CrmCustomerMapper, CrmCustomer> implements ICrmCustomerService {

    @Autowired
    private SysDictClient sysDictClient;
    @Autowired
    private AuthClient authClient;

    @Autowired
    private ICrmCustomerRiskService crmCustomerRiskService;

    @Autowired
    private ICrmCustomerManagerService crmCustomerManagerService;
    @Autowired
    private ICrmCustomerRelationsService crmCustomerRelationsService;
    @Autowired
    private ICrmCustomerBankService crmCustomerBankService;

    @Autowired
    private CodeUtils codeUtils;


    @Autowired
    private CrmCustomerMapper crmCustomerMapper;

    @Autowired
    public ICrmCustomerFeaturesService crmCustomerFeaturesService;

    @Override
    public IPage<CrmCustomerForm> selectPage(CrmCustomerForm crmCustomerForm,
                                             Integer currentPage,
                                             Integer pageSize,
                                             HttpServletRequest req) {
        crmCustomerForm.setTenantCode(CurrentUserUtil.getUserTenantCode());
        Page<CrmCustomerForm> page = new Page<CrmCustomerForm>(currentPage, pageSize);
        IPage<CrmCustomerForm> pageList = crmCustomerMapper.pageList(page, crmCustomerForm);
        if (CollUtil.isNotEmpty(pageList.getRecords())) {
            pageList.getRecords().forEach(crmCustomerForms -> {
//                crmCustomerForms.setCreateName(authClient.selectByUsername(crmCustomerForms.getCreateBy()).getResult().getUserName());
                if (StrUtil.isNotBlank(crmCustomerForms.getServiceType())) {
                    crmCustomerForms.setServiceTypeList(Arrays.asList(crmCustomerForms.getServiceType().split(StrUtil.COMMA)));
                } else {
                    crmCustomerForms.setServiceTypeList(new ArrayList<>());
                }
            });
        }

        return pageList;
    }

    @Override
    public List<CrmCustomerForm> selectList(CrmCustomerForm crmCustomerForm) {
        crmCustomerForm.setTenantCode(CurrentUserUtil.getUserTenantCode());
        return crmCustomerMapper.list(crmCustomerForm);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCustomerMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmCustomerMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerForExcel(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult saveCrmCustomer(CrmCustomerForm crmCustomerForm) {
        crmCustomerForm.setManagerUserId(crmCustomerForm.getFsalesId());
        boolean isAdd = false;
        if (crmCustomerForm.getId() == null) {
            isAdd = true;
        }
        BaseResult riskResult = crmCustomerRiskService.checkIsRisk(crmCustomerForm);
        if (!riskResult.isSuccess()) {
            return riskResult;
        }
        BaseResult onlyResult = checkOnly(isAdd, crmCustomerForm);
        if (onlyResult.isSuccess()) {
            crmCustomerForm.setIsCustAdd(isAdd);
            crmCustomerForm.setIsChangeBusniessType(false);
            crmCustomerForm.setBusinessTypesData(getBusinessTypeData());
        } else {
            return onlyResult;
        }
        if(null != crmCustomerForm.getFsalesId()){
            inteCustForm(crmCustomerForm);
        }

        if (CollUtil.isNotEmpty(crmCustomerForm.getServiceTypeList())) {
            crmCustomerForm.setServiceType(StringUtils.join(crmCustomerForm.getServiceTypeList(), StrUtil.COMMA));
        } else {
            crmCustomerForm.setServiceType(null);
        }
        if (CollUtil.isNotEmpty(crmCustomerForm.getBusinessTypesList())) {
            crmCustomerForm.setBusinessTypes(StringUtils.join(crmCustomerForm.getBusinessTypesList(), StrUtil.COMMA));
        } else {
            crmCustomerForm.setBusinessTypes(null);
        }
        if (isAdd) {
            if (crmCustomerForm.getIsPublic() != null) {
                if (crmCustomerForm.getIsPublic()) {
                    crmCustomerForm.setTransferPublicTime(LocalDateTime.now());
                }
            }
            //设置编码
            setCustCode(crmCustomerForm);
            this.save(crmCustomerForm);
            //创建客户创建业务要求默认数据
            crmCustomerFeaturesService.saveCrmCustomerFeatures(crmCustomerForm.getId());
        } else {
            CrmCustomer checkCust = this.getById(crmCustomerForm.getId());
            //业务类型不为空
            if (StrUtil.isNotBlank(checkCust.getBusinessTypes()) && StrUtil.isNotBlank(crmCustomerForm.getBusinessTypes())) {
                if (!ListUtil.checkIsSame(Arrays.asList(checkCust.getBusinessTypes().split(StrUtil.COMMA)), crmCustomerForm.getBusinessTypesList())) {
                    crmCustomerForm.setIsChangeBusniessType(true);
                }
            } else if (StrUtil.isBlank(checkCust.getBusinessTypes()) && StrUtil.isBlank(crmCustomerForm.getBusinessTypes())) {
                crmCustomerForm.setIsChangeBusniessType(false);
            } else {
                crmCustomerForm.setIsChangeBusniessType(true);
            }
            this.updateById(crmCustomerForm);
        }
        crmCustomerManagerService.saveByCustomer(crmCustomerForm);
        crmCustomerRelationsService.saveRelationByCustomer(crmCustomerForm);
        if (isAdd) {
            return BaseResult.ok(SysTips.ADD_SUCCESS, crmCustomerForm.getId());
        } else {
            return BaseResult.ok(SysTips.EDIT_SUCCESS, crmCustomerForm.getId());
        }
    }

    @Override
    public CrmCustomerForm selectById(Long id) {
        CrmCustomerForm crmCustomerForm = new CrmCustomerForm();
        crmCustomerForm.setId(id);
        List<CrmCustomerForm> crmCustomerFormList = selectList(crmCustomerForm);
        if (CollUtil.isNotEmpty(crmCustomerFormList)) {
            crmCustomerForm = crmCustomerFormList.get(0);
            if (StrUtil.isNotBlank(crmCustomerForm.getBusinessTypes())) {
                crmCustomerForm.setBusinessTypesList(Arrays.asList(crmCustomerForm.getBusinessTypes().split(StrUtil.COMMA)));
            } else {
                crmCustomerForm.setBusinessTypesList(new ArrayList<>());
            }
            if (StrUtil.isNotBlank(crmCustomerForm.getServiceType())) {
                crmCustomerForm.setServiceTypeList(Arrays.asList(crmCustomerForm.getServiceType().split(StrUtil.COMMA)));
            } else {
                crmCustomerForm.setServiceTypeList(new ArrayList<>());
            }
            setCustomerRelationsMsg(crmCustomerForm);
        }
        return crmCustomerForm;
    }

    @Override
    public CrmCodeFrom getCrmCode() {
        CrmCodeFrom crmCodeFrom = new CrmCodeFrom();
        //客户管理-客户状态
        BaseResult<List<SysDictItem>> custStatus = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_STATUS);
        crmCodeFrom.setCustStatus(custStatus.getResult());
        //客户管理-客户星级
        BaseResult<List<SysDictItem>> custStart = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_STAR);
        crmCodeFrom.setCustStar(custStart.getResult());
        //客户管理-客户审核状态
        BaseResult<List<SysDictItem>> custAuditStatus = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_AUDIT_ATATUS);
        crmCodeFrom.setCustAuditStatus(custAuditStatus.getResult());
        //客户管理-客户来源
        BaseResult<List<SysDictItem>> custSources = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_SOURCES);
        crmCodeFrom.setCustSources(custSources.getResult());
        //客户管理-所属行业
        BaseResult<List<SysDictItem>> custIndustry = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_INDUSTRY);
        crmCodeFrom.setCustIndustry(custIndustry.getResult());
        //客户管理-服务类型
        BaseResult<List<SysDictItem>> custServerType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_SERVER_TYPE);
        crmCodeFrom.setCustServerType(custServerType.getResult());
        //客户管理-对账方式
        BaseResult<List<SysDictItem>> custReconciliationMethod = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_RECONCILIATION_METHOD);
        crmCodeFrom.setCustReconciliationMethod(custReconciliationMethod.getResult());
        //客户管理-结算方式
        BaseResult<List<SysDictItem>> custSettlementMethod = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_SETTLEMENT_METHOD);
        crmCodeFrom.setCustSettlementMethod(custSettlementMethod.getResult());
        //客户管理-客户状态
        BaseResult<List<SysDictItem>> custNormalStatus = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_NORMAL_STATUS);
        crmCodeFrom.setCustNormalStatus(custNormalStatus.getResult());
        //客户管理-银行币别
        BaseResult<List<SysDictItem>> custBankCurrency = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BANK_CURRENCY);
        crmCodeFrom.setCustBankCurrency(custBankCurrency.getResult());
        //客户管理-业务类型
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BUSINESS_TYPE);
        crmCodeFrom.setCustBusinessType(custBusinessType.getResult());
        return crmCodeFrom;
    }

    @Override
    public BaseResult<CrmCodeFrom> getBbusinessTypesByCustId(Long custId) {
        CrmCodeFrom crmCodeFrom = new CrmCodeFrom();
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BUSINESS_TYPE);
        List<SysDictItem> sysDictItemList = custBusinessType.getResult();
        List<SysDictItem> returnList = new ArrayList<>();
        CrmCustomer crmCustomer = this.getById(custId);
        List<String> businessList = Arrays.asList(crmCustomer.getBusinessTypes().split(StrUtil.COMMA));
        sysDictItemList.forEach(x -> {
            if (businessList.contains(x.getItemValue())) {
                returnList.add(x);
            }
        });
        crmCodeFrom.setCustBusinessType(returnList);
        return BaseResult.ok(crmCodeFrom);
    }

    @Override
    public String getNextCode(String code) {
        BaseResult baseResult = authClient.getOrderFeign(code, new Date());
        return ((HashMap) baseResult.getResult()).get("order").toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult moveCustToRick(List<CrmCustomer> custList) {
        ComCustomerForm comCustomerForm = new ComCustomerForm();
        comCustomerForm.setCrmCustomerList(custList);
        crmCustomerManagerService.getChangeCustManager(comCustomerForm);
        List<CrmCustomerRisk> riskList = new ArrayList<>();
        if (CollUtil.isNotEmpty(comCustomerForm.getChangeList())) {
            for (CrmCustomer crmCustomer : comCustomerForm.getChangeList()) {
                CrmCustomerRisk crmCustomerRisk = new CrmCustomerRisk();
                crmCustomerRisk.setCustId(crmCustomer.getId());
                crmCustomerRisk.setCustName(crmCustomer.getCustName());
                crmCustomerRisk.setRiskType(CustRiskTypeEnum.BLACKLIST_CUST.getDesc());
                riskList.add(crmCustomerRisk);
            }
        }
        if (CollUtil.isNotEmpty(riskList)) {
            crmCustomerRiskService.saveBatch(riskList);
        }
        BaseResult errResult = getErrMsg(comCustomerForm);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        return BaseResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult changeToSupplier(List<CrmCustomer> custList) {
        ComCustomerForm comCustomerForm = new ComCustomerForm();
        comCustomerForm.setCrmCustomerList(custList);
        crmCustomerManagerService.getChangeCustManager(comCustomerForm);
        crmCustomerRiskService.checkIsRiskByCutsIds(comCustomerForm);

        if (CollUtil.isNotEmpty(comCustomerForm.getChangeList())) {
            List<Long> custIdList = comCustomerForm.getChangeList().stream().map(x -> x.getId()).collect(Collectors.toList());
            CrmCustomerForm crmCustomerForm = new CrmCustomerForm();
            crmCustomerForm.setCustIdList(custIdList);
            List<CrmCustomerForm> customerList = selectList(crmCustomerForm);
            List<CrmCustomer> changeList = customerList.stream().filter(x -> !x.getIsSupplier()).collect(Collectors.toList());
            List<CrmCustomer> supplierList = customerList.stream().filter(x -> x.getIsSupplier()).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(supplierList)){
                comCustomerForm.setExitSupplierList(supplierList);
            }

            if (CollUtil.isNotEmpty(changeList)) {
                List<CrmCustomer> crmCustomerList = tranferCust(changeList, false, true,false);
                crmCustomerList.forEach(crmCustomer -> {
                    crmCustomer.setSupplierCode(codeUtils.getCodeByRule(CodeNumber.CRM_SUPPLIER_CODE));
                });
                this.updateBatchById(crmCustomerList);
            }
        }

        BaseResult errResult = getErrMsg(comCustomerForm);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        return BaseResult.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult changeToPublic(List<CrmCustomer> custList) {
        ComCustomerForm comCustomerForm = new ComCustomerForm();
        comCustomerForm.setCrmCustomerList(custList);
        crmCustomerManagerService.getChangeCustManager(comCustomerForm);
        if (CollUtil.isNotEmpty(comCustomerForm.getChangeList())) {
            List<CrmCustomer> crmCustomerList = tranferCust(comCustomerForm.getChangeList(), true, false,false);
            this.updateBatchById(crmCustomerList);
        }
        BaseResult errResult = getErrMsg(comCustomerForm);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        return BaseResult.ok();
    }

    /**
     * @description 判断是否唯一
     * @author ciro
     * @date 2022/3/2 9:19
     * @param: isAdd
     * @param: crmCustomerForm
     * @return: com.jayud.common.BaseResult
     **/
    private BaseResult checkOnly(boolean isAdd, CrmCustomerForm crmCustomerForm) {
        CrmCustomer crmCustomer = new CrmCustomer();
        if (StrUtil.isNotBlank(crmCustomerForm.getUnCreditCode())) {
            crmCustomer.setUnCreditCode(crmCustomerForm.getUnCreditCode());
            LambdaQueryWrapper<CrmCustomer> creditCodeWrapper = initWrapper(crmCustomer);
            List<CrmCustomer> creditCodeList = this.list(creditCodeWrapper);
            if (isAdd) {
                if (CollUtil.isNotEmpty(creditCodeList)) {
                    return BaseResult.error(SysTips.CREDIT_CODE_EXIT_ERROR);
                }
            } else {
                if (CollUtil.isNotEmpty(creditCodeList)) {
                    if (creditCodeList.size() > 1) {
                        return BaseResult.error(SysTips.NOT_ONE_DATA_ERROR);
                    }
                    if (!crmCustomerForm.getId().equals(creditCodeList.get(0).getId())) {
                        return BaseResult.error(SysTips.CREDIT_CODE_EXIT_ERROR);
                    }
                }
            }
        }
        if (StrUtil.isNotBlank(crmCustomerForm.getCustName())) {
            if (crmCustomerForm.getCustName().length() > 50) {
                return BaseResult.error(SysTips.CUSTOMER_NAME_LONG_ERROR);
            }
            crmCustomer = new CrmCustomer();
            crmCustomer.setCustName(crmCustomerForm.getCustName());
            LambdaQueryWrapper<CrmCustomer> custNameWrapper = initWrapper(crmCustomer);
            List<CrmCustomer> custNameList = this.list(custNameWrapper);
            if (isAdd) {
                if (CollUtil.isNotEmpty(custNameList)) {
                    return BaseResult.error(SysTips.CUSTOMER_NAME_EXIT_ERROR);
                }
            } else {
                if (CollUtil.isNotEmpty(custNameList)) {
                    if (custNameList.size() > 1) {
                        return BaseResult.error(SysTips.NOT_ONE_DATA_ERROR);
                    }
                    if (!crmCustomerForm.getId().equals(custNameList.get(0).getId())) {
                        return BaseResult.error(SysTips.CUSTOMER_NAME_EXIT_ERROR);
                    }
                }
            }
        }
        return BaseResult.ok();
    }

    /**
     * @description 初始化查询条件
     * @author ciro
     * @date 2022/3/2 9:23
     * @param: crmCustomer
     * @return: com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.jayud.crm.model.po.CrmCustomer>
     **/
    private LambdaQueryWrapper<CrmCustomer> initWrapper(CrmCustomer crmCustomer) {
        LambdaQueryWrapper<CrmCustomer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(crmCustomer.getUnCreditCode())) {
            lambdaQueryWrapper.eq(CrmCustomer::getUnCreditCode, crmCustomer.getUnCreditCode());
        }
        if (StrUtil.isNotBlank(crmCustomer.getCustName())) {
            lambdaQueryWrapper.eq(CrmCustomer::getCustName, crmCustomer.getCustName());
        }
        return lambdaQueryWrapper;
    }

    /**
     * @description 填充业务类型名称
     * @author ciro
     * @date 2022/3/3 18:12
     * @param: crmCustomerForm
     * @return: void
     **/
    @Override
    public String changeBusinessType(List<String> businessTypesList) {
        String finalType = "";
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BUSINESS_TYPE);
        Map<String, String> typeMap = new HashMap<>(16);
        List<String> typeNamesList = new ArrayList<>();
        if (CollUtil.isNotEmpty(custBusinessType.getResult())) {
            custBusinessType.getResult().forEach(x -> {
                typeMap.put(x.getItemValue(), x.getItemText());
            });
        }
        if (!typeMap.isEmpty()) {
            businessTypesList.forEach(x -> {
                if (typeMap.containsKey(x)) {
                    typeNamesList.add(typeMap.get(x));
                }
            });
            if (CollUtil.isNotEmpty(typeNamesList)) {
                finalType = StringUtils.join(typeNamesList, StrUtil.C_COMMA);
            }
        }
        return finalType;
    }

    @Override
    public Map<String, String> getBusinessTypeData() {
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BUSINESS_TYPE);
        Map<String, String> typeMap = new HashMap<>(16);
        if (CollUtil.isNotEmpty(custBusinessType.getResult())) {
            custBusinessType.getResult().forEach(x -> {
                typeMap.put(x.getItemValue(), x.getItemText());
            });
        }
        return typeMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult receiveCustomer(ComCustomerForm comCustomerForm) {
        if (CollUtil.isNotEmpty(comCustomerForm.getCrmCustomerList())) {
            List<Long> custIdList = comCustomerForm.getCrmCustomerList().stream().map(x -> x.getId()).collect(Collectors.toList());
            CrmCustomerForm crmCustomerForm = new CrmCustomerForm();
            crmCustomerForm.setCustIdList(custIdList);
            List<CrmCustomerForm> customerList = selectList(crmCustomerForm);
            custIdList = customerList.stream().map(x -> x.getId()).collect(Collectors.toList());
            Map<Long, CrmCustomer> custMap = customerList.stream().filter(x -> x.getIsPublic()).collect(Collectors.toMap(x -> x.getId(), x -> x));
            //取消放入公海
            AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
            cnaleInpublicByIds(custIdList, authUserDetail.getId(), authUserDetail.getUsername());
            //删除负责人
            crmCustomerManagerService.delChargerManager(custIdList);
            //添加负责人
            crmCustomerManagerService.addChargerManager(custIdList, custMap, authUserDetail.getId(), authUserDetail.getUsername(), RoleCodeEnum.SALE_ROLE.getRoleCode(), RoleCodeEnum.SALE_ROLE.getRoleName());
        }
        return BaseResult.ok();
    }

    /**
     * @description 设置联系人
     * @author ciro
     * @date 2022/3/4 14:31
     * @param: crmCustomerForm
     * @return: void
     **/
    private void setCustomerRelationsMsg(CrmCustomerForm crmCustomerForm) {
        CrmCustomerRelations crmCustomerRelations = new CrmCustomerRelations();
        crmCustomerRelations.setCustId(crmCustomerForm.getId());
        crmCustomerRelations.setIsDefault(true);
        List<CrmCustomerRelations> relationsList = crmCustomerRelationsService.selectList(crmCustomerRelations);
        if (CollUtil.isNotEmpty(relationsList)) {
            crmCustomerRelations = relationsList.get(0);
            crmCustomerForm.setCustRelationId(crmCustomerRelations.getId());
            crmCustomerForm.setCustRelationUsername(crmCustomerRelations.getContactName());
            crmCustomerForm.setCustRelationPhone(crmCustomerRelations.getMobile());
            crmCustomerForm.setCustRelationPostName(crmCustomerRelations.getPostName());
        }
    }

    /**
     * @description 获取错误信息
     * @author ciro
     * @date 2022/3/4 18:34
     * @param: comCustomerForm
     * @return: com.jayud.common.BaseResult
     **/
    @Override
    public BaseResult getErrMsg(ComCustomerForm comCustomerForm) {
        String errString = "";
        if (CollUtil.isNotEmpty(comCustomerForm.getErrList())) {
            List<String> errList = comCustomerForm.getErrList().stream().map(x -> x.getCustName()).collect(Collectors.toList());
            errString = StringUtils.join(errList, StrUtil.C_COMMA) + " 修改失败，请联系负责人修改！";
        }
        if (CollUtil.isNotEmpty(comCustomerForm.getRiskList())) {
            List<String> riskList = comCustomerForm.getRiskList().stream().map(x -> x.getCustName()).collect(Collectors.toList());
            errString = StringUtils.join(riskList, StrUtil.C_COMMA) + " 已存在黑名单，请联系负责人修改！";
        }
        if (CollUtil.isNotEmpty(comCustomerForm.getExitSupplierList())) {
            List<String> supplierList = comCustomerForm.getExitSupplierList().stream().map(x -> x.getCustName()).collect(Collectors.toList());
            errString = StringUtils.join(supplierList, StrUtil.C_COMMA) + " 已转为供应商，请勿重复操作！";
        }
        if (CollUtil.isNotEmpty(comCustomerForm.getExitCustList())) {
            List<String> supplierList = comCustomerForm.getExitSupplierList().stream().map(x -> x.getCustName()).collect(Collectors.toList());
            errString = StringUtils.join(supplierList, StrUtil.C_COMMA) + " 已转为客户，请勿重复操作！";
        }
        if (StringUtils.isNotBlank(errString)) {
            return BaseResult.error(errString);
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult checkCustomerMsg(CrmCustomerCheckForm crmCustomerCheckForm) {
        boolean isAdd = false;
        if (crmCustomerCheckForm.getId() == null) {
            isAdd = true;
        }
        CrmCustomerForm crmCustomerForm = new CrmCustomerForm();
        BeanUtils.copyProperties(crmCustomerCheckForm, crmCustomerForm);
        return checkOnly(isAdd, crmCustomerForm);
    }

    @Override
    public BaseResult logicDelByIds(DeleteForm deleteForm) {
        Long aLong = deleteForm.getIds().get(0);
        authClient.addSysLogFeign("删除了客户", aLong);
        if (CollUtil.isNotEmpty(deleteForm.getIds())) {
            crmCustomerMapper.logicDelByIds(deleteForm.getIds(), CurrentUserUtil.getUsername());
            crmCustomerManagerService.logicDelByCustIds(deleteForm.getIds());
            crmCustomerBankService.logicDelByCustIds(deleteForm.getIds());
        }
        return BaseResult.ok();
    }

    @Override
    public void updateManagerMsg(List<Long> idList, Long managerUserId, String managerUserName) {
        this.baseMapper.updateManagerMsg(idList, managerUserId, managerUserName, CurrentUserUtil.getUsername());
    }

    @Override
    public void cnaleInpublicByIds(List<Long> ids, Long managerUserId, String managerUsername) {
        this.baseMapper.cnaleInpublicByIds(ids, managerUserId, managerUsername, CurrentUserUtil.getUsername());
    }

    @Override
    public IPage selectCrmCustomerPage(CrmCustomerForm crmCustomerForm, Integer currentPage, Integer pageSize, HttpServletRequest req) {
        crmCustomerForm.setTenantCode(CurrentUserUtil.getUserTenantCode());
        Page<CrmCustomerVO> page = new Page<CrmCustomerVO>(currentPage, pageSize);
        IPage<CrmCustomerVO> iPage = crmCustomerMapper.selectCrmCustomerPage(page, crmCustomerForm);
        for (CrmCustomerVO record : iPage.getRecords()) {
            //设置联系人
            CrmCustomerRelations crmCustomerRelations = new CrmCustomerRelations();
            crmCustomerRelations.setCustId(crmCustomerForm.getId());
            crmCustomerRelations.setIsDefault(true);
            List<CrmCustomerRelations> relationsList = crmCustomerRelationsService.selectList(crmCustomerRelations);
            if (CollUtil.isNotEmpty(relationsList)){
                crmCustomerRelations = relationsList.get(0);
                record.setCustRelationUsername(crmCustomerRelations.getContactName());
                record.setCustRelationPostName(crmCustomerRelations.getPostName());
            }

        }
        return iPage;

    }

    @Override
    public List<CrmCustomerVO> selectCrmCustomerList(CrmCustomerForm crmCustomerForm) {
        crmCustomerForm.setTenantCode(CurrentUserUtil.getUserTenantCode());
        List<CrmCustomerVO> crmCustomerVOS = crmCustomerMapper.selectCrmCustomerList(crmCustomerForm);

        return crmCustomerVOS;
    }

    @Override
    public BaseResult changeToCust(List<CrmCustomer> custList) {
        ComCustomerForm comCustomerForm = new ComCustomerForm();
        comCustomerForm.setCrmCustomerList(custList);
        crmCustomerManagerService.getChangeCustManager(comCustomerForm);
        crmCustomerRiskService.checkIsRiskByCutsIds(comCustomerForm);

        if (CollUtil.isNotEmpty(comCustomerForm.getChangeList())) {
            List<Long> custIdList = comCustomerForm.getChangeList().stream().map(x -> x.getId()).collect(Collectors.toList());
            CrmCustomerForm crmCustomerForm = new CrmCustomerForm();
            crmCustomerForm.setCustIdList(custIdList);
            List<CrmCustomerForm> customerList = selectList(crmCustomerForm);
            List<CrmCustomer> changeList = customerList.stream().filter(x -> !x.getIsCust()).collect(Collectors.toList());
            List<CrmCustomer> supplierList = customerList.stream().filter(x -> x.getIsCust()).collect(Collectors.toList());
            comCustomerForm.setExitCustList(supplierList);
            if (CollUtil.isNotEmpty(changeList)) {
                List<CrmCustomer> crmCustomerList = tranferCust(changeList, false, false,true);
                crmCustomerList.forEach(crmCustomer -> {
                    crmCustomer.setSupplierCode(codeUtils.getCodeByRule(CodeNumber.CRM_SUPPLIER_CODE));
                });
                this.updateBatchById(crmCustomerList);
            }
        }

        BaseResult errResult = getErrMsg(comCustomerForm);
        if (!errResult.isSuccess()) {
            return errResult;
        }
        return BaseResult.ok();
    }

    @Override
    public CrmCustomer getCustomerByCustomerName(String custName) {
        QueryWrapper<CrmCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmCustomer::getCustName,custName);
        queryWrapper.lambda().eq(CrmCustomer::getIsDeleted,0);
        queryWrapper.lambda().eq(CrmCustomer::getIsCust,1);
        return this.getOne(queryWrapper);
    }

    @Override
    public CrmCustomer getCustomerByUnCreditCode(String unCreditCode) {
        QueryWrapper<CrmCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmCustomer::getUnCreditCode,unCreditCode);
        queryWrapper.lambda().eq(CrmCustomer::getIsDeleted,0);
        queryWrapper.lambda().eq(CrmCustomer::getIsCust,1);
        return this.getOne(queryWrapper);
    }

    @Override
    public CrmCustomer getCustomerBySupplierName(String custName) {
        QueryWrapper<CrmCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmCustomer::getCustName,custName);
        queryWrapper.lambda().eq(CrmCustomer::getIsDeleted,0);
        queryWrapper.lambda().eq(CrmCustomer::getIsSupplier,1);
        return this.getOne(queryWrapper);
    }

    @Override
    public CrmCustomer getCustomerBySupplierUnCreditCode(String unCreditCode) {
        QueryWrapper<CrmCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmCustomer::getUnCreditCode,unCreditCode);
        queryWrapper.lambda().eq(CrmCustomer::getIsDeleted,0);
        queryWrapper.lambda().eq(CrmCustomer::getIsSupplier,1);
        return this.getOne(queryWrapper);
    }

    /**
     * @description 初始用户数据
     * @author ciro
     * @date 2022/3/7 14:02
     * @param: crmCustomerForm
     * @return: void
     **/
    private void inteCustForm(CrmCustomerForm crmCustomerForm) {
        BaseResult<SysUser> userBaseResult = authClient.selectByUserId(crmCustomerForm.getFsalesId());
        if (userBaseResult.isSuccess()) {
            crmCustomerForm.setManagerUsername(userBaseResult.getResult().getUserName());
            crmCustomerForm.setFsalesName(userBaseResult.getResult().getUserName());
        }
    }

    /**
     * @description 转换用户数据(转公海 、 供应商)
     * @author ciro
     * @date 2022/3/7 17:42
     * @param: crmCustomerList
     * @param: isPublic
     * @param: isSupplier
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomer>
     **/
    private List<CrmCustomer> tranferCust(List<CrmCustomer> crmCustomerList, boolean isPublic, boolean isSupplier,boolean isCust) {
        List<Long> ids = crmCustomerList.stream().map(x -> x.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<CrmCustomer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CrmCustomer::getId, ids);
        List<CrmCustomer> customerList = this.list(lambdaQueryWrapper);
        List<CrmCustomer> crmCustomerArrayList = new ArrayList<>();
        for (CrmCustomer crmCustomer : customerList) {
            if (isPublic) {
                if (!crmCustomer.getIsPublic()) {
                    crmCustomer.setIsPublic(true);
                    crmCustomer.setRegDate(LocalDateTime.now());
                    crmCustomerArrayList.add(crmCustomer);
                }
            }
            if (isSupplier) {
                if (!crmCustomer.getIsSupplier()) {
                    crmCustomer.setIsSupplier(true);
                    crmCustomerArrayList.add(crmCustomer);
                }
            }
            if (isCust) {
                if (!crmCustomer.getIsCust()) {
                    crmCustomer.setIsCust(true);
                    crmCustomerArrayList.add(crmCustomer);
                }
            }
        }
        return crmCustomerArrayList;
    }


    private void setCustCode(CrmCustomerForm crmCustomerForm) {
        BaseResult baseResult = authClient.getOrderFeign(CodeNumber.CRM_CUST_CODE, new Date());
        if (baseResult.isSuccess()) {
            HashMap data = (HashMap) baseResult.getResult();
            crmCustomerForm.setCustCode(data.get("order").toString());
            crmCustomerForm.setFLevel(Integer.parseInt(data.get("fLevel").toString()));
            crmCustomerForm.setFStep(Integer.parseInt(data.get("fStep").toString()));
            crmCustomerForm.setCheckStateFlag(data.get("checkStateFlag").toString());
        }
        //设置供应商编码
        if (crmCustomerForm.getIsSupplier() != null) {
            if (crmCustomerForm.getIsSupplier()) {
                crmCustomerForm.setSupplierCode(codeUtils.getCodeByRule(CodeNumber.CRM_SUPPLIER_CODE));
            }
        }
    }

}
