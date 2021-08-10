package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddFeeLadderSettingForm;
import com.jayud.scm.model.vo.FeesComboxVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.IFeeService;
import com.jayud.scm.service.IFeesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用计算公式表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-10
 */
@RestController
@RequestMapping("/fees")
@Api(tags = "费用计算公式")
public class FeesController {

    @Autowired
    private IFeesService feesService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "获取公式设置维护的下拉列表框")
    @PostMapping(value = "/formulaSettingMaintenance")
    public CommonResult formulaSettingMaintenance(@RequestBody Map<String,Object> map) {
        String modelType = MapUtil.getStr(map, "modelType");
        List<FeesComboxVO> feesComboxVOS = feesService.formulaSettingMaintenance(ibDataDicEntryService.getTextByDicCodeAndDataValue("1011",modelType));
        return CommonResult.success(feesComboxVOS);
    }

}

