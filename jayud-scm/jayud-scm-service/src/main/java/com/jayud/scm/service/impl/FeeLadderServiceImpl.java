package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddFeeLadderForm;
import com.jayud.scm.model.bo.AddFeeLadderSettingForm;
import com.jayud.scm.model.po.FeeLadder;
import com.jayud.scm.mapper.FeeLadderMapper;
import com.jayud.scm.model.po.FeeList;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.FeeLadderVO;
import com.jayud.scm.model.vo.FeeListVO;
import com.jayud.scm.service.IFeeLadderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 结算条款阶梯价 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Service
public class FeeLadderServiceImpl extends ServiceImpl<FeeLadderMapper, FeeLadder> implements IFeeLadderService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<FeeLadderVO> getFeeLadderByFeeId(Integer id) {
        QueryWrapper<FeeLadder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FeeLadder::getFeeId,id);
        queryWrapper.lambda().eq(FeeLadder::getVoided,0);
        return ConvertUtil.convertList(this.list(queryWrapper), FeeLadderVO.class);
    }

    @Override
    public boolean stepPriceSetting(AddFeeLadderSettingForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        //新增阶梯价前，先删除原来的阶梯价
        QueryWrapper<FeeLadder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FeeLadder::getFeeId,form.getFeeId());
        queryWrapper.lambda().eq(FeeLadder::getVoided,0);
        List<FeeLadder> list = this.list(queryWrapper);
        for (FeeLadder feeLadder : list) {
            feeLadder.setVoided(1);
            feeLadder.setVoidedBy(systemUser.getId().intValue());
            feeLadder.setVoidedByDtm(LocalDateTime.now());
            feeLadder.setVoidedByName(systemUser.getUserName());
        }
        boolean b = this.updateBatchById(list);
        if(b){
            log.warn("删除原来阶梯价成功"+form.getFeeId());
            List<FeeLadder> feeLadders = ConvertUtil.convertList(form.getAddFeeLadderForms(), FeeLadder.class);
            for (FeeLadder feeLadder : feeLadders) {
                feeLadder.setCrtBy(systemUser.getId().intValue());
                feeLadder.setCrtByDtm(LocalDateTime.now());
                feeLadder.setCrtByName(systemUser.getUserName());
            }
            boolean b1 = this.saveOrUpdateBatch(feeLadders);
            if(b1){
                log.warn("新增阶梯价成功"+form.getFeeId());
            }
        }
        return b;
    }
}
