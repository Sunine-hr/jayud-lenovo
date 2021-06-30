package com.jayud.oceanship.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Value;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class PdfUtil {

    /**
     * 根据pdf模板输出流
     * @param path
     * @param
     * @param resultMap
     * @return
     */
    public static ByteArrayOutputStream createPdfStream(String path,
                                                        Map<String, String> resultMap,Map<String, String> imgmap,String waterMarkName) {

        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        try {

            PdfReader reader = new PdfReader(path);
            PdfStamper stamp = new PdfStamper(reader, ba);

            //使用字体
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            /* 获取模版中的字段 */
            AcroFields form = stamp.getAcroFields();

            //填充表单
            if (resultMap != null) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    form.setFieldProperty(entry.getKey(), "textfont", bf, null);
                    form.setField(entry.getKey(), entry.getValue());
                }
            }

            if(imgmap != null){
                for(String key : imgmap.keySet()) {
                    String value = imgmap.get(key);
                    String imgpath = value;
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    //根据路径读取图片
                    Image image = Image.getInstance(imgpath);
                    //获取图片页面
                    PdfContentByte under = stamp.getOverContent(pageNo);
                    //图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    //添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            }

            PdfGState gs = new PdfGState();
            //改透明度
            gs.setFillOpacity(0.8f);
            gs.setStrokeOpacity(0.8f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            label.setText(waterMarkName);

            if(waterMarkName != null){
                PdfContentByte under;
                // 添加一个水印
                for (int i = 1; i < total; i++) {
                    // 在内容上方加水印
                    under = stamp.getOverContent(i);
                    //在内容下方加水印
                    //under = stamper.getUnderContent(i);
                    gs.setFillOpacity(0.5f);
                    under.setGState(gs);
                    under.beginText();
                    //改变颜色
                    under.setColorFill(BaseColor.LIGHT_GRAY);
                    //改水印文字大小
                    under.setFontAndSize(bf, 30);
                    under.setTextMatrix(350, 50);
                    //后3个参数，x坐标，y坐标，角度
                    under.showTextAligned(Element.ALIGN_CENTER, waterMarkName, 400, 450, 55);

                    under.endText();
                }
            }



            stamp.setFormFlattening(true);//不能编辑
            stamp.close();


            reader.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }
        return ba;
    }

   //导出pdf加水印
    public void waterMark(String inputFile,String outputFile, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            PdfGState gs = new PdfGState();
            //改透明度
            gs.setFillOpacity(0.5f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            label.setText(waterMarkName);

            PdfContentByte under;
            // 添加一个水印
            for (int i = 1; i < total; i++) {
                // 在内容上方加水印
                under = stamper.getOverContent(i);
                //在内容下方加水印
                //under = stamper.getUnderContent(i);
                gs.setFillOpacity(0.5f);
                under.setGState(gs);
                under.beginText();
                //改变颜色
                under.setColorFill(BaseColor.LIGHT_GRAY);
                //改水印文字大小
                under.setFontAndSize(base, 150);
                under.setTextMatrix(70, 200);
                //后3个参数，x坐标，y坐标，角度
                under.showTextAligned(Element.ALIGN_CENTER, waterMarkName, 300, 350, 55);

                under.endText();
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
