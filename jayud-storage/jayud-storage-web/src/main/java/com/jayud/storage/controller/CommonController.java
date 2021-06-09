package com.jayud.storage.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.WarehouseGoodsInForm;
import com.jayud.storage.model.bo.WarehouseGoodsOutForm;
import com.jayud.storage.model.po.*;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IWarehouseService warehouseService;

    @Autowired
    private IWarehouseAreaService warehouseAreaService;

    @Autowired
    private IWarehouseAreaShelvesService warehouseAreaShelvesService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private IStorageOutOrderService storageOutOrderService;

    /**
     * 导出入库商品模板
     */
    @ApiOperation(value = "导出入库商品模板")
    @GetMapping(value = "/exportInProductTemplate")
    public void exportInProductTemplate( HttpServletResponse response) throws IOException {
//        ExcelUtils.exportSinglePageHeadExcel("入库商品模板", WarehouseGoodsInForm.class,response);

        ExcelWriter excelWriter = null;
        OutputStream outputStream = null;
        String fileName = "入库商品模板";
        try {
            outputStream = response.getOutputStream();
            fileName = fileName + ".xls";
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
            excelWriter = EasyExcel.write(response.getOutputStream(),WarehouseGoodsInForm.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("入库商品").build();
            List<WarehouseGoodsInForm> warehouseGoodsInForms = new ArrayList<>();
            excelWriter.write(warehouseGoodsInForms, writeSheet);
        }  catch (Exception e) {
            outputStream.close();
            e.printStackTrace();
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }

        }
    }

    /**
     * 导出出库商品模板
     */
    @ApiOperation(value = "导出出库商品模板")
    @GetMapping(value = "/exportOutProductTemplate")
    public void exportOutProductTemplate( HttpServletResponse response) throws IOException {
//        ExcelUtils.exportSinglePageHeadExcel("出库商品模板", WarehouseGoodsOutForm.class,response);

        ExcelWriter excelWriter = null;
        OutputStream outputStream = null;
        String fileName = "出库商品模板";
        try {
            outputStream = response.getOutputStream();
            fileName = fileName + ".xls";
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
            excelWriter = EasyExcel.write(response.getOutputStream(),WarehouseGoodsOutForm.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("出库商品").build();
            List<WarehouseGoodsInForm> warehouseGoodsInForms = new ArrayList<>();
            excelWriter.write(warehouseGoodsInForms, writeSheet);
        }  catch (Exception e) {
            outputStream.close();
            e.printStackTrace();
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }

        }
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
        arryList.remove(0);
//        Object o = arryList.get(1);
//        System.out.println(o);
//        List<WarehouseGoodsInForm> warehouseGoodsInForms = arryList.toJavaList(WarehouseGoodsInForm.class);
//        for (WarehouseGoodsInForm warehouseGoodsInForm : warehouseGoodsInForms) {
//
//        }
        return CommonResult.success(arryList);
    }

    /**
     * 导入出库商品信息
     */
    @ApiOperation(value = "导入出库商品信息")
    @PostMapping(value = "/importOutProductInformation")
    public CommonResult importOutProductInformation(@RequestBody MultipartFile file){
        List<Object> list = null;
        try {
            list = EasyExcelFactory.read(file.getInputStream(), new Sheet(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String listString = JSONObject.toJSONString(list);
        log.info("list:{}",  listString);
        JSONArray arryList = JSONObject.parseArray(listString);
        arryList.remove(0);
//        List<WarehouseGoodsOutForm> warehouseGoodsOutForms = arryList.toJavaList(WarehouseGoodsOutForm.class);
//        for (WarehouseGoodsOutForm warehouseGoodsOutForm : warehouseGoodsOutForms) {
//
//        }
        return CommonResult.success(arryList);
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
//        List<InitComboxWarehouseVO> data2 = omsClient.initComboxWarehouseVO().getData();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        List<Warehouse> list = warehouseService.list(queryWrapper);
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("operation",data);
        hashMap.put("cardType",data1);
        hashMap.put("warehouseInfo",list);
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
        if(type.equals("1")){
            return CommonResult.success(list);
        }
        if(type.equals("2")){
            List<GoodNumberVO> goodNumberVOS = ConvertUtil.convertList(list, GoodNumberVO.class);
            for (GoodNumberVO goodNumberVO : goodNumberVOS) {
                List<String> str = new ArrayList<>();
                List<InGoodsOperationRecord> list1 = inGoodsOperationRecordService.getListBySku(goodNumberVO.getSku());
                for (InGoodsOperationRecord inGoodsOperationRecord : list1) {
                    str.add(inGoodsOperationRecord.getWarehousingBatchNo());
                }
                goodNumberVO.setBatchNumbers(str);
            }
            return CommonResult.success(goodNumberVOS);
        }
        return CommonResult.error(444,"获取商品失败");
    }

    @ApiOperation(value = "查询所有库位")
    @PostMapping(value = "/location1ComBox")
    public CommonResult location1ComBox(@RequestBody Map<String,Object> map){
        List<LocationCodeVO> warehouseAreaShelvesLocations = warehouseAreaShelvesLocationService.getList();
        return CommonResult.success(warehouseAreaShelvesLocations);
    }

    @ApiOperation(value = "货架类型下拉列表框")
    @PostMapping(value = "/locationComBox")
    public CommonResult locationComBox(){
        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("shelfType").getData();
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("shelfType",data);
        return CommonResult.success(hashMap);
    }

    @ApiOperation(value = "客户名称下拉列表框")
    @PostMapping(value = "/goodComBox")
    public CommonResult goodComBox(){
        return CommonResult.success(omsClient.getCustomerInfo().getData());
    }

    @ApiOperation(value = "移库下拉列表框")
    @PostMapping(value = "/relocationComBox")
    public CommonResult relocationComBox(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        List<Warehouse> list = warehouseService.list(queryWrapper);
        List<WarehouseArea> list1 = warehouseAreaService.list(queryWrapper);
        List<WarehouseAreaShelves> list2 = warehouseAreaShelvesService.list(queryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("warehouse",list);
        map.put("warehouseArea",list1);
        map.put("warehouseAreaShelves",list2);
        return CommonResult.success(map);
    }

    @ApiOperation(value = "获取打印拣货单库位下拉列表框")
    @PostMapping(value = "/pickLocationComBox")
    public CommonResult pickLocationComBox(@RequestBody Map<String,Object> map){

        String orderNo = MapUtil.getStr(map, "orderNo");

        List<WarehouseGoods> outListByOrderNo = warehouseGoodsService.getOutWarehouseGoodsByOrderNo(orderNo);

        Map<String,Object> map1 = new HashMap<>();

        if(CollectionUtils.isNotEmpty(outListByOrderNo)){
            for (WarehouseGoods warehouseGoods : outListByOrderNo) {
                InGoodsOperationRecord listByWarehousingBatchNoAndSku = inGoodsOperationRecordService.getListByWarehousingBatchNoAndSku(warehouseGoods.getWarehousingBatchNo(), warehouseGoods.getSku());
                //获取商品对应的库位编码
                List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(listByWarehousingBatchNoAndSku.getId());
                map1.put(warehouseGoods.getSku(),goodsLocationRecordByGoodId);
            }
        }

        return CommonResult.success(map1);
    }

}
