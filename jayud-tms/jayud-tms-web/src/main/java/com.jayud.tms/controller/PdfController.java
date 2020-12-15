package com.jayud.tms.controller;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.tms.model.vo.DriverInfoPdfVO;
import com.jayud.tms.model.vo.SendCarPdfVO;
import com.jayud.tms.pdfUtil.PdfTemplateUtil;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTransportService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
@Api(tags = "生成PDF")
public class PdfController {

    @Autowired
    private IOrderTransportService orderTransportService;

    @Autowired
    private IOrderSendCarsService sendCarsService;

    @ApiOperation(value = "渲染数据,orderNo=子订单号")
    @PostMapping(value = "/initPdfData")
    public CommonResult<SendCarPdfVO> initPdfData(@RequestBody Map<String,Object> param) {
        String orderNo = MapUtil.getStr(param, CommonConstant.ORDER_NO);
        if(StringUtil.isNullOrEmpty(orderNo)){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        SendCarPdfVO sendCarPdfVO = orderTransportService.initPdfData(orderNo, CommonConstant.ZGYS);
        return CommonResult.success(sendCarPdfVO);
    }

    @ApiOperation(value = "二期优化1：司机资料,orderNo=子订单号")
    @PostMapping(value = "/initDriverInfo")
    public CommonResult<DriverInfoPdfVO> initDriverInfo(@RequestBody Map<String,Object> param) {
        String orderNo = MapUtil.getStr(param, CommonConstant.ORDER_NO);
        if(StringUtil.isNullOrEmpty(orderNo)){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        DriverInfoPdfVO driverInfoPdfVO = sendCarsService.initDriverInfo(orderNo);
        return CommonResult.success(driverInfoPdfVO);
    }


    @ApiOperation(value = "下载PDF,废弃")
    @RequestMapping("/export")
    public void exportPdf(HttpServletResponse response) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = null;
        OutputStream out = null;
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("obj", orderTransportService.initPdfData("T236488225987", CommonConstant.ZGYS));
            byteArrayOutputStream = PdfTemplateUtil.createPDF(data, "send_car_sheet.ftl");
            // 设置响应消息头，告诉浏览器当前响应是一个下载文件
            response.setContentType("application/x-msdownload");
            // 告诉浏览器，当前响应数据要求用户干预保存到文件中，以及文件名是什么 如果文件名有中文，必须URL编码
            String fileName = URLEncoder.encode("托运单T236488225987.pdf", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = response.getOutputStream();
            byteArrayOutputStream.writeTo(out);
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导出失败：" + e.getMessage());
        } finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }




}
