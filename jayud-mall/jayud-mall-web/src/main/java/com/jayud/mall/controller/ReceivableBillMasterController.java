package com.jayud.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.model.bo.BillMasterForm;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.vo.ReceivableBillExcelMasterVO;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IReceivableBillMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "C017-client-应收账单接口")
@ApiSort(value = 17)
@RestController
@RequestMapping("/receivablebillmaster")
public class ReceivableBillMasterController {

    @Autowired
    IReceivableBillMasterService receivableBillMasterService;
    @Autowired
    BaseService baseService;

    //应收账单分页查询
    @ApiOperation(value = "应收账单分页查询")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findReceivableBillMasterByPage")
    public CommonResult<CommonPageResult<ReceivableBillMasterVO>> findReceivableBillMasterByPage(
            @RequestBody QueryReceivableBillMasterForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId());
        IPage<ReceivableBillMasterVO> pageList = receivableBillMasterService.findReceivableBillMasterByPage(form);
        CommonPageResult<ReceivableBillMasterVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //应收账单-查看明细
    //lookDetail
    @ApiOperation(value = "应收账单-查看明细")
    @ApiOperationSupport(order = 2)
    @PostMapping("/lookDetail")
    public CommonResult<ReceivableBillMasterVO> lookDetail(@Valid @RequestBody BillMasterForm form){
        Long id = form.getId();
        return receivableBillMasterService.lookDetail(id);
    }


    @ApiOperation(value = "应收账单-下载账单")
    @GetMapping("/downloadBills")
    //@PostMapping("/downloadBills")
    @ApiOperationSupport(order = 6)
    public void downloadBills(@RequestParam(value = "ids",required=false) String ids,
                                              HttpServletRequest request, HttpServletResponse response) {
        try {
            CustomerUser customerUser = baseService.getCustomerUser();
            if(ObjectUtil.isEmpty(customerUser)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
            }
            Integer customerId = customerUser.getId();

            String[] split = ids.split(",");

            List<Long> ids2 = new ArrayList<>();
            for(int i=0; i<split.length; i++){
                ids2.add(Long.valueOf(split[i]));
            }

            ReceivableBillExcelMasterVO receivableBillExcelVO = receivableBillMasterService.downloadBills(customerId, ids2);

            String customerName = receivableBillExcelVO.getCustomerName();
            String json = JSON.toJSONString(receivableBillExcelVO, SerializerFeature.DisableCircularReferenceDetect);
            JSONObject jsonObject = JSONObject.parseObject(json);
            ClassPathResource classPathResource = new ClassPathResource("template/Nanjing_bills.xlsx");
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

            String fileName = "账单"+"_"+customerName;
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
                JSONArray list1 = jsonObject.getJSONArray("receivableBillDetailList");
                JSONArray list2 = jsonObject.getJSONArray("billAmountTotalList");
                // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
                excelWriter.fill(new FillWrapper("a", list1), fillConfig, sheet);
                excelWriter.fill(new FillWrapper("b", list2), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
