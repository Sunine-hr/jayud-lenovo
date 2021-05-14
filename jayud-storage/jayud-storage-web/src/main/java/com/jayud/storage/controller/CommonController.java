package com.jayud.storage.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.WarehouseGoodsInForm;
import com.jayud.storage.model.bo.WarehouseGoodsOutForm;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.jayud.storage.model.vo.GoodVO;
import com.jayud.storage.model.vo.InitComboxSVO;
import com.jayud.storage.model.vo.InitComboxWarehouseVO;
import com.jayud.storage.service.IGoodService;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "仓储模块公用接口")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private IWarehouseAreaShelvesLocationService warehouseAreaShelvesLocationService;


    /**
     * 导出入库商品模板
     */
    @ApiOperation(value = "导出入库商品模板")
    @PostMapping(value = "/exportInProductTemplate")
    public void exportInProductTemplate(@RequestBody HttpServletResponse response){
        ExcelUtils.exportSinglePageHeadExcel("入库商品模板", WarehouseGoodsInForm.class,response);
    }

    /**
     * 导出出库商品模板
     */
    @ApiOperation(value = "导出出库商品模板")
    @PostMapping(value = "/exportOutProductTemplate")
    public void exportOutProductTemplate(@RequestBody HttpServletResponse response){
        ExcelUtils.exportSinglePageHeadExcel("出库商品模板", WarehouseGoodsOutForm.class,response);
    }

    /**
     * 导入入库商品信息
     */
    @ApiOperation(value = "导入入库商品信息")
    @PostMapping(value = "/importInProductInformation")
    public CommonResult importInProductInformation(@RequestBody MultipartFile file){
        List<Object> list = null;
        try {
            list = EasyExcelFactory.read(file.getInputStream(), new Sheet(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String listString = JSONObject.toJSONString(list);
        log.info("list:{}",  listString);
        JSONArray arryList = JSONObject.parseArray(listString);

        return CommonResult.success();
    }

    /**
     * 导入出库商品信息
     */
    @ApiOperation(value = "导入出库商品信息")
    @PostMapping(value = "/importOutProductInformation")
    public CommonResult importOutProductInformation(@RequestBody MultipartFile file){

        return CommonResult.success();
    }

    @ApiOperation(value = "生成入仓号/生成出仓号")
    @PostMapping(value = "/getWarehouseNumber")
    public CommonResult getWarehouseNumber(@RequestBody Map<String,Object> map){
        Long type = MapUtil.getLong(map,"type");
        String warehouseNumber = null;
        if(type == 1){//生成入仓号
            warehouseNumber = (String)omsClient.getWarehouseNumber("JYDRK").getData();
        }
        if(type == 2){
            warehouseNumber = (String)omsClient.getWarehouseNumber("JYDCK").getData();
        }
        if(warehouseNumber==null){
            return CommonResult.error(444,"获取入仓号失败");
        }
        return CommonResult.success(warehouseNumber);
    }

    @ApiOperation(value = "确认入仓下拉列表框")
    @PostMapping(value = "/commonComBox")
    public CommonResult commonComBox(){
        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("operation").getData();
        List<InitComboxSVO> data1 = omsClient.initDictNameByDictTypeCode("cardType").getData();
        List<InitComboxWarehouseVO> data2 = omsClient.initComboxWarehouseVO().getData();
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("operation",data);
        hashMap.put("cardType",data1);
        hashMap.put("warehouseInfo",data2);
        return CommonResult.success(hashMap);
    }

    @ApiOperation(value = "获取商品下拉列表框")
    @PostMapping(value = "/goodsComBox")
    public CommonResult goodsComBox(@RequestBody Map<String,Object> map){
        String code = MapUtil.getStr(map,"code");
        String type = MapUtil.getStr(map,"type");
        Long customerId = omsClient.getCustomerByCode(code).getData();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customer_id",customerId);
        List<GoodVO> list = goodService.list(queryWrapper);
        if(type=="1"){
            return CommonResult.success(list);
        }
        if(type=="2"){

        }
        return CommonResult.error(444,"获取商品失败");
    }

    @ApiOperation(value = "查询所有库位")
    @PostMapping(value = "/location1ComBox")
    public CommonResult location1ComBox(@RequestBody Map<String,Object> map){
        List<WarehouseAreaShelvesLocation> warehouseAreaShelvesLocations = warehouseAreaShelvesLocationService.getList();
        return CommonResult.success(warehouseAreaShelvesLocations);
    }

    @ApiOperation(value = "库位添加下拉列表框")
    @PostMapping(value = "/locationComBox")
    public CommonResult locationComBox(){
        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("shelfType").getData();
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("shelfType",data);
        return CommonResult.success(hashMap);
    }
}
