package com.jayud.file.controller;

import cn.hutool.json.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.file.config.FdfsConfig;
import com.jayud.file.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


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

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile")
    @ApiOperation(value = "上传文件", httpMethod = "POST")
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResult.error(1, "上传文件内容为空!");
        }
        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            JSONObject jsonObject = uploadService.upLoadFile(file);
            return ApiResult.ok(jsonObject);
        } catch (Exception e) {
            log.error("upload file Exception!", e);
            return ApiResult.error(e.getMessage());
        }
    }


}









    



