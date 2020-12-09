package com.jayud.file.controller;

import com.jayud.common.ApiResult;
import com.jayud.file.config.FdfsConfig;
import com.jayud.file.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "file对外接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private FdfsConfig uploadProperties;
    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "获取根路径")
    @RequestMapping(value = "/api/getBaseUrl")
    public ApiResult getBaseUrl() {
        String baseUrl = uploadProperties.getBaseUrl();
        if (baseUrl != null) {
            return ApiResult.ok(baseUrl);
        }
        return ApiResult.error();
    }

}









    



