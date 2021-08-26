package com.jayud.customs.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.aop.annotations.RepeatSubmitLimit;
import com.jayud.common.enums.CustomsBizModelEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.HttpUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.WordUtil;
import com.jayud.customs.model.bo.AddCustomsDeclarationFilingForm;
import com.jayud.customs.model.bo.QueryCustomsDeclarationFiling;
import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import com.jayud.customs.model.po.CustomsDeclarationFiling;
import com.jayud.customs.model.vo.CustomsDeclarationFilingVO;
import com.jayud.customs.service.ICustomsDeclFilingRecordService;
import com.jayud.customs.service.ICustomsDeclarationFilingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Value("${customsFile.filingPath}")
    private String customsFilingPath;


    @ApiOperation(value = "新增/编辑")
    @PostMapping("/saveOrUpdate")
    @RepeatSubmitLimit
    public CommonResult saveOrUpdate(@RequestBody @Valid AddCustomsDeclarationFilingForm form) {
        //校验箱单号是否存在
        if (form.getId() == null && this.customsDeclarationFilingService.exitBoxNum(new CustomsDeclarationFiling().setBoxNum(form.getBoxNum()))) {
            return CommonResult.error(400, "箱单号已存在,请重新生成箱单号");
        }
        if (form.getNums().size() == 0) {
            return CommonResult.error(400, "报关单号至少保留一个");
        }
        //校验云报关单号唯一性
        Set<String> nums = form.getNums().stream().map(CustomsDeclFilingRecord::getNum).collect(Collectors.toSet());

        List<CustomsDeclFilingRecord> list = this.customsDeclFilingRecordService.getByNums(form.getId(), new ArrayList<>(nums));
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> ids = list.stream().map(e -> e.getCustomsDeclFilingId()).collect(Collectors.toSet());
            List<CustomsDeclarationFiling> customsDeclarationFilings = this.customsDeclarationFilingService.listByIds(ids);
            String str = "报关单号已经归档在%s箱号中，不允许再次归档";
            StringBuilder sb = new StringBuilder();
            customsDeclarationFilings.forEach(e -> sb.append(String.format(str, e.getBoxNum())).append("\n"));
            return CommonResult.error(400, sb.toString());
        }
        Set<CustomsDeclFilingRecord> tmps = form.getNums().stream().filter(e -> !StringUtils.isEmpty(e.getNum())).collect(Collectors.toSet());
        form.setNums(new ArrayList<>(tmps));
        if (form.getNums().size() == 0) {
            return CommonResult.error(400, "请输入报关单号");
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

    @ApiOperation(value = "查询归档详情")
    @PostMapping("/getDetails")
    public CommonResult<CustomsDeclarationFilingVO> getDetails(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        CustomsDeclarationFilingVO tmp = this.customsDeclarationFilingService.getDetails(id);
        return CommonResult.success(tmp);
    }

    @ApiOperation(value = "导出报关归档文件")
    @PostMapping("/exportWordFile")
    public CommonResult<CustomsDeclarationFilingVO> exportWordFile(@RequestBody Map<String, Object> map) throws Exception {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        CustomsDeclarationFilingVO tmp = this.customsDeclarationFilingService.getDetails(id);
        //进出口类型(1进口 2出口)
        String imAndExType = tmp.getImAndExType() == 1 ? "进口" : "出口";
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("boxNum", tmp.getBoxNum());
        dataMap.put("docType", CustomsBizModelEnum.getDesc(tmp.getBizModel()) + "/" + imAndExType);
        dataMap.put("filingDate", tmp.getFilingDate());
        dataMap.put("number", tmp.getNumber());
        dataMap.put("createUser", tmp.getCreateUser() == null ? "" : tmp.getCreateUser());
        dataMap.put("createTime", DateUtils.LocalDateTime2Str(tmp.getCreateTime(), "yyyy年MM月dd日"));
        WordUtil.exportWord(customsFilingPath, "报关归档文件", null
                , dataMap, HttpUtils.getHttpResponseServletContext());

        return CommonResult.success();
    }


//    public static void main(String[] args) {
//        CustomsDeclarationFilingController tm=new CustomsDeclarationFilingController();
//        System.out.println(tm.prefix.get(1));
//    }

}

