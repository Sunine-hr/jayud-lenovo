package com.jayud.common.utils;

import cn.afterturn.easypoi.word.WordExportUtil;
import com.alibaba.excel.EasyExcel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WordUtil {

    /**
     *
     *
     * @param templateWordPath
     * @param dataMap
     * @return
     * @throws Exception
     */
    /**
     * 导出word（2007版本docx）
     *
     * @param templateWordPath 模板地址
     * @param fileName         文件名称
     * @param tarDir           目标文件
     * @param dataMap          数据
     * @param response
     * @throws Exception
     */
    public static void exportWord(String templateWordPath,
                                  String fileName, String tarDir, Map<String, Object> dataMap,
                                  HttpServletResponse response) throws Exception {
        File tf = new File(templateWordPath);
        if (!tf.exists() || !tf.isFile()) {
            throw new RuntimeException("File [" + templateWordPath + "] Not Found Or Not File.");
        }
        XWPFDocument document = WordExportUtil.exportWord07(templateWordPath, dataMap);

        if (response != null) { //网络流下载
//            response.setContentType("multipart/form-data");
            String tmpPath = tarDir + fileName;
            FileOutputStream fos = new FileOutputStream(tmpPath);
            document.write(fos);
            //设置强制下载不打开
            response.setContentType("application/force-download");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".docx");
            ServletOutputStream out = response.getOutputStream();
            document.write(out);
            out.close();
            if (!StringUtils.isEmpty(tarDir)) {
                delFileWord(tarDir, fileName);
            }

        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            document.write(bos);
            FileOutputStream fos = new FileOutputStream(tarDir);
            fos.write(bos.toByteArray());
            fos.flush();
        }

    }

    /**
     * 删除零时生成的文件
     */
    public static void delFileWord(String filePath, String fileName) {
        File file = new File(filePath + fileName);
        file.delete();
    }

    public static void main(String[] args) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("boxNum", "ZGJK202108001");
        dataMap.put("docType", "海运/出口");
        dataMap.put("filingDate", "2021-08");
        dataMap.put("number", 1);
        dataMap.put("createUser", "张三");
        dataMap.put("createTime", "2021年8月7日");

//        byte[] doc = exportWord("D:\\公司\\模板\\报关归档\\customsFiling.docx", dataMap);

    }
}

