package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.AddCurrencyManageForm;
import com.jayud.finance.bo.AddCurrencyRateForm;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.vo.CurrencyRateVO;
import com.jayud.finance.vo.InitComboxStrVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/currency/")
@Api(tags = "汇率管理")
public class CurrencyController {

    @Autowired
    ICurrencyRateService currencyRateService;

    @Autowired
    OmsClient omsClient;

    @ApiOperation(value = "汇率管理列表分页查询")
    @PostMapping("/findCurrencyRateByPage")
    public CommonResult<CommonPageResult<CurrencyRateVO>> findCurrencyRateByPage(@RequestBody QueryCurrencyRateForm form) {
        IPage<CurrencyRateVO> pageList = currencyRateService.findCurrencyRateByPage(form);
        CommonPageResult<CurrencyRateVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增汇率")
    @PostMapping("/saveCurrencyRate")
    public CommonResult saveCurrencyRate(@RequestBody AddCurrencyManageForm form) {
        return currencyRateService.saveCurrencyRate(form);
    }

    @ApiOperation(value = "编辑汇率")
    @PostMapping("/editCurrencyRate")
    public CommonResult editCurrencyRate(@RequestBody AddCurrencyRateForm form) {
        Boolean result = currencyRateService.editCurrencyRate(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "删除 id=列表id")
    @PostMapping("/delCurrencyRate")
    public CommonResult delCurrencyRate(@RequestBody Map<String,Object> param) {
        Long id = Long.parseLong(MapUtil.getStr(param,"id"));
        currencyRateService.removeById(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "币种下拉框")
    @PostMapping("/initCurrencyInfo")
    public CommonResult<List<InitComboxStrVO>> initCurrencyInfo() {
        return CommonResult.success(omsClient.initCurrencyInfo().getData());
    }





}
