package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.CorrespondEnum;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.CommodityDetailVO;
import com.jayud.scm.model.vo.CommodityFormVO;
import com.jayud.scm.model.vo.CommodityVO;
import com.jayud.scm.service.ICommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/commodity")
@Api(tags = "商品管理")
@Slf4j
public class CommodityController {

    @Autowired
    private ICommodityService commodityService;

    @ApiOperation(value = "商品列表")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@Valid @RequestBody QueryCommodityForm commodityForm) {
        commodityForm.setTime();

        if(commodityForm.getKey() != null && CorrespondEnum.getName(commodityForm.getKey()) == null){
            return CommonResult.error(444,"该条件无法搜索");
        }
        commodityForm.setKey(CorrespondEnum.getName(commodityForm.getKey()));

        List list = new ArrayList();
        //获取表头信息
        Class<CommodityFormVO> commodityFormVOClass = CommodityFormVO.class;
        Field[] declaredFields = commodityFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                Map map = new HashMap<>();
                map.put("key", declaredField.getName());
                map.put("name", annotation.value());
                list.add(map);
            }
        }
        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<CommodityFormVO> page = this.commodityService.findByPage(commodityForm);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
        }else {
            CommonPageResult<CommodityFormVO> pageVO = new CommonPageResult(page);
            map1.put("pageInfo", pageVO);
        }

        return CommonResult.success(map1);
    }

    @ApiOperation(value = "增加或修改商品")
    @PostMapping(value = "/saveOrUpdateCommodity")
    public CommonResult saveOrUpdateCommodity(@Valid @RequestBody AddCommodityForm form) {
        boolean result = this.commodityService.saveOrUpdateCommodity(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"商品插入或修改失败");
    }

    @ApiOperation(value = "根据id获取商品信息")
    @PostMapping(value = "/findCommodityById")
    public CommonResult<CommodityVO> findCommodityById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        return CommonResult.success(commodityService.findCommodityById(id));
    }

    @ApiOperation(value = "根据id获取商品信息")
    @PostMapping(value = "/findCommodityDetailById")
    public CommonResult<CommodityDetailVO> findCommodityDetailById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CommodityDetailVO commodityDetailVO = commodityService.findCommodityDetailById(id);
        return CommonResult.success(commodityDetailVO);
    }

    @ApiOperation(value = "审核商品")
    @PostMapping(value = "/reviewCommodity")
    public CommonResult reviewCommodity(@RequestBody AddReviewCommodityForm form) {
        boolean result = this.commodityService.reviewCommodity(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"商品审核失败");
    }

    @ApiOperation(value = "批量审核商品")
    @PostMapping(value = "/reviewCommodities")
    public CommonResult reviewCommodities(@RequestBody AddReviewCommodityForm form) {
        boolean result = this.commodityService.reviewCommodities(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"商品批量审核失败");
    }

    @ApiOperation(value = "税收归类")
    @PostMapping(value = "/taxClassification")
    public CommonResult taxClassification(@RequestBody TaxCommodityForm form) {
        boolean result = this.commodityService.taxClassification(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"商品税收归类失败");

    }

    /**
     * 导出入库商品模板
     */
    @ApiOperation(value = "导出商品模板")
    @GetMapping(value = "/exportCommodityTemplate")
    public void exportInProductTemplate( HttpServletResponse response) throws IOException {
//        ExcelUtils.exportSinglePageHeadExcel("商品模板", AddCommodityModelForm.class,response);

        ExcelWriter excelWriter = null;
        OutputStream outputStream = null;
        String fileName = "商品模板";
        try {
            outputStream = response.getOutputStream();
            fileName = fileName + ".xlsx";
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
            excelWriter = EasyExcel.write(response.getOutputStream(), AddCommodityModelForm.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("入库商品").build();
            List<AddCommodityModelForm> commodityModelForms = new ArrayList<>();
            excelWriter.write(commodityModelForms, writeSheet);
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

    @ApiOperation(value = "上传文件-导入商品信息")
    @RequestMapping(value = "/importExcelByCustomerGoods", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperationSupport(order = 4)
    public CommonResult importExcelByCustomerGoods(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);

        //配置别名
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("商品型号","skuModel");
        aliasMap.put("商品名称","skuName");
        aliasMap.put("报关名称","skuNameHs");
        aliasMap.put("单位","skuUnit");
        aliasMap.put("品牌","skuBrand");
        aliasMap.put("产地","skuOrigin");
        aliasMap.put("商品描述","skuNotes");
        aliasMap.put("配件","accessories");
        aliasMap.put("单位净重","unitNw");
        aliasMap.put("参考价","referencePrice");
        aliasMap.put("料号","itemNo");
        aliasMap.put("备注","remark");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<AddCommodityModelForm> list= excelReader.read(0, 1, AddCommodityModelForm.class);

        log.warn("导入的数据"+list);

        boolean result = commodityService.addCommodity(list);
        if(result){
            return CommonResult.success(list);
        }
        return CommonResult.error(444,"导入数据插入失败");
    }

}

