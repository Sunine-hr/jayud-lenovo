package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.AddFeeModelForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.FeeModelVO;
import com.jayud.scm.service.IFeeModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "根据条件分页查询所有该客户的结算设置")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<FeeModelVO> page = this.feeModelService.findByPage(form);
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

}

