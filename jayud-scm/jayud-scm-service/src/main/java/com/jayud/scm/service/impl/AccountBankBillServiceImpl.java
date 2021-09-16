package com.jayud.scm.service.impl;

import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddAccountBankBillForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.AccountBankBillMapper;
import com.jayud.scm.model.vo.AccountBankBillVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.utils.DateUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 水单主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Service
public class AccountBankBillServiceImpl extends ServiceImpl<AccountBankBillMapper, AccountBankBill> implements IAccountBankBillService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IAccountBankBillFollowService accountBankBillFollowService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IAccountBankBillEntryService accountBankBillEntryService;

    @Autowired
    private ICustomerService customerService;

    @Override
    public AccountBankBillVO getAccountBankBillById(Integer id) {
        AccountBankBill accountBankBill = this.getById(id);
        AccountBankBillVO accountBankBillVO = ConvertUtil.convert(accountBankBill, AccountBankBillVO.class);
        return accountBankBillVO;
    }

    @Override
    public boolean saveOrUpdateAccountBankBill(AddAccountBankBillForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AccountBankBillFollow accountBankBillFollow = new AccountBankBillFollow();
        AccountBankBill accountBankBill = ConvertUtil.convert(form, AccountBankBill.class);
        if(form.getId() != null){
            accountBankBill.setMdyBy(systemUser.getId().intValue());
            accountBankBill.setMdyByDtm(LocalDateTime.now());
            accountBankBill.setMdyByName(systemUser.getUserName());
            accountBankBillFollow.setSType(OperationEnum.UPDATE.getCode());
            accountBankBillFollow.setFollowContext("修改水单信息，水单编号为"+accountBankBill.getBankBillNo());
        }else{
            accountBankBill.setBankBillNo(commodityService.getOrderNo(NoCodeEnum.ACCOUNT_BANK_BILL.getCode(),LocalDateTime.now()));
            accountBankBill.setCrtBy(systemUser.getId().intValue());
            accountBankBill.setCrtByDtm(LocalDateTime.now());
            accountBankBill.setCrtByName(systemUser.getUserName());
            accountBankBillFollow.setSType(OperationEnum.INSERT.getCode());
            accountBankBillFollow.setFollowContext("新增水单信息，水单编号为"+accountBankBill.getBankBillNo());
        }
        boolean result = this.saveOrUpdate(accountBankBill);
        if(result){
            log.warn("新增或修改水单信息成功");
            accountBankBillFollow.setBankBillId(accountBankBill.getId());
            accountBankBillFollow.setCrtBy(systemUser.getId().intValue());
            accountBankBillFollow.setCrtByDtm(LocalDateTime.now());
            accountBankBillFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.accountBankBillFollowService.save(accountBankBillFollow);
            if(save){
                log.warn("新增或修改，操作记录新增成功");
            }
        }
        return result;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<AccountBankBillEntry> accountBankBillEntries = new ArrayList<>();
        List<AccountBankBillFollow> accountBankBillFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            List<AccountBankBillEntry> accountBankBillEntries1 = accountBankBillEntryService.getListByAccountBankBillId(id);
            for (AccountBankBillEntry accountBankBillEntry : accountBankBillEntries1) {
                accountBankBillEntry.setVoidedBy(deleteForm.getId().intValue());
                accountBankBillEntry.setVoidedByDtm(deleteForm.getDeleteTime());
                accountBankBillEntry.setVoidedByName(deleteForm.getName());
                accountBankBillEntry.setVoided(1);
                accountBankBillEntries.add(accountBankBillEntry);
            }

            AccountBankBillFollow accountBankBillFollow = new AccountBankBillFollow();
            accountBankBillFollow.setBankBillId(id.intValue());
            accountBankBillFollow.setSType(OperationEnum.DELETE.getCode());
            accountBankBillFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            accountBankBillFollow.setCrtBy(deleteForm.getId().intValue());
            accountBankBillFollow.setCrtByDtm(deleteForm.getDeleteTime());
            accountBankBillFollow.setCrtByName(deleteForm.getName());
            accountBankBillFollows.add(accountBankBillFollow);
        }
        //删除水单明细
        boolean update = this.accountBankBillEntryService.updateBatchById(accountBankBillEntries);
        if(!update){
            log.warn("删除水单详情失败");
        }

        boolean b1 = this.accountBankBillFollowService.saveBatch(accountBankBillFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+accountBankBillFollows);
        }
        return b1;
    }

    @Override
    public CommonResult reviewWaterBill(PermissionForm form) {

        AccountBankBillFollow accountBankBillFollow = new AccountBankBillFollow();
        accountBankBillFollow.setBankBillId(form.getId().intValue());
        accountBankBillFollow.setSType(OperationEnum.UPDATE.getCode());
        accountBankBillFollow.setCrtBy(form.getId().intValue());
        accountBankBillFollow.setCrtByDtm(LocalDateTime.now());
        accountBankBillFollow.setCrtByName(form.getUserName());

        CommonResult commonResult = customerService.toExamine(form);
        if(commonResult.getCode().equals(0)){
            accountBankBillFollow.setFollowContext("水单审核成功");
        }else {
            accountBankBillFollow.setFollowContext("水单审核失败");
        }
        boolean save = this.accountBankBillFollowService.save(accountBankBillFollow);
        if(save){
            log.warn("审核，操作日志添加成功");
        }
        return commonResult;
    }

    @Override
    public CommonResult reverseReviewWaterBill(PermissionForm form) {
        AccountBankBillFollow accountBankBillFollow = new AccountBankBillFollow();
        accountBankBillFollow.setBankBillId(form.getId().intValue());
        accountBankBillFollow.setSType(OperationEnum.UPDATE.getCode());
        accountBankBillFollow.setCrtBy(form.getId().intValue());
        accountBankBillFollow.setCrtByDtm(LocalDateTime.now());
        accountBankBillFollow.setCrtByName(form.getUserName());

        CommonResult commonResult = customerService.deApproval(form);
        if(commonResult.getCode().equals(0)){
            accountBankBillFollow.setFollowContext("水单反审成功");
        }else {
            accountBankBillFollow.setFollowContext("水单反审失败");
        }
        boolean save = this.accountBankBillFollowService.save(accountBankBillFollow);
        if(save){
            log.warn("反审，操作日志添加成功");
        }
        return commonResult;
    }

    @Override
    public boolean arrival(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AccountBankBill accountBankBill = this.getById(form.getId());

        if(accountBankBill.getVerificationMoney() == null || accountBankBill.getVerificationMoney().compareTo(new BigDecimal(0)) < 1 || accountBankBill.getBillArMoney().compareTo(accountBankBill.getVerificationMoney()) ==0){
            accountBankBill.setVerificationMoney(form.getBillArMoney());
        }

        accountBankBill.setBillArMoney(form.getBillArMoney());
        accountBankBill.setAccountDate(DateUtil.stringToLocalDateTime(form.getAccountDate(),"yyyy-MM-dd HH:mm:ss"));
        accountBankBill.setMdyBy(form.getId().intValue());
        accountBankBill.setMdyByDtm(LocalDateTime.now());
        accountBankBill.setMdyByName(systemUser.getUserName());

        boolean result = this.updateById(accountBankBill);
        if(result){
            log.warn("到账成功");
            AccountBankBillFollow accountBankBillFollow = new AccountBankBillFollow();
            accountBankBillFollow.setBankBillId(form.getId().intValue());
            accountBankBillFollow.setSType(OperationEnum.UPDATE.getCode());
            accountBankBillFollow.setFollowContext("到账成功到账金额"+form.getBillArMoney());
            accountBankBillFollow.setCrtBy(form.getId().intValue());
            accountBankBillFollow.setCrtByDtm(LocalDateTime.now());
            accountBankBillFollow.setCrtByName(systemUser.getUserName());
            boolean save = this.accountBankBillFollowService.save(accountBankBillFollow);
            if(save){
                log.warn("到账，操作日志添加成功");
            }
        }

        return result;
    }
}
