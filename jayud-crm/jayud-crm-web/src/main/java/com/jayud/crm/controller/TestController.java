package com.jayud.crm.controller;


import com.jayud.auth.model.po.SysArea;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.service.ICrmCustomerFeaturesService;
import io.lettuce.core.ScriptOutputType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Api(tags = "测试信息")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    public ICrmCustomerFeaturesService crmCustomerFeaturesService;
    @PostMapping("/testFind")
    public BaseResult testFind(){
        BaseResult baseResult = authClient.selectListFeign();
        System.out.println("查询的数据内容："+baseResult);
        return BaseResult.ok(baseResult);
    }


    @PostMapping(value="/itemByDictCode")
    public BaseResult testDemo(@RequestParam String dictCode){
        BaseResult baseResult = authClient.selectItemByDictCodeFeign(dictCode);
        System.out.println("1232成功！"+baseResult);
        return BaseResult.ok(baseResult);
    }

    @GetMapping(value="/testdate")
    public BaseResult testdate(@RequestParam String dictCode){
        BaseResult orderFeign = authClient.getOrderFeign(dictCode, new Date());
        System.out.println("时间："+orderFeign);
        return BaseResult.ok(orderFeign);
    }


    @GetMapping(value="/testSave")
    public BaseResult testSave(@RequestParam(name="level",required=false) Integer level,@RequestParam(name="parentCode",required=false) Long parentCode){
        BaseResult baseResult = authClient.selectListSysAreaFeign(level,parentCode);
        System.out.println("省市级联："+baseResult);
        return BaseResult.ok(baseResult);
    }


    @GetMapping(value="/testCrmCustomerFeatures")
    public BaseResult testCrmCustomerFeatures(@RequestParam(name="custId") Long custId){
        crmCustomerFeaturesService.saveCrmCustomerFeatures(custId);
        return BaseResult.ok();
    }


    @ApiOperation("公共方法新增日志")
    @PostMapping("/api/addSysLogFeign")
    public BaseResult addFeign(@RequestParam("logContent") String logContent,@RequestParam("businessId") Long businessId) {
        authClient.addSysLogFeign(logContent, businessId);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }
}
