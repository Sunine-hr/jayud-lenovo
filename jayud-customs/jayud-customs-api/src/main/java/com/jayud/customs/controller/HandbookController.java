package com.jayud.customs.controller;

import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.customs.model.po.CustomsHandbookBlacklist;
import com.jayud.customs.service.CustomsHandbookService;
import com.jayud.customs.service.ICustomsHandbookBlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/customs/otherbusiness")
@Api(tags = "报关-手册服务接口")
public class HandbookController {
    @Autowired
    CustomsHandbookService handbookService;
    @Autowired
    ICustomsHandbookBlacklistService blacklistService;

    @ApiOperation(value = "接收客户的excel并返回一个经过敏感词筛查的excel")
    @PostMapping("/blacklist/name/excel")
    public void processExcel(@RequestPart(name = "file") MultipartFile file, HttpServletResponse response) {
        //todo  后续完善：可能需要根据客户名获取相应的接收实体类名，进行处理
//        String customerName = "Xianda";
//        String className = "BlacklistCheck" + customerName + "Form";

        String customerName = "Default";
        String className = "BlacklistCheck" + customerName + "Form";


        //处理请求
        try {
            handbookService.processHandbookBlacklistCheck(className, file, response);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.INTERNAL_SERVER_ERROR, "转换异常");
        }
    }

    /**
     * 增加
     *
     * @param param
     * @return
     */
    @PostMapping("/blacklist/add")
    @ApiOperation(value = "增加黑名单")
    public CommonResult addBlackList(@RequestBody String param) {
        JSONObject reqForm = null;
        try {
            reqForm = JSONObject.parseObject(param);
        } catch (Exception exception) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "请求参数格式不对：" + param);
        }
        if (null == reqForm) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "数据解析失败");
        }

        Map<String, Object> innerMap = reqForm.getInnerMap();

        String replacement = innerMap.get("replacement").toString();
        String name = innerMap.get("name").toString();
        String userName = innerMap.get("userName").toString();

        CustomsHandbookBlacklist blacklist = new CustomsHandbookBlacklist();
        blacklist.setName(name);
        blacklist.setReplacement(replacement);
        blacklist.setCreateUser(userName);
        blacklist.setCreateTime(LocalDateTime.now());

        try {
            blacklistService.save(blacklist);
        } catch (Exception exception) {
            if (exception.getMessage().contains("Duplicate entry")) {
                return CommonResult.error(ResultEnum.PARAM_ERROR, "写入失败：黑名单关键字：" + name + "已经存在");
            }
            return CommonResult.error(ResultEnum.PARAM_ERROR, "写入失败");
        }
        return CommonResult.success("写入成功：黑名单关键字：" + name + "，替换词：" + replacement + "，上传成功！");

    }

}
