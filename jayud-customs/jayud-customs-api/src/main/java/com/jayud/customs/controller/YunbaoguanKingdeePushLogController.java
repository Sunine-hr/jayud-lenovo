package com.jayud.customs.controller;


import com.alibaba.fastjson.JSON;
import com.jayud.customs.model.po.YunbaoguanKingdeePushLog;
import com.jayud.customs.service.IYunbaoguanKingdeePushLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 云报关到金蝶推送日志 前端控制器
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-22
 */
@RequestMapping("/yunbaoguanKingdeePushLog")
@RestController
@Slf4j
@Api(tags = "云报关到金蝶推送日志接口")
public class YunbaoguanKingdeePushLogController {

    @Autowired
    IYunbaoguanKingdeePushLogService yunbaoguanKingdeePushLogService;

    @RequestMapping(path = "/saveOrOpdateLog", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新-云报关到金蝶推送日志")
    public Boolean saveOrOpdateLog(@RequestBody String msg){
        YunbaoguanKingdeePushLog log = JSON.parseObject(msg, YunbaoguanKingdeePushLog.class);
        return yunbaoguanKingdeePushLogService.saveOrUpdate(log);
    }

}

