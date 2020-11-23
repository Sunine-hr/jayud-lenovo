package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCostInfoForm;
import com.jayud.oms.model.bo.QueryCostInfoForm;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.vo.CostInfoVO;
import com.jayud.oms.service.ICostInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用名描述 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@RestController
@RequestMapping("/costInfo")
@Api(tags = "基础数据费用名列表接口")
public class CostInfoController {

    @Autowired
    private ICostInfoService costInfoService;

    @ApiOperation(value = "查询费用名称列表")
    @PostMapping(value = "/findCostInfoByPage")
    public CommonResult<CommonPageResult<CostInfoVO>> findCostInfoByPage(@RequestBody QueryCostInfoForm form) {
        IPage<CostInfoVO> pageList = costInfoService.findCostInfoByPage(form);
        CommonPageResult<CostInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增编辑费用名称")
    @PostMapping(value = "/saveOrUpdateCostInfo")
    public CommonResult saveOrUpdateCostInfo(@Valid @RequestBody AddCostInfoForm form) {
        CostInfo costInfo = new CostInfo().setId(form.getId())
                .setIdCode(form.getIdCode()).setName(form.getName());
        if (this.costInfoService.checkUnique(costInfo)) {
            return CommonResult.error(400, "名称或代码已经存在");
        }
        if (this.costInfoService.saveOrUpdateCostInfo(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "更改启用/禁用费用名称状态,id是费用名称主键")
    @PostMapping(value = "/enableOrDisableCostInfo")
    public CommonResult enableOrDisableCostInfo(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.costInfoService.enableOrDisableCostInfo(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }


    @ApiOperation(value = "根据主键获取费用名称详情,id是费用名称主键")
    @PostMapping(value = "/getCostInfoById")
    public CommonResult getCostInfoById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        CostInfo costInfo = this.costInfoService.getById(id);
        String[] tmps = costInfo.getCids().split(",");

        List<Long> list = new ArrayList<>();
        for (String tmp : tmps) {
            list.add(Long.parseLong(tmp));
        }
        CostInfoVO costInfoVO = ConvertUtil.convert(costInfo, CostInfoVO.class);
        costInfoVO.setCids(list);
        return CommonResult.success(costInfoVO);
    }
}

