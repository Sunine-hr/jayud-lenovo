package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.po.FeeList;
import com.jayud.scm.mapper.FeeListMapper;
import com.jayud.scm.model.po.Fees;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.FeeListVO;
import com.jayud.scm.service.IFeeListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.IFeesService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 结算条款费用明细 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Service
public class FeeListServiceImpl extends ServiceImpl<FeeListMapper, FeeList> implements IFeeListService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IFeesService feesService;

    @Override
    public List<FeeListVO> getFeeListByFeeId(Integer id) {
        QueryWrapper<FeeList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FeeList::getFeeId,id);
        queryWrapper.lambda().eq(FeeList::getVoided,0);
        List<FeeListVO> feeListVOS = ConvertUtil.convertList(this.list(queryWrapper), FeeListVO.class);
        for (FeeListVO feeListVO : feeListVOS) {
            Fees fees = feesService.getFeesById(feeListVO.getFeesId());
            feeListVO.setFeeAlias(fees.getFeeAlias());
            feeListVO.setFeeName(fees.getFeeName());
            feeListVO.setFeeFormula(fees.getFeeFormula());
        }
        return feeListVOS;
    }

    @Override
    public void delete(Integer id) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        QueryWrapper<FeeList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FeeList::getFeeId,id);
        queryWrapper.lambda().eq(FeeList::getVoided,0);
        List<FeeList> list = this.list(queryWrapper);
        for (FeeList feeList : list) {
            feeList.setVoided(1);
            feeList.setVoidedBy(systemUser.getId().intValue());
            feeList.setVoidedByDtm(LocalDateTime.now());
            feeList.setVoidedByName(systemUser.getUserName());
        }
        this.updateBatchById(list);
        log.warn("删除结算方案条款明细成功");
    }
}
