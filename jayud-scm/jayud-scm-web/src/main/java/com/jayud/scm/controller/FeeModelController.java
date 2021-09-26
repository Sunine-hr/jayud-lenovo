package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerAgreementVO;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.FeeModelVO;
import com.jayud.scm.model.vo.FeeVO;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 结算方案 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/feeModel")
@Api(tags = "结算方案")
public class FeeModelController {

    @Autowired
    private IFeeModelService feeModelService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IFeeService feeService;

    @ApiOperation(value = "根据条件分页查询所有该客户的结算设置")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<FeeModelVO> page = this.feeModelService.findByPage(form);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            for (FeeModelVO record : page.getRecords()) {
                record.setModelTypeName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1011",record.getModelType()));

            }
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户结算设置")
    @PostMapping(value = "/addFeeModel")
    public CommonResult addFeeModel(@RequestBody AddFeeModelForm form) {
        boolean result = feeModelService.addFeeModel(form);
        if(!result){
            return CommonResult.error(444,"新增客户结算设置失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改默认值")
    @PostMapping(value = "/modifyDefaultValues")
    public CommonResult modifyDefaultValues(@RequestBody AddFeeModelForm form) {
        boolean result = feeModelService.modifyDefaultValues(form);
        if(!result){
            return CommonResult.error(444,"修改客户结算设置默认值失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "删除结算方案")
    @PostMapping(value = "/deleteFeeByIds")
    public CommonResult deleteFeeByIds(@RequestBody DeleteForm form) {
        boolean result = feeModelService.deleteFeeByIds(form);
        if(!result){
            return CommonResult.error(444,"删除结算方案失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "审核结算方案")
    @PostMapping(value = "/approveSettlementScheme")
    public CommonResult approveSettlementScheme(@RequestBody PermissionForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        List<FeeVO> fee = feeService.getFeeVOByFeeModelId(form.getId());
        if(CollectionUtils.isEmpty(fee)){
            return CommonResult.error(444,"该结算方案没有结算条款，无法进行审核");
        }
        for (FeeVO feeVO : fee) {
            if(CollectionUtils.isEmpty(feeVO.getFeeListVOS())){
                return CommonResult.error(444,"该结算条款没有设置结算公式，无法进行审核");
            }
        }

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());
        return customerService.toExamine(form);
    }

}

