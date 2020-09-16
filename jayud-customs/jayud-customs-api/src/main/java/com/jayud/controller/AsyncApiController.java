package com.jayud.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.jayud.customs.service.IGeneralApiLogService;
import com.jayud.model.po.GeneralApiLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * 异步数据处理中心（供feign暴露的接口使用）
 *
 * @author william
 * @description
 * @Date: 2020-09-11 09:41
 */
@RequestMapping("/api/customs")
@Api(tags = "异步数据处理中心（供feign暴露的接口使用）")
@RestController
@Slf4j
public class AsyncApiController {
    @Autowired
    IGeneralApiLogService logService;

        @PostMapping("/generalLog/save")
    @ApiOperation(value = "保存接口调用日志")
    public void saveLog(@RequestBody Map<String, String> param) {
        if (CollectionUtil.isEmpty(param)) {
            log.error("保存接口调用日志失败：数据为空");
            return;
        }
        String logInfo = MapUtil.getStr(param, "logInfo");
        if (null == logInfo || Objects.equals(logInfo, "")) {
            log.error("保存接口调用日志失败：数据为空");
            return;
        }
        GeneralApiLog generalApiLog = null;
        try {
            generalApiLog = JSONUtil.toBean(logInfo, GeneralApiLog.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存接口调用日志失败：数据解析失败");
            return;
        }
        logService.save(generalApiLog);
    }
}
