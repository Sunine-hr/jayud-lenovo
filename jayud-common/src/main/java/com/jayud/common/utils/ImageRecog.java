package com.jayud.common.utils;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片识别
 */
@Slf4j
public class ImageRecog {


    /**
     * 条形码识别(定制版,六联单号)
     *
     * @param urlPath
     * @param type    1:本地,2:网络图片
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static String discernBarCode(String urlPath, Integer type) throws Exception {
        BufferedImage image = null;
        if (urlPath.endsWith(".pdf")){
            image=PDF2ImgUtil.pdf2Image(urlPath,2,500);
        }else {
            if (type == 1) {
                image = ImageIO.read(new File(urlPath));
            } else {
                URL url = new URL(urlPath);
                image = ImageIO.read(url);
            }
            if (image == null) {
                return null;
            }
        }

        int width = image.getWidth();
        int height = image.getHeight();
        String text = "";
        //截图部分(满铺情况)
//        BufferedImage pic2 = image.getSubimage(width / 2 + 200, height/2-320, width / 3, height / 8);
        BufferedImage pic2 = image.getSubimage(width / 2, height / 10, width - width / 2, height - height / 2);
//        File desImage = new File("./tmp.png");
//        ImageIO.write(pic2, "png", desImage);

        LuminanceSource source = new BufferedImageLuminanceSource(pic2);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = new HashMap<>();
        // 解码设置编码方式为：utf-8，
        hints.put(DecodeHintType.CHARACTER_SET, "GBK");
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        try {
            Result result = new MultiFormatReader().decode(bitmap, hints);
            System.out.println(result.getText());
            text = result.getText();
        } catch (NotFoundException e) {
            log.warn("识别条形失败,更改其他方案");
        }
        String reg = "^(\\d{13})$";
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(text);
        if (!m.matches()) {
            text = "";
        }

        if (StringUtils.isEmpty(text)) {
            String base64 =ImageProcessing.imgToBase64(pic2);
            System.out.println("转换base64 :" + base64);
            String response = AlibabaOcrUtil.characterRecognition(base64,"f3b8f4766eda484b93bf1fbb6a11d462");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray prism_wordsInfo = jsonObject.getJSONArray("prism_wordsInfo");

            for (int i = 0; i < prism_wordsInfo.size(); i++) {
                JSONObject tmp = prism_wordsInfo.getJSONObject(i);
                if (tmp == null) continue;
                r = Pattern.compile(reg);
                m = r.matcher(tmp.getStr("word"));
                if (m.matches()) {
                    text = tmp.getStr("word");
                    break;
                }
            }
            System.out.println("请求阿里云识别:" + text);
        }

        return text;
    }


    public static void main(String[] args) throws Exception {
//        discernBarCode("http://test.oms.jayud.com:9448/group1/M00/00/24/wKgA52E2wwuAVUrzAA6mT89xGY8048.jpg",2);
//        discernBarCode("D:\\ideaProject\\security\\kooko-core\\src\\main\\resources\\picture\\六联单号7.jpg", 1);
        discernBarCode("http://test.oms.jayud.com:9448/group1/M00/00/24/wKgA52E5pbiAB2zwAAEeEbJJa6E233.pdf", 2);
    }
}
