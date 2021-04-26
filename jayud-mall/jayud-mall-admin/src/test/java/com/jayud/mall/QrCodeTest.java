package com.jayud.mall;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.junit.Test;

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
                FileUtil.file("d:/aaa/qrcode.jpg"));


    }


    @Test
    public void createQrCode2(){
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        QrCodeUtil.generate(
                "http://192.168.3.25:8081/#/addh5?id=111122222",
                300,
                300,
                FileUtil.touch("d:/aaa/111/qrcode.jpg"));

    }

    @Test
    public void test3(){
        String originalFilename = "运单 10001923.xls";
        String substring = originalFilename.substring("运单 ".length(), originalFilename.length() - ".xls".length());
        System.out.println(substring);
    }

    /**
     * 数字转字符串前面自动补0的实现
     */
    @Test
    public void test4(){
        int youNumber = 99;
        String str = String.format("%04d", youNumber);
        System.out.println(str); // 0001
    }

}
