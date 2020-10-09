package com.jayud.file.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.file.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * @author : hanbin
 * @date : 10:54:23
 * 文件处理
 */
@RestController
@RequestMapping("/file")
@Api(value = "上传接口", tags = "上传接口")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传图片",httpMethod = "POST")
    public ApiResult singleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResult.error(1, "上传文件内容为空!");
        }

        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            JSONObject jsonObject = uploadService.upLoadImage(file);
            return ApiResult.ok(jsonObject);
        } catch (Exception e) {
            logger.error("uploadImage Exception!", e);
            return ApiResult.error(e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传文件",httpMethod = "POST")
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
            logger.error("upload file Exception!", e);
            return ApiResult.error(e.getMessage());
        }
    }


    /**
     * 删除文件
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除文件", httpMethod = "POST")
    public ApiResult singleFileDelete(@RequestBody Map<String,Object> paramMap) {
        String param = MapUtil.getStr(paramMap,"absolutePath");
        if (param.isEmpty()) {
            return ApiResult.error(1, "文件地址不能为空!");
        }
        this.uploadService.deleteFile(param);
        return ApiResult.ok();
    }

}

