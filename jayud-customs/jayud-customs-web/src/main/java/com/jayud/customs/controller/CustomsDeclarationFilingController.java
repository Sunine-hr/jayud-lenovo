package com.jayud.customs.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.aop.annotations.RepeatSubmitLimit;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.model.bo.AddCustomsDeclarationFilingForm;
import com.jayud.customs.model.bo.QueryCustomsDeclarationFiling;
import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import com.jayud.customs.model.po.CustomsDeclarationFiling;
import com.jayud.customs.model.vo.CustomsDeclarationFilingVO;
import com.jayud.customs.service.ICustomsDeclFilingRecordService;
import com.jayud.customs.service.ICustomsDeclarationFilingService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 报关归档 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
@RestController
@RequestMapping("/customsDeclarationFiling")
public class CustomsDeclarationFilingController {

    @Autowired
    private ICustomsDeclarationFilingService customsDeclarationFilingService;
    @Autowired
    private ICustomsDeclFilingRecordService customsDeclFilingRecordService;


    @ApiOperation(value = "新增/编辑")
    @PostMapping("/saveOrUpdate")
    @RepeatSubmitLimit
    public CommonResult saveOrUpdate(@RequestBody @Valid AddCustomsDeclarationFilingForm form) {
        //校验箱单号是否存在
        if (this.customsDeclarationFilingService.exitBoxNum(new CustomsDeclarationFiling().setBoxNum(form.getBoxNum()))) {
            return CommonResult.error(400, "箱单号已存在,请重新生成箱单号");
        }
        //校验云报关单号唯一性
        Set<String> nums = form.getNums().stream().map(CustomsDeclFilingRecord::getNum).collect(Collectors.toSet());
        List<CustomsDeclFilingRecord> list = this.customsDeclFilingRecordService.getByNums(new ArrayList<>(nums));
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> ids = list.stream().map(e -> e.getCustomsDeclFilingId()).collect(Collectors.toSet());
            List<CustomsDeclarationFiling> customsDeclarationFilings = this.customsDeclarationFilingService.listByIds(ids);
            String str = "报关单号已经归档在%s箱号中，不允许再次归档";
            StringBuilder sb = new StringBuilder();
            customsDeclarationFilings.forEach(e -> sb.append(String.format(str, e.getBoxNum())).append("\n"));
            return CommonResult.error(400, sb.toString());
        }
        this.customsDeclarationFilingService.saveOrUpdate(form);
        return CommonResult.success();
    }


    @ApiOperation(value = "生成箱单号")
    @PostMapping("/generateBoxNum")
    public CommonResult<String> generateBoxNum(@RequestBody Map<String, Object> param) {
        String filingDate = MapUtil.getStr(param, "filingDate");
        Integer bizModel = MapUtil.getInt(param, "bizModel");
        Integer imAndExType = MapUtil.getInt(param, "imAndExType");

        if (StringUtils.isEmpty(filingDate) || bizModel == null || imAndExType == null) {
            return CommonResult.success();
        }
        String boxNum = this.customsDeclarationFilingService.generateBoxNum(filingDate, bizModel, imAndExType);

        return CommonResult.success(boxNum);
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<CustomsDeclarationFilingVO>> findByPage(@RequestBody QueryCustomsDeclarationFiling form) {
        IPage<CustomsDeclarationFilingVO> iPage = this.customsDeclarationFilingService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

//    public static void main(String[] args) {
//        CustomsDeclarationFilingController tm=new CustomsDeclarationFilingController();
//        System.out.println(tm.prefix.get(1));
//    }

}

