package com.jayud.mall.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.vo.CustomerGoodsVO;
import com.jayud.mall.service.ICustomerGoodsService;
import com.jayud.mall.utils.ExcelTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customergoods")
@Api(tags = "C端-客户商品表接口")
public class CustomerGoodsController {

    @Autowired
    ICustomerGoodsService customerGoodsService;

    @ApiOperation(value = "分页查询客户商品")
    @PostMapping("/findCustomerGoodsByPage")
    public CommonResult<CommonPageResult<CustomerGoodsVO>> findCustomerGoodsByPage(@RequestBody QueryCustomerGoodsForm form) {
        IPage<CustomerGoodsVO> pageList = customerGoodsService.findCustomerGoodsByPage(form);
        CommonPageResult<CustomerGoodsVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存商品信息")
    @PostMapping("/saveCustomerGoods")
    public CommonResult<CustomerGoodsVO> saveCustomerGoods(@RequestBody CustomerGoodsForm form){
        CustomerGoodsVO customerGoodsVO = customerGoodsService.saveCustomerGoods(form);
        return CommonResult.success(customerGoodsVO);
    }

    @ApiOperation(value = "下载Excel模板-客户商品")
    @RequestMapping(value = "/downloadExcelTemplateByCustomerGoods", method = RequestMethod.GET)
    public void downloadExcelTemplateByCustomerGoods(HttpServletResponse response){
        new ExcelTemplateUtil().downloadExcel(response, "customer_goods.xlsx", "客户商品导入模板.xlsx");
    }

    @ApiOperation(value = "上传文件-导入客户商品")
    @RequestMapping(value = "/importExcelByCustomerGoods", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<CustomerGoodsVO>> importExcelByCustomerGoods(@RequestParam("file") MultipartFile file){
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
        aliasMap.put("*SKU","sku");
        aliasMap.put("*商品名称(中文)","nameCn");
        aliasMap.put("商品名称(英文)","nameEn");
        aliasMap.put("商品图片","imageUrl");
        aliasMap.put("条形码","barcode");
        aliasMap.put("*商品重量(KG)","weight");
        aliasMap.put("长(cm)","length");
        aliasMap.put("宽(cm）","width");
        aliasMap.put("高(cm)","height");
        aliasMap.put("*商品类型","typesName");
        aliasMap.put("*申报价值","declaredValue");
        aliasMap.put("*申报货币","declaredCurrency");
        aliasMap.put("海关编码","hsCode");
        aliasMap.put("销售链接","salesLink");
        aliasMap.put("销售价格","salesPrice");
        aliasMap.put("销售货币","salesPriceCurrency");

        excelReader.setHeaderAlias(aliasMap);

        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<CustomerGoodsVO> list= excelReader.read(0, 1, CustomerGoodsVO.class);

        return CommonResult.success(list);
    }


}
