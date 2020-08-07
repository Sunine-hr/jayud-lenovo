package com.jayud.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传工具
 *
 * @author Satellite
 */
@Component
public class UploadUtil {

    /**
     * 处理文件上传
     *
     * @param file
     * @param pathName 存放文件的目录的绝对路径 servletContext.getRealPath("/upload")
     * @return
     * @throws IOException
     */
    public static String upload(MultipartFile file, String pathName) throws IOException {
        // 将Properties和流关联
        String orgFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(orgFileName);
        try {
            File targetFile = new File(pathName, fileName);
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static File downLoad(String url) {
        try {
            String filePath = url;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            return myDelFile;
        } catch (Exception e) {
            System.out.println("下载文件操作出错");
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();

        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();

        }
    }


}