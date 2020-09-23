package com.jayud.file.service.impl;

import cn.hutool.json.JSONObject;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.jayud.file.config.FdfsConfig;
import com.jayud.file.enums.ExceptionEnum;
import com.jayud.file.exception.FileException;
import com.jayud.file.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


/**
 * @author van
 */
@Slf4j
@Service
@Configuration
@EnableConfigurationProperties(FdfsConfig.class)
public class UploadServiceImpl implements UploadService {
    private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig; //fdfs缩略图工具类
    private FdfsConfig uploadProperties;

    public UploadServiceImpl(FdfsConfig uploadProperties) {
        this.uploadProperties = uploadProperties;
    }


    @Override
    public JSONObject upLoadImage(MultipartFile file) {
        //校验文件是否是图片
        try {
            String contentType = file.getContentType();
            if (!uploadProperties.getAllowImageTypes().contains(contentType)) {
                logger.info("上传的不是图片文件" + contentType);
                throw new FileException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容是否符合图片规范
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read == null) {
                logger.info("上传的文件不符合规范");
                throw new FileException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //将文件写入对应的储存地址
            // 2、将图片上传到FastDFS
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 2.2、上传

            StorePath storePath = fastFileStorageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);

            log.info("原图完整访问路径：" + uploadProperties.getBaseUrl() + "/" + storePath.getFullPath());

            // 2.2、上传并生成缩略图
//            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
//                    file.getInputStream(), file.getSize(), extension, null);
//            log.info("缩略图图完整访问路径：" + uploadProperties.getBaseUrl() + thumbImageConfig.getThumbImagePath(storePath.getFullPath()));

            // 2.3、返回完整路径
            JSONObject jsonObject = new JSONObject();
            Objects.requireNonNull(jsonObject.put("filePath", uploadProperties.getBaseUrl() + "/" + storePath.getFullPath()));
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public JSONObject upLoadFile(MultipartFile file) {
        try {
//            String contentType = file.getContentType();
//            if (!uploadProperties.getAllowTypes().contains(contentType)) {
//                logger.info("上传的不是图片文件" + contentType);
//                throw new FileException(ExceptionEnum.INVALID_FILE_TYPE);
//            }
            //校验文件内容是否符合图片规范
//            BufferedImage read = ImageIO.read(file.getInputStream());
//            if (read == null) {
//                logger.info("上传的文件不符合规范");
//                throw new FileException(ExceptionEnum.INVALID_FILE_TYPE);
//            }
            //将文件写入对应的储存地址
            // 2、将文件上传到FastDFS
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 2.2、上传
            StorePath storePath = fastFileStorageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);
            log.info("文件完整访问路径：" + uploadProperties.getBaseUrl() + "/" + storePath.getFullPath());

            // 2.3、返回完整路径
            JSONObject jsonObject = new JSONObject();
            Objects.requireNonNull(jsonObject.put("filePath", uploadProperties.getBaseUrl() + "/" + storePath.getFullPath()));
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }

    /**
     * 删除图片
     *
     * @param filePath
     * @return
     */
    @Override
    public void deleteFile(String filePath) {
        if (StringUtils.isNotEmpty(filePath)) {
            this.fastFileStorageClient.deleteFile(filePath);
        }
    }
}
