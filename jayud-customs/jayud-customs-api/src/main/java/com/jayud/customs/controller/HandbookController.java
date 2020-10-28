package com.jayud.customs.controller;

import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.customs.service.CustomsHandbookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/handbook")
@Api(tags = "报关-手册服务接口")
public class HandbookController {
    @Autowired
    CustomsHandbookService handbookService;

    @ApiOperation(value = "接收客户的excel并返回一个经过敏感词筛查的excel")
    @PostMapping("/process/name/excel")
    public void processExcel(@RequestPart(name = "file") MultipartFile file, HttpServletResponse response) {
        //todo  后续完善：可能需要根据客户名获取相应的接收实体类名，进行处理
        String customerName = "Xianda";
        String className = "BlacklistCheck" + customerName + "Form";

        //处理请求
        try {
            handbookService.processHandbookBlacklistCheck(className, file, response);
        } catch (Exception e) {
            e.printStackTrace();
            Asserts.fail(ResultEnum.INTERNAL_SERVER_ERROR,"转换异常");
        }
    }

}
