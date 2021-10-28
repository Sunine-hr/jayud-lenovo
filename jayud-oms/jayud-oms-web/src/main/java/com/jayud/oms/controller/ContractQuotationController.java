package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddContractQuotationForm;
import com.jayud.oms.model.bo.QueryContractQuotationForm;
import com.jayud.oms.model.bo.QueryCustomsQuestionnaireForm;
import com.jayud.oms.model.po.ContractQuotation;
import com.jayud.oms.model.vo.ContractQuotationVO;
import com.jayud.oms.service.IContractQuotationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 合同报价 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@RestController
@Api(tags = "合同报价")
@RequestMapping("/contractQuotation")
public class ContractQuotationController {

    @Autowired
    private IContractQuotationService contractQuotationService;

    @ApiOperation("分页查询合同报价")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<ContractQuotationVO>> findByPage(@RequestBody QueryContractQuotationForm form) {
        IPage<ContractQuotationVO> iPage = this.contractQuotationService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }


    @ApiOperation("新增/编辑合同报价")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody AddContractQuotationForm form) {
        form.checkParam();
        if (this.contractQuotationService.exitName(form.getId(), form.getName())) {
            return CommonResult.error(400, "该报价名称已存在");
        }
        this.contractQuotationService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation("自动生成编号")
    @PostMapping("/autoGenerateNum")
    public CommonResult<String> autoGenerateNum() {
        StringBuilder orderNo = new StringBuilder("BJ");
        orderNo.append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd"))
                .append(StringUtils.zeroComplement(4, this.contractQuotationService.count() + 1));
        return CommonResult.success(orderNo.toString());
    }

    @ApiOperation("获取编辑详情")
    @PostMapping("/getEditInfoById")
    public CommonResult<ContractQuotationVO> getEditInfoById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        ContractQuotationVO tmp = this.contractQuotationService.getEditInfoById(id);
        return CommonResult.success(tmp);
    }

    @ApiOperation("删除合同报价信息")
    @PostMapping("/deleteById")
    public CommonResult<ContractQuotationVO> deleteById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.contractQuotationService.updateById(new ContractQuotation().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }


    @ApiOperation("审核操作")
    @PostMapping("/auditOpt")
    public CommonResult<ContractQuotationVO> auditOpt(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.contractQuotationService.updateById(new ContractQuotation().setId(id).setAuditStatus(1)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken()));
        return CommonResult.success();
    }

    @ApiOperation("反审核操作")
    @PostMapping("/antiAuditOpt")
    public CommonResult<ContractQuotationVO> antiAuditOpt(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.contractQuotationService.updateById(new ContractQuotation().setId(id).setAuditStatus(2)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken()));
        return CommonResult.success();
    }

}

