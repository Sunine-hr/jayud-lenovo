package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddFeeModelForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.FeeModelMapper;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.FeeModelVO;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.IFeeModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.IFeeService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 结算方案 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Service
public class FeeModelServiceImpl extends ServiceImpl<FeeModelMapper, FeeModel> implements IFeeModelService {

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private IFeeService feeService;

    @Override
    public IPage<FeeModelVO> findByPage(QueryCommonForm form) {
        Page<FeeModelVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean addFeeModel(AddFeeModelForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        CustomerFollow customerFollow = new CustomerFollow();

        FeeModel feeModel = ConvertUtil.convert(form, FeeModel.class);

        feeModel.setCrtBy(systemUser.getId().intValue());
        feeModel.setCrtByDtm(LocalDateTime.now());
        feeModel.setCrtByName(systemUser.getUserName());
        feeModel.setModelName(commodityService.getOrderNo("1003", LocalDateTime.now()));
        customerFollow.setSType(OperationEnum.INSERT.getCode());
        customerFollow.setFollowContext(systemUser.getUserName()+"增加结算设置");

        boolean save1 = this.save(feeModel);
        if(save1){
            customerFollow.setCustomerId(form.getCustomerId());
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加结算设置，客户操作日志添加成功");
            }
        }
        return save1;
    }

    @Override
    public boolean modifyDefaultValues(AddFeeModelForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取原来为默认值的值
        QueryWrapper<FeeModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FeeModel::getCustomerId,form.getCustomerId());
        queryWrapper.lambda().eq(FeeModel::getModelType,form.getModelType());
        queryWrapper.lambda().eq(FeeModel::getIsDefaultValue,1);
        FeeModel one = this.getOne(queryWrapper);

        FeeModel feeModel = ConvertUtil.convert(form, FeeModel.class);
        feeModel.setMdyBy(systemUser.getId().intValue());
        feeModel.setMdyByDtm(LocalDateTime.now());
        feeModel.setMdyByName(systemUser.getUserName());

        CustomerFollow customerFollow = new CustomerFollow();
        customerFollow.setSType(OperationEnum.UPDATE.getCode());
        customerFollow.setCustomerId(form.getCustomerId());
        customerFollow.setFollowContext(UserOperator.getToken()+"修改结算设置的默认值为"+feeModel.getModelName());
        customerFollow.setCrtBy(systemUser.getId().intValue());
        customerFollow.setCrtByDtm(LocalDateTime.now());
        customerFollow.setCrtByName(UserOperator.getToken());
        boolean update = this.updateById(feeModel);
        if(update){
            log.warn("修改默认值成功");
            boolean save = customerFollowService.save(customerFollow);
            if(save){
                log.warn("增加客户修改结算设置默认值日志成功");
            }
            //修改成功
            if(one != null){
                one.setIsDefaultValue(0);
                this.updateById(one);
            }
        }
        return update;
    }

    @Override
    public boolean deleteFeeByIds(DeleteForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<FeeModel> feeModels = new ArrayList<>();
        List<Long> longs = new ArrayList<>();
        for (Long id : form.getIds()) {
            FeeModel feeModel = new FeeModel();
            feeModel.setId(id.intValue());
            feeModel.setVoided(1);
            feeModel.setVoidedBy(systemUser.getId().intValue());
            feeModel.setVoidedByDtm(LocalDateTime.now());
            feeModel.setVoidedByName(systemUser.getUserName());
            feeModels.add(feeModel);
            List<Long> longs1 = feeService.getFeeByFeeModelId(feeModel.getId());
            longs.addAll(longs1);
        }
        boolean b = this.updateBatchById(feeModels);
        if(b){
            log.warn("删除结算方案成功");
            form.setIds(longs);
            feeService.deleteFeeByIds(form);

            CustomerFollow customerFollow = new CustomerFollow();
            customerFollow.setSType(OperationEnum.DELETE.getCode());
            customerFollow.setFollowContext("批量删除结算方案条款");
            customerFollow.setCrtBy(systemUser.getId().intValue());
            customerFollow.setCrtByDtm(LocalDateTime.now());
            customerFollow.setCrtByName(systemUser.getUserName());
        }

        return false;
    }


}
