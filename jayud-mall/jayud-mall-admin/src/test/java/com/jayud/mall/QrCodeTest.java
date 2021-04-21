package com.jayud.mall;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;

public class QrCodeTest {

    /**
     * 根据url，生成二维码图片，微信扫描图片访问url
     *
     * http://192.168.3.25:8081/#/addh5?id=111122222        url
     * d:/qrcode.jpg                                        目录{.jpg}
     */
    @Test
    public void createQrCode(){
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        QrCodeUtil.generate(
                "http://192.168.3.25:8081/#/addh5?id=111122222",
                300,
                300,
                FileUtil.file("d:/qrcode.jpg"));
        final BufferedImage image = QrCodeUtil.generate(
                "http://192.168.3.25:8081/#/addh5?id=111122222", 300, 300);
        System.out.println(image);

    }

}
