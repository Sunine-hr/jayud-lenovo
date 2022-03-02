package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.form.CrmCodeFrom;
import com.jayud.crm.model.form.CrmCustomerForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomer;
import com.jayud.crm.mapper.CrmCustomerMapper;
import com.jayud.crm.service.ICrmCustomerService;
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
    private CrmCustomerMapper crmCustomerMapper;

    @Override
    public IPage<CrmCustomer> selectPage(CrmCustomer crmCustomer,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmCustomer> page=new Page<CrmCustomer>(currentPage,pageSize);
        IPage<CrmCustomer> pageList= crmCustomerMapper.pageList(page, crmCustomer);
        return pageList;
    }

    @Override
    public List<CrmCustomer> selectList(CrmCustomer crmCustomer){
        return crmCustomerMapper.list(crmCustomer);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerForExcel(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult saveCrmCustomer(CrmCustomerForm crmCustomerForm) {
        boolean isAdd = false;
        if (crmCustomerForm.getId() == null){
            isAdd = true;
        }
        BaseResult onlyResult = checkOnly(isAdd,crmCustomerForm);
        if (!onlyResult.isSuccess()){
            return onlyResult;
        }
        if (CollUtil.isNotEmpty(crmCustomerForm.getBusinessTypesList())) {
            crmCustomerForm.setBusinessTypes(StringUtils.join(crmCustomerForm.getBusinessTypesList(),StrUtil.C_COMMA));
        }
        if (isAdd){
            this.save(crmCustomerForm);
        }else {
            this.updateById(crmCustomerForm);
        }
        if (isAdd){
            return BaseResult.ok(crmCustomerForm.getId());
        }else {
            return BaseResult.ok(crmCustomerForm.getId());
        }
    }

    @Override
    public CrmCustomerForm selectById(Long id) {
        CrmCustomerForm crmCustomerForm = new CrmCustomerForm();
        CrmCustomer crmCustomer = this.getById(id);
        BeanUtils.copyProperties(crmCustomer,crmCustomerForm);
        crmCustomerForm.setBusinessTypesList(Arrays.asList(crmCustomer.getBusinessTypes().split(StrUtil.COMMA)));
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
        crmCodeFrom.setCustIndustry(custServerType.getResult());
//        //客户管理-对账方式
//        BaseResult<List<SysDictItem>> custReconciliationMethod = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_RECONCILIATION_METHOD);
//        crmCodeFrom.setCustIndustry(custReconciliationMethod.getResult());
//        //客户管理-结算方式
//        BaseResult<List<SysDictItem>> custSettlementMethod = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_SETTLEMENT_METHOD);
//        crmCodeFrom.setCustIndustry(custSettlementMethod.getResult());
//        //客户管理-客户状态
//        BaseResult<List<SysDictItem>> custNormalStatus= sysDictClient.selectItemByDictCode(CrmDictCode.CUST_NORMAL_STATUS);
//        crmCodeFrom.setCustIndustry(custNormalStatus.getResult());
//        //客户管理-银行币别
//        BaseResult<List<SysDictItem>> custBankCurrency= sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BANK_CURRENCY);
//        crmCodeFrom.setCustBankCurrency(custBankCurrency.getResult());
        //客户管理-银行币别
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BUSINESS_TYPE);
        crmCodeFrom.setCustBusinessType(custBusinessType.getResult());
        return crmCodeFrom;
    }

    /**
     * @description 判断是否唯一
     * @author  ciro
     * @date   2022/3/2 9:19
     * @param: isAdd
     * @param: crmCustomerForm
     * @return: com.jayud.common.BaseResult
     **/
    private BaseResult checkOnly(boolean isAdd ,CrmCustomerForm crmCustomerForm){
        CrmCustomer crmCustomer = new CrmCustomer();
        crmCustomer.setUnCreditCode(crmCustomerForm.getUnCreditCode());
        LambdaQueryWrapper<CrmCustomer> creditCodeWrapper = initWrapper(crmCustomer);
        List<CrmCustomer> creditCodeList = this.list(creditCodeWrapper);
        if (isAdd){
            if (CollUtil.isNotEmpty(creditCodeList)){
                return BaseResult.error(SysTips.CREDIT_CODE_EXIT_ERROR);
            }
        }else {
            if (CollUtil.isNotEmpty(creditCodeList)){
                if (creditCodeList.size()>1){
                    return BaseResult.error(SysTips.NOT_ONE_DATA_ERROR);
                }
                if (!crmCustomerForm.getId().equals(creditCodeList.get(0).getId())){
                    return BaseResult.error(SysTips.CREDIT_CODE_EXIT_ERROR);
                }
            }
        }
        crmCustomer = new CrmCustomer();
        crmCustomer.setCustName(crmCustomerForm.getCustName());
        LambdaQueryWrapper<CrmCustomer> custNameWrapper = initWrapper(crmCustomer);
        List<CrmCustomer> custNameList = this.list(custNameWrapper);
        if (isAdd){
            if (CollUtil.isNotEmpty(custNameList)){
                return BaseResult.error(SysTips.CUSTOMER_NAME_EXIT_ERROR);
            }
        }else {
            if (CollUtil.isNotEmpty(custNameList)){
                if (custNameList.size()>1){
                    return BaseResult.error(SysTips.NOT_ONE_DATA_ERROR);
                }
                if (!crmCustomerForm.getId().equals(custNameList.get(0).getId())){
                    return BaseResult.error(SysTips.CUSTOMER_NAME_EXIT_ERROR);
                }
            }
        }

        return BaseResult.ok();
    }

    /**
     * @description 初始化查询条件
     * @author  ciro
     * @date   2022/3/2 9:23
     * @param: crmCustomer
     * @return: com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.jayud.crm.model.po.CrmCustomer>
     **/
    private LambdaQueryWrapper<CrmCustomer> initWrapper(CrmCustomer crmCustomer){
        LambdaQueryWrapper<CrmCustomer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(crmCustomer.getUnCreditCode())){
            lambdaQueryWrapper.eq(CrmCustomer::getUnCreditCode,crmCustomer.getUnCreditCode());
        }
        if (StrUtil.isNotBlank(crmCustomer.getCustName())){
            lambdaQueryWrapper.eq(CrmCustomer::getCustName,crmCustomer.getCustName());
        }
        return lambdaQueryWrapper;
    }

}
