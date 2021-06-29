package com.jayud.mall;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * pdf测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class pdfTest {

    @Test
    public void test(){
        fillTemplate();
    }

    /**
     * 测试-获取pdf模板资源
     */
    @Test
    public void testFile(){
        try {
            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_warehouse_no.pdf");
            InputStream inputStream = classPathResource.getInputStream();
            System.out.println(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(true);
    }


    // 利用模板生成pdf
    public static void fillTemplate() {
        // 模板路径
        String templatePath = "D:/pdf_model/pdf_model.pdf";
        // 生成的新文件路径
        String newPDFPath = "D:/pdf_model/ceshi.pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            String[] str = { "123456789", "TOP__ONE", "男", "1991-01-01", "130222111133338888", "河北省保定市" };
            int i = 0;
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next().toString();
                System.out.println(name);
                form.setField(name, str[i++]);
            }
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException e) {
            System.out.println(1);
        } catch (BadPdfFormatException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            System.out.println(2);
        }

    }


}
