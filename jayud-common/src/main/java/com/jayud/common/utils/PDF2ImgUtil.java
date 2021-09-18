package com.jayud.common.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDF2ImgUtil {


    /**
     * pdf文件转图片
     *
     * @param filePath
     * @param type     读取类型 1:本地 2网络
     * @param dpi
     * @return
     * @throws Exception
     */
    public static BufferedImage pdf2Image(String filePath, Integer type, int dpi) throws Exception {
        PDDocument pdDocument = null;
        if (type == 1) {
            File file = new File(filePath);
            pdDocument = PDDocument.load(file);
        } else {
            URL url = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            pdDocument = PDDocument.load(inputStream);
        }

        PDFRenderer renderer = new PDFRenderer(pdDocument);
        BufferedImage image = renderer.renderImageWithDPI(0, dpi);

//        imOut = ImageIO.createImageOutputStream(bs);
//        File desImage = new File("./tmp1.png");
//        ImageIO.write(image, "png", desImage);
//        image.flush();
//        is = new ByteArrayInputStream(bs.toByteArray());
//        is.close();
        return image;
    }


    /**
     * pdf文件转图片
     *
     * @return
     * @throws Exception
     */
    public static InputStream pdf2Image2(String filePath, Integer type,int dpi) throws Exception {
        File file = new File(filePath);
        InputStream is = null;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut;
        PDDocument pdDocument = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        BufferedImage image = renderer.renderImageWithDPI(0, dpi);
        imOut = ImageIO.createImageOutputStream(bs);
//        File desImage = new File("./tmp1.png");
         ImageIO.write(image, "png", imOut);
        image.flush();
        is = new ByteArrayInputStream(bs.toByteArray());
        is.close();
        return is;
    }
    public static void main(String[] args) throws Exception {
//         pdf2Image("D:\\ideaProject\\security\\kooko-core\\src\\main\\resources\\picture\\富恩皇岗进口.pdf", 2, 500);
        pdf2Image("http://test.oms.jayud.com:9448/group1/M00/00/24/wKgA52E5pbiAB2zwAAEeEbJJa6E233.pdf", 2, 500);
    }


}
