package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.AcctPayMapper;
import com.jayud.scm.model.vo.AcctPayVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 付款单主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class AcctPayServiceImpl extends ServiceImpl<AcctPayMapper, AcctPay> implements IAcctPayService {

    @Autowired
    private IAcctPayFollowService acctPayFollowService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IAcctPayEntryService acctPayEntryService;

    @Autowired
    private ICustomerService customerService;

    @Override
    public boolean generatePaymentDocument(AddAcctPayReceiptForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctPay acctPay = ConvertUtil.convert(form, AcctPay.class);
        acctPay.setPayNo(commodityService.getOrderNo(NoCodeEnum.ACCT_PAY.getCode(), LocalDateTime.now()));
        acctPay.setId(null);
        acctPay.setPayType("T/T");
        acctPay.setCustomerId(form.getSupplierId());
        acctPay.setCustomerName(form.getSupplierName());
        acctPay.setSupplierId(form.getSupplierId());
        acctPay.setSupplierName(form.getSupplierName());
        acctPay.setPayDate(LocalDateTime.now());
        acctPay.setCrtBy(systemUser.getId().intValue());
        acctPay.setCrtByDtm(LocalDateTime.now());
        acctPay.setCrtByName(systemUser.getUserName());
//        if(form.getProxyMoney() != null){
//            acctPay.setApMoney(acctPay.getApMoney().subtract(form.getProxyMoney()));
//        }
        if(form.getPayType() != null && form.getPayType().equals("预收")){
            acctPay.setIsYuFee(1);
        }
        boolean save = this.save(acctPay);
        if(save){
            log.warn("生成付款单成功");
            AcctPayFollow acctPayFollow = new AcctPayFollow();
            acctPayFollow.setCrtBy(systemUser.getId().intValue());
            acctPayFollow.setCrtByDtm(LocalDateTime.now());
            acctPayFollow.setCrtByName(systemUser.getUserName());
            acctPayFollow.setSType(OperationEnum.INSERT.getCode());
            acctPayFollow.setFollowContext(systemUser.getUserName()+"生成付款单成功");
            acctPayFollow.setPayId(acctPay.getId());
            boolean save1 = this.acctPayFollowService.save(acctPayFollow);
            if(save1){
                log.warn("生成付款单，操作记录添加成功");
            }
        }

        return save;
    }

    @Override
    public AcctPayVO getAcctPayById(Integer id) {
        AcctPay acctPay = this.getById(id);
        AcctPayVO acctPayVO = ConvertUtil.convert(acctPay, AcctPayVO.class);
        return acctPayVO;
    }

    @Override
    public boolean saveOrUpdateAcctPay(AddAcctPayForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctPayFollow acctPayFollow = new AcctPayFollow();
        AcctPay acctPay = ConvertUtil.convert(form, AcctPay.class);
        if(form.getId() != null){
            acctPay.setMdyBy(systemUser.getId().intValue());
            acctPay.setMdyByDtm(LocalDateTime.now());
            acctPay.setMdyByName(systemUser.getUserName());
            acctPayFollow.setSType(OperationEnum.UPDATE.getCode());
            acctPayFollow.setFollowContext(systemUser.getUserName()+"修改付款单信息");
        }else{
            acctPay.setPayNo(commodityService.getOrderNo(NoCodeEnum.ACCT_PAY.getCode(),LocalDateTime.now()));
            acctPay.setCrtBy(systemUser.getId().intValue());
            acctPay.setCrtByDtm(LocalDateTime.now());
            acctPay.setCrtByName(systemUser.getUserName());
            acctPayFollow.setSType(OperationEnum.INSERT.getCode());
            acctPayFollow.setFollowContext(systemUser.getUserName()+"新增付款单信息");
        }
        boolean result = this.saveOrUpdate(acctPay);
        if(result){
            log.warn("新增或修改付款单成功");
            acctPayFollow.setCrtBy(systemUser.getId().intValue());
            acctPayFollow.setCrtByDtm(LocalDateTime.now());
            acctPayFollow.setCrtByName(systemUser.getUserName());
            acctPayFollow.setPayId(acctPay.getId());
            boolean save = this.acctPayFollowService.save(acctPayFollow);
            if(save){
                log.warn("新增或修改付款单,操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<AcctPayEntry> acctPayEntries = new ArrayList<>();
        List<AcctPayFollow> acctPayFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {

            List<AcctPayEntry> acctPayEntries1 = acctPayEntryService.getListByAcctPayId(id);
            for (AcctPayEntry acctPayEntry : acctPayEntries1) {
                acctPayEntry.setPayId(null);
                acctPayEntries.add(acctPayEntry);
            }

            AcctPayFollow acctPayFollow = new AcctPayFollow();
            acctPayFollow.setPayId(id.intValue());
            acctPayFollow.setSType(OperationEnum.DELETE.getCode());
            acctPayFollow.setFollowContext(OperationEnum.DELETE.getDesc()+id);
            acctPayFollow.setCrtBy(deleteForm.getId().intValue());
            acctPayFollow.setCrtByDtm(deleteForm.getDeleteTime());
            acctPayFollow.setCrtByName(deleteForm.getName());
            acctPayFollows.add(acctPayFollow);
        }
        //修改应收款单明细
        boolean update = this.acctPayEntryService.updateBatchById(acctPayEntries);
        if(!update){
            log.warn("删除付款单详情失败");
        }

        boolean b1 = this.acctPayFollowService.saveBatch(acctPayFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+acctPayFollows);
        }
        return b1;
    }

    @Override
    public AcctPay getAcctPayByPayToMeId(Integer id) {
        QueryWrapper<AcctPay> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AcctPay::getPayToMeId,id);
        queryWrapper.lambda().eq(AcctPay::getVoided,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean payment(AddAcctPayForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        AcctPay acctPay = ConvertUtil.convert(form, AcctPay.class);

        acctPay.setStateFlag(1);
        acctPay.setMdyBy(systemUser.getId().intValue());
        acctPay.setMdyByDtm(LocalDateTime.now());
        acctPay.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctPay);
        if(result){
            log.warn("付款成功");
            AcctPayFollow acctPayFollow = new AcctPayFollow();
            acctPayFollow.setSType(OperationEnum.UPDATE.getCode());
            acctPayFollow.setFollowContext(systemUser.getUserName()+"进行付款操作");
            acctPayFollow.setCrtBy(systemUser.getId().intValue());
            acctPayFollow.setCrtByDtm(LocalDateTime.now());
            acctPayFollow.setCrtByName(systemUser.getUserName());
            acctPayFollow.setPayId(acctPay.getId());
            boolean save = this.acctPayFollowService.save(acctPayFollow);
            if(save){
                log.warn("付款操作,操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean confirmPayment(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        AcctPay acctPay = new AcctPay();

        acctPay.setId(form.getId());
        acctPay.setIsPay(1);
        acctPay.setMdyBy(systemUser.getId().intValue());
        acctPay.setMdyByDtm(LocalDateTime.now());
        acctPay.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctPay);
        if (result) {
            log.warn("确认付款成功");
            AcctPayFollow acctPayFollow = new AcctPayFollow();
            acctPayFollow.setSType(OperationEnum.UPDATE.getCode());
            acctPayFollow.setFollowContext(systemUser.getUserName() + "进行确认付款操作");
            acctPayFollow.setCrtBy(systemUser.getId().intValue());
            acctPayFollow.setCrtByDtm(LocalDateTime.now());
            acctPayFollow.setCrtByName(systemUser.getUserName());
            acctPayFollow.setPayId(acctPay.getId());
            boolean save = this.acctPayFollowService.save(acctPayFollow);
            if (save) {
                log.warn("确认付款操作,操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public boolean cancelPayment(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        AcctPay acctPay = new AcctPay();

        acctPay.setId(form.getId());
        acctPay.setStateFlag(0);
        acctPay.setMdyBy(systemUser.getId().intValue());
        acctPay.setMdyByDtm(LocalDateTime.now());
        acctPay.setMdyByName(systemUser.getUserName());
        boolean result = this.updateById(acctPay);
        if (result) {
            log.warn("取消付款成功");
            AcctPayFollow acctPayFollow = new AcctPayFollow();
            acctPayFollow.setSType(OperationEnum.UPDATE.getCode());
            acctPayFollow.setFollowContext(systemUser.getUserName() + "进行取消付款操作");
            acctPayFollow.setCrtBy(systemUser.getId().intValue());
            acctPayFollow.setCrtByDtm(LocalDateTime.now());
            acctPayFollow.setCrtByName(systemUser.getUserName());
            acctPayFollow.setPayId(acctPay.getId());
            boolean save = this.acctPayFollowService.save(acctPayFollow);
            if (save) {
                log.warn("取消付款操作,操作记录添加成功");
            }
        }
        return result;
    }

    @Override
    public Integer saveAcctPay(AddAcctPayForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctPayFollow acctPayFollow = new AcctPayFollow();
        AcctPay acctPay = ConvertUtil.convert(form, AcctPay.class);

        acctPay.setId(null);
        acctPay.setPayNo(commodityService.getOrderNo(NoCodeEnum.ACCT_PAY.getCode(), LocalDateTime.now()));
        acctPay.setCrtBy(systemUser.getId().intValue());
        acctPay.setCrtByDtm(LocalDateTime.now());
        acctPay.setCrtByName(systemUser.getUserName());
        acctPayFollow.setSType(OperationEnum.INSERT.getCode());
        acctPayFollow.setFollowContext(systemUser.getUserName() + "新增付款单信息");

        boolean result = this.saveOrUpdate(acctPay);
        if(result){
            log.warn("新增或修改付款单成功");
            acctPayFollow.setCrtBy(systemUser.getId().intValue());
            acctPayFollow.setCrtByDtm(LocalDateTime.now());

            acctPayFollow.setCrtByName(systemUser.getUserName());
            acctPayFollow.setPayId(acctPay.getId());
            boolean save = this.acctPayFollowService.save(acctPayFollow);
            if(save){
                log.warn("新增或修改付款单,操作记录添加成功");
            }
        }
        return acctPay.getId();
    }

    @Override
    public CommonResult reverseAcctPay(PermissionForm form) {
        AcctPayFollow acctPayFollow = new AcctPayFollow();
        acctPayFollow.setPayId(form.getId().intValue());
        acctPayFollow.setSType(OperationEnum.UPDATE.getCode());
        acctPayFollow.setCrtBy(form.getId().intValue());
        acctPayFollow.setCrtByDtm(LocalDateTime.now());
        acctPayFollow.setCrtByName(form.getUserName());

        CommonResult commonResult = customerService.deApproval(form);
        if(commonResult.getCode().equals(0)){
            acctPayFollow.setFollowContext("水单审核成功");
        }else {
            acctPayFollow.setFollowContext("水单审核失败");
        }
        boolean save = this.acctPayFollowService.save(acctPayFollow);
        if(save){
            log.warn("审核，操作日志添加成功");
        }
        return commonResult;
    }

}
