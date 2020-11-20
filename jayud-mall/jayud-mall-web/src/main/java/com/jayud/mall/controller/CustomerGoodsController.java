package com.jayud.mall.controller;

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

import javax.servlet.http.HttpServletResponse;

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


}
