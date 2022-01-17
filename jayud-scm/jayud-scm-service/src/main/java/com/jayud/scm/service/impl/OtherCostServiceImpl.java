package com.jayud.scm.service.impl;

import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddOtherCostForm;
import com.jayud.scm.model.po.OtherCost;
import com.jayud.scm.mapper.OtherCostMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.service.IOtherCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 其他费用记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@Service
public class OtherCostServiceImpl extends ServiceImpl<OtherCostMapper, OtherCost> implements IOtherCostService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public boolean saveOrUpdateOtherCost(AddOtherCostForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        OtherCost otherCost = ConvertUtil.convert(form, OtherCost.class);
        if(otherCost.getId() != null){
            otherCost.setMdyBy(systemUser.getId().intValue());
            otherCost.setMdyByDtm(LocalDateTime.now());
            otherCost.setMdyByName(systemUser.getUserName());
        }else{
            otherCost.setCrtBy(systemUser.getId().intValue());
            otherCost.setCrtByDtm(LocalDateTime.now());
            otherCost.setCrtByName(systemUser.getUserName());
        }
        boolean result = this.saveOrUpdate(otherCost);
        if(result){
            log.warn("新增或修改其他费用成功");
        }
        return result;
    }
}
