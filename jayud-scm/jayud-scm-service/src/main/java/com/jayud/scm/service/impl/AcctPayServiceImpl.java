package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddAcctPayForm;
import com.jayud.scm.model.bo.AddAcctPayReceiptForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.AcctPay;
import com.jayud.scm.mapper.AcctPayMapper;
import com.jayud.scm.model.po.AcctPayFollow;
import com.jayud.scm.model.po.ExportTaxInvoice;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.AcctPayVO;
import com.jayud.scm.service.IAcctPayFollowService;
import com.jayud.scm.service.IAcctPayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public boolean generatePaymentDocument(AddAcctPayReceiptForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        AcctPay acctPay = ConvertUtil.convert(form, AcctPay.class);
        acctPay.setPayNo(commodityService.getOrderNo(NoCodeEnum.ACCT_PAY.getCode(), LocalDateTime.now()));
        acctPay.setCrtBy(systemUser.getId().intValue());
        acctPay.setCrtByDtm(LocalDateTime.now());
        acctPay.setCrtByName(systemUser.getUserName());
        if(form.getProxyMoney() != null){
            acctPay.setApMoney(acctPay.getApMoney().subtract(form.getProxyMoney()));
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
}
