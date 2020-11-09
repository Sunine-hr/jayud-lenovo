package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomsDataForm;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.vo.CustomsDataVO;
import com.jayud.mall.service.ICustomsDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customsdata")
@Api(tags = "报关资料接口")
public class CustomsDataController {

    @Autowired
    ICustomsDataService customsDataService;

    @ApiOperation(value = "分页查询报关资料")
    @PostMapping("/findCustomsDataByPage")
    public CommonResult<CommonPageResult<CustomsDataVO>> findCustomsDataByPage(@RequestBody QueryCustomsDataForm form) {
        IPage<CustomsDataVO> pageList = customsDataService.findCustomsDataByPage(form);
        CommonPageResult<CustomsDataVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存报关资料")
    @PostMapping("/saveCustomsData")
    public CommonResult saveCustomsData(@RequestBody CustomsDataForm form){
        return customsDataService.saveCustomsData(form);
    }


}
