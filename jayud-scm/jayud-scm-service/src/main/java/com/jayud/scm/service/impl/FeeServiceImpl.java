package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddFeeForm;
import com.jayud.scm.model.bo.AddFeeListForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.FeeMapper;
import com.jayud.scm.model.vo.FeeLadderVO;
import com.jayud.scm.model.vo.FeeListVO;
import com.jayud.scm.model.vo.FeeVO;
import com.jayud.scm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 结算方案条款 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Service
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements IFeeService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Autowired
    private IFeeListService feeListService;

    @Autowired
    private IFeeLadderService feeLadderService;

    @Override
    public IPage<FeeVO> findByPage(QueryCommonForm form) {
        Page<FeeVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateFee(AddFeeForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        CustomerFollow customerFollow = new CustomerFollow();
        Fee fee = ConvertUtil.convert(form, Fee.class);
        if(fee.getId() != null){
            fee.setMdyBy(systemUser.getId().intValue());
            fee.setMdyByDtm(LocalDateTime.now());
            fee.setMdyByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.UPDATE.getCode());
            customerFollow.setFollowContext(systemUser.getUpdatedUser()+"修改结算方案条款");
        }else {
            fee.setCrtBy(systemUser.getId().intValue());
            fee.setCrtByDtm(LocalDateTime.now());
            fee.setCrtByName(systemUser.getUserName());
            customerFollow.setSType(OperationEnum.INSERT.getCode());
            customerFollow.setFollowContext(systemUser.getUpdatedUser()+"新增结算方案条款");
        }
        boolean save = this.saveOrUpdate(fee);
        if(save){
            //不管新增还是修改，删除原来的结算方案条款明细
            feeListService.delete(fee.getId());

            //新增结算方案条款明细
            List<AddFeeListForm> addFeeListForms = form.getAddFeeListForms();
            List<FeeList> feeLists = ConvertUtil.convertList(addFeeListForms, FeeList.class);
            for (FeeList feeList : feeLists) {
                feeList.setFeeId(fee.getId());
                feeList.setCrtBy(systemUser.getId().intValue());
                feeList.setCrtByDtm(LocalDateTime.now());
                feeList.setCrtByName(systemUser.getUserName());
            }
            boolean b = feeListService.saveBatch(feeLists);
            if(b){
                log.warn("增加结算方案条款明细成功");
            }

            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save1 = customerFollowService.save(customerFollow);
            if(save1){
                log.warn("增加结算方案条款，增加客户操作日志成功");
            }
        }
        return save;
    }

    @Override
    public FeeVO getFeeById(Integer id) {
        FeeVO feeVO = ConvertUtil.convert(this.getById(id), FeeVO.class);
        List<FeeListVO> feeListByFeeId = feeListService.getFeeListByFeeId(feeVO.getId());
        feeVO.setFeeListVOS(feeListByFeeId);
        return feeVO;
    }

    @Override
    public boolean deleteFeeByIds(DeleteForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<Fee> fees = new ArrayList<>();
        List<FeeList> feeLists = new ArrayList<>();
        for (Long id : form.getIds()) {
            Fee fee = new Fee();
            fee.setId(id.intValue());
            fee.setVoided(1);
            fee.setVoidedBy(systemUser.getId().intValue());
            fee.setVoidedByDtm(LocalDateTime.now());
            fee.setVoidedByName(systemUser.getUserName());
            fees.add(fee);
            List<FeeListVO> feeListByFeeId = feeListService.getFeeListByFeeId(fee.getId());
            feeLists.addAll(ConvertUtil.convertList(feeListByFeeId,FeeList.class));
        }
        boolean b = this.updateBatchById(fees);
        if(b){
            log.warn("删除结算方案条款成功");
            for (FeeList feeList : feeLists) {
                feeList.setVoided(1);
                feeList.setVoidedBy(systemUser.getId().intValue());
                feeList.setVoidedByDtm(LocalDateTime.now());
                feeList.setVoidedByName(systemUser.getUserName());
            }
            feeListService.updateBatchById(feeLists);
            log.warn("删除结算方案条款明细成功");

            CustomerFollow customerFollow = new CustomerFollow();
            customerFollow.setSType(OperationEnum.DELETE.getCode());
            customerFollow.setFollowContext("批量删除结算方案条款");
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
        }

        return b;
    }

    @Override
    public List<Long> getFeeByFeeModelId(Integer id) {
        QueryWrapper<Fee> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Fee::getFeeModelId,id);
        queryWrapper.lambda().eq(Fee::getVoided,0);
        List<Fee> list = this.list(queryWrapper);
        List<Long> longs = new ArrayList<>();
        for (Fee fee : list) {
            longs.add(fee.getId().longValue());
        }
        return longs;
    }

    @Override
    public boolean copyFee(Integer id) {
        Fee byId = this.getById(id);
        List<FeeListVO> feeListByFeeId = feeListService.getFeeListByFeeId(byId.getId());
        List<FeeLadderVO> feeLadderByFeeId = feeLadderService.getFeeLadderByFeeId(byId.getId());
        byId.setId(null);
        boolean save = this.save(byId);
        if(save){
            log.warn("复制结算方案条款成功");
            for (FeeLadderVO feeLadderVO : feeLadderByFeeId) {
                feeLadderVO.setId(null);
            }
            for (FeeListVO feeListVO : feeListByFeeId) {
                feeListVO.setId(null);
            }
            boolean b = feeListService.saveBatch(ConvertUtil.convertList(feeListByFeeId, FeeList.class));
            if(b){
                log.warn("复制结算方案条款明细成功");
            }
            boolean b1 = feeLadderService.saveBatch(ConvertUtil.convertList(feeLadderByFeeId, FeeLadder.class));
            if(b1){
                log.warn("复制结算方案条款阶梯价成功");
            }
        }
        return save;
    }

    @Override
    public List<FeeVO> getFeeVOByFeeModelId(Integer id) {
        QueryWrapper<Fee> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Fee::getFeeModelId,id);
        queryWrapper.lambda().eq(Fee::getVoided,0);
        List<Fee> list = this.list(queryWrapper);
        List<FeeVO> feeVOS = ConvertUtil.convertList(list, FeeVO.class);
        for (FeeVO feeVO : feeVOS) {
            List<FeeListVO> feeListByFeeId = feeListService.getFeeListByFeeId(feeVO.getId());
            feeVO.setFeeListVOS(feeListByFeeId);
        }
        return feeVOS;
    }


}
