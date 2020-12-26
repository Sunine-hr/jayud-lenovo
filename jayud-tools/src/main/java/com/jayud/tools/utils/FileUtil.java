package com.jayud.tools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * IO常用工具包
 * @author amosryan
 * @since 2010-06-03
 */
@Slf4j
public class FileUtil {
    /**
     * 从文件路径模板获取json对象以便后续处理
     *
     * @param filePath 格式 json/material.json 将从resources中尝试获取指定的json包下的文件
     * @return
     * @throws Exception
     */
    public static JSONObject getMapFromJsonFile(String filePath) throws Exception {
        //获取文件
        //获取物料数据模板
        String template = FileUtil.toString(new ClassPathResource(filePath));
        log.debug("template = {}", template);
        //带顺序的json，model顺序会相互影响，不能改变顺序
        //将template读到的模板数据尝试解析并放入LinkedHashMap中
        LinkedHashMap<String, Object> json = JSON.parseObject(template, LinkedHashMap.class, Feature.OrderedField);
        JSONObject basic = new JSONObject(true);
        //将LinkedHashMap数据装进JSONObject
        basic.putAll(json);
        return basic;
    }

    /**
     * 将输入流转换成字节流
     * @param input
     * @return
     * @throws Exception
     */
    public static byte[] toBytes(InputStream input) throws Exception {
        byte[] data = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int read = 0;
            while ((read = input.read(b)) > 0) {
                byteOut.write(b, 0, read);
            }
            data = byteOut.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return data;
    }

    /**
     * 将文件读取为一个字符串
     *
     * @param
     * @return
     * @throws Exception
     */
    public static String toString(File file) throws Exception {
        return toString(new FileInputStream(file));
    }

    /**
     * 将ClassPathResource读取的文件流转为字符串
     * @param classPathResource
     * @return
     * @throws Exception
     */
    public static String toString(ClassPathResource classPathResource) throws Exception {
        return  toString(classPathResource.getInputStream());
    }

    /**
     * 将输入流转换为一个串
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static String toString(InputStream input) throws Exception {
        return toStringWithLineBreak(input, null);
    }

    /**
     * 以指定编码格式将输入流按行置入一个List<String>
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static List<String> toLines(InputStream input, String encoding)
            throws Exception {
        InputStreamReader insreader = new InputStreamReader(input, encoding);
        BufferedReader bin = new BufferedReader(insreader);
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = bin.readLine()) != null) {
            lines.add(line);
        }
        bin.close();
        insreader.close();
        return lines;
    }

    /**
     * 以GBK格式将输入流按行置入一个List<String>
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static List<String> toLines(InputStream input) throws Exception {
        return toLines(input, "GBK");
    }

    /**
     * 转换为每行补充指定换行符(例如："/n"，"</br>")
     *
     * @param input
     * @param lineBreak
     * @return
     * @throws Exception
     */
    public static String toStringWithLineBreak(InputStream input,
                                               String lineBreak) throws Exception {
        List<String> lines = toLines(input);
        StringBuilder sb = new StringBuilder(20480);
        for (String line : lines) {
            sb.append(line);
            if (lineBreak != null) {
                sb.append(lineBreak);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串转出到指定文件
     * @param saveFile
     * @param content
     */
    public static void toFile(File saveFile, String content) {
        File parent = saveFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(saveFile));
            out.print(content);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 将一组文件打zip包
     *
     * @param srcFiles
     * @param targetFileName
     * @throws IOException
     */
    public static void filesToZip(List<File> srcFiles, String targetFileName)
            throws IOException {
        String fileOutName = targetFileName + ".zip";
        byte[] buf = new byte[1024];
        FileInputStream in = null;
        FileOutputStream fos = null;
        ZipOutputStream out = null;
        try {
            fos = new FileOutputStream(fileOutName);
            out = new ZipOutputStream(fos);
            for (File file : srcFiles) {
                in = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(file.getName()));
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (fos != null) {
                out.closeEntry();
                out.close();
                fos.close();
            }
        }
    }

}