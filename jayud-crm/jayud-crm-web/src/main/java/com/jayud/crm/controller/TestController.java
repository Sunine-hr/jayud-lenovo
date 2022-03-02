package com.jayud.crm.controller;


import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.crm.feign.AuthClient;
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
}
