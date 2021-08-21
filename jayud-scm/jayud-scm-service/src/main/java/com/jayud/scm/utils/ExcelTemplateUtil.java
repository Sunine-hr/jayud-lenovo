package com.jayud.scm.utils;

import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * ExcelTemplateUtil导出模板工具类
 */
@Slf4j
public class ExcelTemplateUtil {

    /**
     * 下载模板文件
     * @param response 响应
     * @param inFileName 输入文件名
     * @param outFileNam 输出文件名
     */
    public void downloadExcel(HttpServletResponse response, String inFileName, String outFileNam) {
        InputStream inputStream = null;
        try {
            response.reset();
            //设置输出文件格式
            response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String(outFileNam.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            String name = StringUtils.toUtf8String(outFileNam);//转utf-8，url编码解码
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            ServletOutputStream outputStream = response.getOutputStream();
            inputStream = this.getClass().getResourceAsStream("/template/"+inFileName);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, length);
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭资源出错" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}