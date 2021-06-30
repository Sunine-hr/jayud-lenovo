package com.jayud.mall.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderInfoParaForm;
import com.jayud.mall.model.bo.OrderPickIdForm;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.model.vo.MarkVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.OrderWarehouseNoVO;
import com.jayud.mall.service.IOrderPickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderpick")
@Api(tags = "C012-client-订单对应提货信息表接口")
@ApiSort(value = 12)
public class OrderPickController {

    @Autowired
    IOrderPickService orderPickService;

    @ApiOperation(value = "批量创建-订单对应提货地址(进仓单号)")
    @ApiOperationSupport(order = 1)
    @PostMapping("/createOrderPickList")
    public CommonResult<List<OrderPickVO>> createOrderPickList(@RequestBody List<DeliveryAddressVO> form){
        List<OrderPickVO> orderPickVOList = orderPickService.createOrderPickList(form);
        return CommonResult.success(orderPickVOList);
    }

    //提货地址-下载进仓单号
    @ApiOperation(value = "提货地址-下载进仓单号")
    @ApiOperationSupport(order = 2)
    @PostMapping("/downloadWarehouseNoByPickId")
    public CommonResult<OrderWarehouseNoVO> downloadWarehouseNoByPickId(@Valid @RequestBody OrderPickIdForm form){
        Long id = form.getId();
        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByPickId(id);
        return CommonResult.success(orderWarehouseNoVO);
    }

    //订单-下载进仓(不存在提货地址时)
    @ApiOperation(value = "订单-下载进仓单(不存在提货地址时)")
    @ApiOperationSupport(order = 3)
    @PostMapping("/downloadWarehouseNoByOrderId")
    public CommonResult<OrderWarehouseNoVO> downloadWarehouseNoByOrderId(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByOrderId(id);
        return CommonResult.success(orderWarehouseNoVO);
    }

    @ApiOperation(value = "提货地址-下载进仓单号(1进仓单)")
    @GetMapping("/downloadWarehouseNoByPickId1")
    @ApiOperationSupport(order = 4)
    public void downloadWarehouseNoByPickId1(@RequestParam(value = "id",required=false) Long id,
                             HttpServletRequest request, HttpServletResponse response) {
        try {
            OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByPickId(id);
            String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
            String add = orderWarehouseNoVO.getAdd();//目的仓库代码
            String json = JSON.toJSONString(orderWarehouseNoVO, SerializerFeature.DisableCircularReferenceDetect);
            JSONObject jsonObject = JSONObject.parseObject(json);
            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_warehouse_no.xlsx");
            InputStream inputStream = classPathResource.getInputStream();
            XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream);
            String sheetNamePrefix = "";
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                String sheetName = sheetNamePrefix + (i + 1);
                templateWorkbook.setSheetName(i, sheetName);
            }
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());
            String fileName = customerName+"_"+add+"_"+"进仓单";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                excelWriter.fill(jsonObject.getJSONArray("details"), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "提货地址-下载进仓单号(2箱唛)")
    @GetMapping("/downloadWarehouseNoByPickId2")
    @ApiOperationSupport(order = 5)
    public void downloadWarehouseNoByPickId2(@RequestParam(value = "id",required=false) Long id,
                                             HttpServletRequest request, HttpServletResponse response) {
        try {
            OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByPickId(id);
            String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
            String add = orderWarehouseNoVO.getAdd();//目的仓库代码
            List<MarkVO> markList = orderWarehouseNoVO.getMarkList();
            String json = JSON.toJSONString(markList, SerializerFeature.DisableCircularReferenceDetect);
            JSONArray jsonArray = JSONArray.parseArray(json);

            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_carton_no.xlsx");
            InputStream inputStream = classPathResource.getInputStream();
            XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream);
            copyFirstSheet(templateWorkbook, jsonArray.size()-1);
            String sheetNamePrefix = "";
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                String sheetName = sheetNamePrefix + (i + 1);
                templateWorkbook.setSheetName(i, sheetName);
            }
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());
            String fileName = customerName+"_"+add+"_"+"箱唛";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //excelWriter.fill(jsonObject.getJSONArray("details"), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "订单-下载进仓单(1进仓单)")
    @GetMapping("/downloadWarehouseNoByOrderId1")
    @ApiOperationSupport(order = 6)
    public void downloadWarehouseNoByOrderId1(@RequestParam(value = "id",required=false) Long id,
                                             HttpServletRequest request, HttpServletResponse response) {
        try {
            OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByOrderId(id);
            String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
            String add = orderWarehouseNoVO.getAdd();//目的仓库代码
            String json = JSON.toJSONString(orderWarehouseNoVO, SerializerFeature.DisableCircularReferenceDetect);
            JSONObject jsonObject = JSONObject.parseObject(json);
            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_warehouse_no.xlsx");
            InputStream inputStream = classPathResource.getInputStream();
            XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream);
            String sheetNamePrefix = "";
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                String sheetName = sheetNamePrefix + (i + 1);
                templateWorkbook.setSheetName(i, sheetName);
            }
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = customerName+"_"+add+"_"+"进仓单";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                excelWriter.fill(jsonObject.getJSONArray("details"), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "订单-下载进仓单(2箱唛)")
    @GetMapping("/downloadWarehouseNoByOrderId2")
    @ApiOperationSupport(order = 7)
    public void downloadWarehouseNoByOrderId2(@RequestParam(value = "id",required=false) Long id,
                                             HttpServletRequest request, HttpServletResponse response) {
        try {
            OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByOrderId(id);
            String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
            String add = orderWarehouseNoVO.getAdd();//目的仓库代码
            List<MarkVO> markList = orderWarehouseNoVO.getMarkList();
            String json = JSON.toJSONString(markList, SerializerFeature.DisableCircularReferenceDetect);
            JSONArray jsonArray = JSONArray.parseArray(json);

            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_carton_no.xlsx");
            InputStream inputStream = classPathResource.getInputStream();
            XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream);
            copyFirstSheet(templateWorkbook, jsonArray.size()-1);
            String sheetNamePrefix = "";
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                String sheetName = sheetNamePrefix + (i + 1);
                templateWorkbook.setSheetName(i, sheetName);
            }
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());
            String fileName = customerName+"_"+add+"_"+"箱唛";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //excelWriter.fill(jsonObject.getJSONArray("details"), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //订单-下载进仓单(1进仓单)pdf
    @ApiOperation(value = "订单-下载进仓单(1进仓单)pdf")
    @GetMapping("/downloadPdfWarehouseNoByOrderId1")
    @ApiOperationSupport(order = 8)
    public void downloadPdfWarehouseNoByOrderId1(@RequestParam(value = "id",required=false) Long id,
                                              HttpServletRequest request, HttpServletResponse response) {

        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByOrderId(id);
        String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
        String add = orderWarehouseNoVO.getAdd();//目的仓库代码
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //cn.hutool.core.bean  obj-转->map
        BeanUtil.copyProperties(orderWarehouseNoVO, resultMap);

        try {
            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_warehouse_no.pdf");//获取pdf模板
            InputStream inputStream = classPathResource.getInputStream();

            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(reader, ba);
            //使用字体
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            /* 获取模版中的字段 */
            AcroFields form = stamper.getAcroFields();
            //填充pdf表单
            if (MapUtil.isNotEmpty(resultMap)) {
                //为字段赋值
                for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                    form.setFieldProperty(entry.getKey(), "textfont", bf, null);
                    if(ObjectUtil.isNotEmpty(entry.getValue())){
                        form.setField(entry.getKey(), entry.getValue().toString());
                    }

                }
            }
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();//关闭 打印器
            reader.close();//关闭 阅读器

            String fileName = customerName+"_"+add+"_"+"进仓单"+ ".pdf";
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-pdf");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename );

            out.write(ba.toByteArray());
            out.flush();
            out.close();
            ba.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //订单-下载进仓单(2箱唛)pdf
    @ApiOperation(value = "订单-下载进仓单(2箱唛)pdf")
    @GetMapping("/downloadPdfWarehouseNoByOrderId2")
    @ApiOperationSupport(order = 9)
    public void downloadPdfWarehouseNoByOrderId2(@RequestParam(value = "id",required=false) Long id,
                                              HttpServletRequest request, HttpServletResponse response) {
        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByOrderId(id);
        String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
        String add = orderWarehouseNoVO.getAdd();//目的仓库代码

        //箱唛
        List<MarkVO> markList = orderWarehouseNoVO.getMarkList();

        try {
            List<PdfReader> list = new ArrayList();//每一条数据代表一页pdf
            for (int i=0; i<markList.size(); i++){
                MarkVO markVO = markList.get(i);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                BeanUtil.copyProperties(markVO, resultMap);

                PdfReader pdfReader = null;//输出的pdf

                ClassPathResource classPathResource = new ClassPathResource("template/nanjing_carton_no.pdf");//获取pdf模板
                InputStream inputStream = classPathResource.getInputStream();

                ByteArrayOutputStream ba = new ByteArrayOutputStream();//输出流 pdf
                PdfReader reader = new PdfReader(inputStream);//读取的pdf模板
                PdfStamper stamper = new PdfStamper(reader, ba);
                //使用字体
                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                /* 获取模版中的字段 */
                AcroFields form = stamper.getAcroFields();
                //填充pdf表单
                if (MapUtil.isNotEmpty(resultMap)) {
                    //为字段赋值
                    for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                        form.setFieldProperty(entry.getKey(), "textfont", bf, null);
                        if(ObjectUtil.isNotEmpty(entry.getValue())){
                            form.setField(entry.getKey(), entry.getValue().toString());
                        }
                    }
                }
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();//关闭 打印器
                reader.close();//关闭 阅读器

                pdfReader = new PdfReader(ba.toByteArray());
                list.add(pdfReader);

            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();//构建字节输出流
            //上面已经获得了pdf的每一页，这里我只需要合并成为一个pdf，然后返回
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, baos);
            document.open();
            for (int k = 0; k < list.size(); k++) {
                PdfReader pdfReader = list.get(k);
                document.newPage();
                copy.addDocument(pdfReader);
            }
            copy.close();

            String fileName = customerName+"_"+add+"_"+"箱唛"+ ".pdf";
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-pdf");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename );

            out.write(baos.toByteArray());
            out.flush();
            out.close();
            baos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //提货地址-下载进仓单号(1进仓单)pdf
    @ApiOperation(value = "提货地址-下载进仓单号(1进仓单)pdf")
    @GetMapping("/downloadPdfWarehouseNoByPickId1")
    @ApiOperationSupport(order = 10)
    public void downloadPdfWarehouseNoByPickId1(@RequestParam(value = "id",required=false) Long id,
                                             HttpServletRequest request, HttpServletResponse response) {

        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByPickId(id);
        String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
        String add = orderWarehouseNoVO.getAdd();//目的仓库代码
        Map<String, Object> resultMap = new HashMap<String, Object>();
        BeanUtil.copyProperties(orderWarehouseNoVO, resultMap);

        try {
            ClassPathResource classPathResource = new ClassPathResource("template/nanjing_warehouse_no.pdf");//获取pdf模板
            InputStream inputStream = classPathResource.getInputStream();

            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(reader, ba);
            //使用字体
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            /* 获取模版中的字段 */
            AcroFields form = stamper.getAcroFields();
            //填充pdf表单
            if (MapUtil.isNotEmpty(resultMap)) {
                //为字段赋值
                for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                    form.setFieldProperty(entry.getKey(), "textfont", bf, null);
                    if(ObjectUtil.isNotEmpty(entry.getValue())){
                        form.setField(entry.getKey(), entry.getValue().toString());
                    }

                }
            }
            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();//关闭 打印器
            reader.close();//关闭 阅读器

            String fileName = customerName+"_"+add+"_"+"进仓单"+ ".pdf";
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-pdf");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename );

            out.write(ba.toByteArray());
            out.flush();
            out.close();
            ba.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //提货地址-下载进仓单号(2箱唛)pdf
    @ApiOperation(value = "提货地址-下载进仓单号(2箱唛)pdf")
    @GetMapping("/downloadPdfWarehouseNoByPickId2")
    @ApiOperationSupport(order = 11)
    public void downloadPdfWarehouseNoByPickId2(@RequestParam(value = "id",required=false) Long id,
                                             HttpServletRequest request, HttpServletResponse response) {

        OrderWarehouseNoVO orderWarehouseNoVO = orderPickService.downloadWarehouseNoByPickId(id);
        String customerName = orderWarehouseNoVO.getCustomerName();//客户公司名称
        String add = orderWarehouseNoVO.getAdd();//目的仓库代码
        List<MarkVO> markList = orderWarehouseNoVO.getMarkList();

        try {
            List<PdfReader> list = new ArrayList();//每一条数据代表一页pdf
            for (int i=0; i<markList.size(); i++){
                MarkVO markVO = markList.get(i);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                BeanUtil.copyProperties(markVO, resultMap);

                PdfReader pdfReader = null;//输出的pdf

                ClassPathResource classPathResource = new ClassPathResource("template/nanjing_carton_no.pdf");//获取pdf模板
                InputStream inputStream = classPathResource.getInputStream();

                ByteArrayOutputStream ba = new ByteArrayOutputStream();//输出流 pdf
                PdfReader reader = new PdfReader(inputStream);//读取的pdf模板
                PdfStamper stamper = new PdfStamper(reader, ba);
                //使用字体
                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                /* 获取模版中的字段 */
                AcroFields form = stamper.getAcroFields();
                //填充pdf表单
                if (MapUtil.isNotEmpty(resultMap)) {
                    //为字段赋值
                    for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                        form.setFieldProperty(entry.getKey(), "textfont", bf, null);
                        if(ObjectUtil.isNotEmpty(entry.getValue())){
                            form.setField(entry.getKey(), entry.getValue().toString());
                        }
                    }
                }
                stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
                stamper.close();//关闭 打印器
                reader.close();//关闭 阅读器

                pdfReader = new PdfReader(ba.toByteArray());
                list.add(pdfReader);

            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();//构建字节输出流
            //上面已经获得了pdf的每一页，这里我只需要合并成为一个pdf，然后返回
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, baos);
            document.open();
            for (int k = 0; k < list.size(); k++) {
                PdfReader pdfReader = list.get(k);
                document.newPage();
                copy.addDocument(pdfReader);
            }
            copy.close();

            String fileName = customerName+"_"+add+"_"+"箱唛"+ ".pdf";
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-pdf");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename );

            out.write(baos.toByteArray());
            out.flush();
            out.close();
            baos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void copyFirstSheet(XSSFWorkbook workbook, int times) {
        if (times <= 0) {
            throw new IllegalArgumentException("times error");
        }
        for (int i = 0; i < times; i++) {
            workbook.cloneSheet(0);
        }
    }



}
