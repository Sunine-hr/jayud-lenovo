package com.jayud.trailer.feign;


import com.jayud.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * customs模块消费file模块的接口
 */
@FeignClient(value = "jayud-file-web")
public interface FileClient {

    /**
     * 获取根路径
     */
    @RequestMapping(value = "/api/getBaseUrl")
    ApiResult getBaseUrl();


    /**
     * 上传文件
     *
     * @param file
     */
    @RequestMapping(value = "/api/uploadFile")
    ApiResult uploadFile(@RequestParam("file") MultipartFile file);
}
