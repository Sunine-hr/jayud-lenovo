package com.jayud.storage.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.storage.model.bo.WarehouseGoodsInForm;
import com.jayud.storage.model.bo.WarehouseGoodsOutForm;
import com.jayud.storage.model.po.WarehouseGoods;
import com.netflix.client.http.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "仓储模块公用接口")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    /**
     * 导出入库商品模板
     */
    @ApiOperation(value = "导出入库商品模板")
    @PostMapping(value = "/exportInProductTemplate")
    public void exportInProductTemplate(HttpServletResponse response){
        ExcelUtils.exportSinglePageHeadExcel("入库商品模板", WarehouseGoodsInForm.class,response);
    }

    /**
     * 导出出库商品模板
     */
    @ApiOperation(value = "导出出库商品模板")
    @PostMapping(value = "/exportOutProductTemplate")
    public void exportOutProductTemplate(HttpServletResponse response){
        ExcelUtils.exportSinglePageHeadExcel("出库商品模板", WarehouseGoodsOutForm.class,response);
    }

    /**
     * 导入入库商品信息
     */
//    @ApiOperation(value = "导入入库商品信息")
//    @PostMapping(value = "/importInProductInformation")
//    public CommonResult importInProductInformation(MultipartFile file){
//
//    }

    /**
     * 导入出库商品信息
     */
//    @ApiOperation(value = "导入出库商品信息")
//    @PostMapping(value = "/importOutProductInformation")
//    public CommonResult importOutProductInformation(MultipartFile file){
//
//    }

}
