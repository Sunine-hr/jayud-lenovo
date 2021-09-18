package com.jayud.common.utils;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片处理
 */
public class ImageProcessing {

    public static String imgToBase64(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(bufferedImage, "png", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String pngBase64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        pngBase64 = pngBase64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        return pngBase64;
    }
}
