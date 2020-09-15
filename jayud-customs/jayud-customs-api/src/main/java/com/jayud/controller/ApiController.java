package com.jayud.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.jayud.annotations.APILog;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.model.bo.*;
import com.jayud.model.vo.*;
import com.jayud.service.ICustomsApiService;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * @author william
 * @description
 * @Date: 2020-09-07 15:14
 */
@RequestMapping("/customs")
@Api(tags = "云报关接口")
@RestController
public class ApiController {
    @Autowired
    ICustomsApiService service;

    @ApiOperation(value = "云报关登录（强制从新登录）")
    @PostMapping("/login")
    public CommonResult login(@Valid @RequestBody LoginForm form) {
        service.login(form);
        return CommonResult.success();
    }


    //委托单上传部分
    @ApiOperation(value = "委托单上传")
    @PostMapping("/order/push")
    public CommonResult pushOrder(@Valid @RequestBody PushOrderForm form) {
        return CommonResult.success(service.pushOrder(form));
    }

    @ApiOperation(value = "上传委托单的附件")
    @PostMapping("/order/appendix/push")
    public CommonResult pushAppendix(@RequestBody @Valid PushAppendixForm form, MultipartFile file) {
        try {
            String encode = Base64.encode(file.getInputStream());
            if (StringUtils.isBlank(encode)) {
                Asserts.fail(ResultEnum.PARAM_ERROR, "文件转码异常，上传失败");
            }
            form.setData(encode);
        } catch (IOException e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.PARAM_ERROR, "文件转码异常，上传失败");
        }
        service.pushAppendix(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "查询委托单")
    @PostMapping("/order/find")
    public CommonResult<FindOrderInfoVO> findOrderInfo(@Valid @RequestBody FindOrderInfoWrapperForm form) {
        return CommonResult.success(service.findOrderInfo(form));
    }

    @ApiOperation(value = "委托单明细查询")
    @PostMapping("/order/detail/find")
    public CommonResult<FindOrderDetailVO> findOrderDetail(@RequestBody Map<String, Object> param) {
        String uid = MapUtil.getStr(param, "uid");
        if (StringUtils.isBlank(uid)) {
            return CommonResult.success(null);
        }
        return CommonResult.success(service.findOrderDetail(uid));
    }

    //todo swagger的map类型处理注解修改
    @ApiOperation(value = "报关单下载")
    @PostMapping("/declaration/download")
    public CommonResult<DownloadCustomsDeclarationVO> DownloadCustomsDeclaration(@RequestBody Map<String, String> param) {
        if (CollectionUtil.isEmpty(param)) {
            return CommonResult.success();
        }
        String id = MapUtil.getStr(param, "id");
        String idType = MapUtil.getStr(param, "idType");
        if (StringUtils.isBlank(id) || StringUtils.isBlank(idType)) {
            return CommonResult.success();
        }
        return CommonResult.success(service.DownloadCustomsDeclaration(id, idType));
    }

    @ApiOperation(value = "报关进程查询")
    @PostMapping("/declaration/process/info")
    public CommonResult<DclarationProcessStepVO> getDeclarationProcessStep(@RequestBody Map<String, String> param) {
        if (CollectionUtil.isEmpty(param)) {
            return CommonResult.success();
        }
        String id = MapUtil.getStr(param, "id");
        if (StringUtils.isBlank(id)) {
            return CommonResult.success();
        }
        return CommonResult.success(service.getDeclarationProcessStep(id));
    }

    @ApiOperation(value = "委托单操作进程查询")
    @PostMapping("/order/process/info")
    public CommonResult<OrderProcessStepVO> getOrderProcessStep(@RequestBody Map<String,String> param){
        if (CollectionUtil.isEmpty(param)) {
            return CommonResult.success();
        }
        String id = MapUtil.getStr(param, "id");
        if (StringUtils.isBlank(id)) {
            return CommonResult.success();
        }
        return CommonResult.success(service.getOrderProcessStep(id));
    }

    @ApiOperation(value = "test")
    @PostMapping("/test")
    @APILog
    public CommonResult<OrderProcessStepVO> test(@RequestBody Map<String, String> param) {
        System.out.println(JSONUtil.toJsonStr(param));
        return CommonResult.success();
    }

}
