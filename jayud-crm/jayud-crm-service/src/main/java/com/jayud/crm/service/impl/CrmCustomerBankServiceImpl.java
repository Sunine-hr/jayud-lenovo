package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.crm.feign.AuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerBank;
import com.jayud.crm.mapper.CrmCustomerBankMapper;
import com.jayud.crm.service.ICrmCustomerBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 基本档案_客户_银行账户(crm_customer_bank) 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCustomerBankServiceImpl extends ServiceImpl<CrmCustomerBankMapper, CrmCustomerBank> implements ICrmCustomerBankService {


    @Autowired
    private CrmCustomerBankMapper crmCustomerBankMapper;
    @Autowired
    private AuthClient authClient;

    @Override
    public IPage<CrmCustomerBank> selectPage(CrmCustomerBank crmCustomerBank,
                                             Integer currentPage,
                                             Integer pageSize,
                                             HttpServletRequest req){

        Page<CrmCustomerBank> page=new Page<CrmCustomerBank>(currentPage,pageSize);
        IPage<CrmCustomerBank> pageList= crmCustomerBankMapper.pageList(page, crmCustomerBank);
        return pageList;
    }

    @Override
    public List<CrmCustomerBank> selectList(CrmCustomerBank crmCustomerBank){
        return crmCustomerBankMapper.list(crmCustomerBank);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmCustomerBankMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmCustomerBankMapper.logicDel(id,CurrentUserUtil.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelByIds(List<Long> ids) {
        Long aLong = ids.get(0);
        CrmCustomerBank byId = this.getById(aLong);
        authClient.addSysLogFeign(" 删除了银行账号", byId.getCustId());
        LambdaQueryWrapper<CrmCustomerBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CrmCustomerBank::getId,ids);
        List<CrmCustomerBank> bankList = this.list(lambdaQueryWrapper);
        List<Long> idList = new ArrayList<>();
        for (CrmCustomerBank bank : bankList){
            if (!bank.getIsDefault()){
                idList.add(bank.getId());
            }
        }
        if (CollUtil.isNotEmpty(idList)) {
            crmCustomerBankMapper.logicDelByIds(ids, CurrentUserUtil.getUsername());
        }
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerBankForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerBankForExcel(paramMap);
    }

    @Override
    public BaseResult<CrmCustomerBank> saveBank(CrmCustomerBank crmCustomerBank) {
        boolean isAdd = false;
        if (crmCustomerBank.getId() == null){
            isAdd = true;
        }
        if (crmCustomerBank.getIsDefault() != null) {
            if (crmCustomerBank.getIsDefault()) {
                changeOnlyDefault(isAdd, crmCustomerBank);
            }
        }
        if (isAdd){
            this.save(crmCustomerBank);
        }else {
            this.updateById(crmCustomerBank);
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }else {
            return BaseResult.ok(SysTips.EDIT_SUCCESS);
        }
    }

    @Override
    public void logicDelByCustIds(List<Long> custIds) {
        crmCustomerBankMapper.logicDelByCustIds(custIds,CurrentUserUtil.getUsername());
    }


    /**
     * @description 修改只能唯一默认账号
     * @author  ciro
     * @date   2022/3/2 14:30
     * @param: isAdd
     * @param: crmCustomerBank
     * @return: void
     **/
    private void changeOnlyDefault(boolean isAdd,CrmCustomerBank crmCustomerBank){
        LambdaQueryWrapper<CrmCustomerBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CrmCustomerBank::getIsDeleted,false);
        lambdaQueryWrapper.eq(CrmCustomerBank::getCustId,crmCustomerBank.getCustId());
        lambdaQueryWrapper.eq(CrmCustomerBank::getIsDefault,true);
        CrmCustomerBank customerBanks = this.getOne(lambdaQueryWrapper);
        if (customerBanks != null) {
            boolean isChange = false;
            if (isAdd) {
                isChange = true;

            }else {
                if (!crmCustomerBank.getId().equals(customerBanks.getId())){
                    isChange = true;
                }
            }
            if (isChange){
                customerBanks.setIsDefault(false);
                this.updateById(customerBanks);
            }
        }

    }

}
