package com.jayud.scm.service.impl;

import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddVerificationReocrdsForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.VerificationReocrdsMapper;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 核销列表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-08
 */
@Service
public class VerificationReocrdsServiceImpl extends ServiceImpl<VerificationReocrdsMapper, VerificationReocrds> implements IVerificationReocrdsService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private IAcctPayEntryService acctPayEntryService;

    @Autowired
    private IAcctReceiptService acctReceiptService;

    @Autowired
    private IAcctPayService acctPayService;

    @Autowired
    private IAccountBankBillService accountBankBillService;

    @Override
    public boolean writeOff(List<AddVerificationReocrdsForm> form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Set<Integer> set = new HashSet<>();
        Integer payId = form.get(0).getPayId();
        List<VerificationReocrds> verificationReocrds = new ArrayList<>();
        for (AddVerificationReocrdsForm addVerificationReocrdsForm : form) {
            set.add(addVerificationReocrdsForm.getOrderId());
            VerificationReocrds verificationReocrd = ConvertUtil.convert(addVerificationReocrdsForm, VerificationReocrds.class);
            verificationReocrd.setFBillNo(commodityService.getOrderNo(NoCodeEnum.VERIFICATION_REOCRDS.getCode(),LocalDateTime.now()));
            verificationReocrd.setCMoney(addVerificationReocrdsForm.getNMoney());
            verificationReocrd.setCrtBy(systemUser.getId().intValue());
            verificationReocrd.setCrtByDtm(LocalDateTime.now());
            verificationReocrd.setCrtByName(systemUser.getUserName());
            verificationReocrds.add(verificationReocrd);
        }

        boolean result = this.saveBatch(verificationReocrds);
        if(result){
            log.warn("核销成功");
            for (Integer integer : set) {
                CommonResult commonResult = bookingOrderService.reverseCalculation(integer);
                if(commonResult.getCode().equals(1)){
                    log.warn(integer+"出口核销反算人民币单价 失败");
                }
            }
            //核销成功，生成应付款  水单类别为预付款，生成收款单，生成付款单，核销成功后生成应付款单
            AcctReceipt acctReceipt = acctReceiptService.getById(payId);
            AccountBankBill accountBankBill = accountBankBillService.getById(acctReceipt.getBankId());
            AcctPay acctPay = acctPayService.getAcctPayByPayToMeId(acctReceipt.getId());
            if(accountBankBill.getBankBillType().equals("预付款")){
                List<AcctPayEntry> list = new ArrayList<>();
                for (VerificationReocrds verificationReocrd : verificationReocrds) {
                    AcctPayEntry acctPayEntry = new AcctPayEntry();
                    acctPayEntry.setFBillNo(commodityService.getOrderNo(NoCodeEnum.ACCT_ENTRY_PAY.getCode(),LocalDateTime.now()));
                    acctPayEntry.setPayId(acctPay.getId());
                    acctPayEntry.setCurrencyName(verificationReocrd.getCurrencyName());
                    acctPayEntry.setFDate(LocalDateTime.now());
                    acctPayEntry.setApMoney(verificationReocrd.getCMoney().multiply(acctReceipt.getAccRate()));
                    acctPayEntry.setExportVerificationId(verificationReocrd.getId());
                    acctPayEntry.setCrtBy(systemUser.getId().intValue());
                    acctPayEntry.setCrtByDtm(LocalDateTime.now());
                    acctPayEntry.setCrtByName(systemUser.getUserName());
                    list.add(acctPayEntry);
                }

                boolean result1 = acctPayEntryService.saveBatch(list);
                if(result1){
                    log.warn("核销成功，生成对应的应付款单");
                }
            }


        }
        return result;
    }

    @Override
    public boolean cancelWriteOff(QueryCommonForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        List<VerificationReocrds> list = new ArrayList<>();
        for (Integer id : form.getIds()) {
            VerificationReocrds verificationReocrds = new VerificationReocrds();
            verificationReocrds.setId(id);
            verificationReocrds.setVoided(1);
            verificationReocrds.setVoidedBy(systemUser.getId().intValue());
            verificationReocrds.setVoidedByDtm(LocalDateTime.now());
            verificationReocrds.setVoidedByName(systemUser.getUserName());
            list.add(verificationReocrds);
        }
        boolean result = this.updateBatchById(list);
        if(result){
            log.warn("反核销成功");
            boolean delete = acctPayEntryService.deleteAcctPayEntryByExportVerificationId(form.getIds());
            if(delete){
                log.warn("删除应收款单成功");
            }
        }
        return result;
    }
}
