package com.jayud.mall;

import cn.hutool.core.bean.BeanUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.jayud.mall.model.vo.MarkVO;
import com.jayud.mall.model.vo.OrderWarehouseNoVO;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * pdf测试
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
public class pdfTest {

    /**
     * 测试导出pdf
     */
    @Test
    public void test(){
        fillTemplate();
    }

    /**
     * 测试页面大小
     */
    @Test
    public void testsize(){
        fillSize();
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

    /**
     * hutool
     * BeanUtil测试Object 转 Map
      */
    @Test
    public void objectConvertMap(){

        OrderWarehouseNoVO vo = new OrderWarehouseNoVO();
        vo.setWarehouseNo("1111");
        vo.setSailTime("2020-11-11");
        vo.setJcTime("2018-12-23");

        List<MarkVO> markList = new ArrayList<>();
        MarkVO markVO = new MarkVO();
        markVO.setWarehouseNo("11112");
        markVO.setCartonNo("ddddd");
        markList.add(markVO);
        vo.setMarkList(markList);

        System.out.println("Object对象：" + vo);
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.copyProperties(vo, map);//cn.hutool.core.bean  obj-转->map
        System.out.println("Map对象：" + map);

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

            Rectangle pageSize = new Rectangle(283.50F,283.50F); // 100mm*100mm
            Document doc = new Document(pageSize,30,30,50,50);
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

    /**
     * 设置页面大小
     */
    public static void fillSize() {
        // 生成的新文件路径
        String newPDFPath = "D:/pdf_model/ceshi—size.pdf";
        FileOutputStream out = null;// 输出流
        try {
            out = new FileOutputStream(newPDFPath);
            Rectangle pageSize = new Rectangle(283.50F,283.50F); // 100mm*100mm
            Document doc = new Document(pageSize,30,30,50,50);//设置页面的大小
            PdfWriter.getInstance(doc, out);
            doc.open();
            doc.add(new Paragraph("1"));//在文档上写“Hello World”
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }


}
