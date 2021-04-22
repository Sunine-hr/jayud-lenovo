package com.jayud.file.service;

import cn.hutool.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    JSONObject upLoadImage(MultipartFile file);

    /**
     * 上传
     * @param file
     * @return
     */
    JSONObject upLoadFile(MultipartFile file);

    /**
     * 根据url创建二维码
     * @param url
     * @return
     */
    JSONObject createQrCode(String url);

    /**
     * 删除文件
     * @param filePath
     */
    void deleteFile(String filePath);
}
