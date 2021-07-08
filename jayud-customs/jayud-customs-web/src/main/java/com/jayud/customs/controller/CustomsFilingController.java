package com.jayud.customs.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.service.ICustomsFilingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 报关归档表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/customsFiling")
public class CustomsFilingController {

    @Autowired
    private ICustomsFilingService customsFilingService;

    /**
     * 生成箱号
     */
    @ApiOperation(value = "生成箱号")
    @PostMapping(value = "generateBoxNum")
    public CommonResult<String> generateBoxNum(@RequestBody Map<String, Object> map) {
        String archiveDate = MapUtil.getStr(map, "archiveDate");
        Integer goodsType = MapUtil.getInt(map, "goodsType");
        Integer bizModel = MapUtil.getInt(map, "bizModel");
        if (StringUtils.isEmpty(archiveDate) || bizModel == null || goodsType == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        String boxNum=this.customsFilingService.generateBoxNum(archiveDate,goodsType,bizModel);

        return null;
    }
}

