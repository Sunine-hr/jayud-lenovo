package com.jayud.scm.service.impl;

import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddVerificationReocrdsForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.po.VerificationReocrds;
import com.jayud.scm.mapper.VerificationReocrdsMapper;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.ISystemUserService;
import com.jayud.scm.service.IVerificationReocrdsService;
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

    @Override
    public boolean writeOff(List<AddVerificationReocrdsForm> form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        Set<Integer> set = new HashSet<>();
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
        }
        return result;
    }
}
