package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.crm.model.form.CrmCustomerForm;
import lombok.extern.slf4j.Slf4j;
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
        crmCustomerForm.setBusinessTypes(StrUtil.join((CharSequence) crmCustomerForm.getBusinessTypesList(),StrUtil.COMMA));
        if (isAdd){
            this.save(crmCustomerForm);
        }else {
            this.updateById(crmCustomerForm);
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else {
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
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
                if (creditCodeList.size()>0){
                    return BaseResult.error(SysTips.NOT_ONE_DATA_ERROR);
                }
                if (crmCustomerForm.getId().equals(creditCodeList.get(0).getId())){
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
                if (custNameList.size()>0){
                    return BaseResult.error(SysTips.NOT_ONE_DATA_ERROR);
                }
                if (crmCustomerForm.getId().equals(custNameList.get(0).getId())){
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
