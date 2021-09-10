package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddVerificationReocrdsForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.po.VerificationReocrds;
import com.jayud.scm.mapper.VerificationReocrdsMapper;
import com.jayud.scm.service.ICommodityService;
import com.jayud.scm.service.ISystemUserService;
import com.jayud.scm.service.IVerificationReocrdsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public boolean writeOff(List<AddVerificationReocrdsForm> form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<VerificationReocrds> verificationReocrds = ConvertUtil.convertList(form, VerificationReocrds.class);
        for (VerificationReocrds verificationReocrd : verificationReocrds) {
            verificationReocrd.setFBillNo(commodityService.getOrderNo(NoCodeEnum.VERIFICATION_REOCRDS.getCode(),LocalDateTime.now()));
            verificationReocrd.setCrtBy(systemUser.getId().intValue());
            verificationReocrd.setCrtByDtm(LocalDateTime.now());
            verificationReocrd.setCrtByName(systemUser.getUserName());
        }
        boolean result = this.saveBatch(verificationReocrds);
        if(result){
            log.warn("核销成功");
        }
        return result;
    }
}
