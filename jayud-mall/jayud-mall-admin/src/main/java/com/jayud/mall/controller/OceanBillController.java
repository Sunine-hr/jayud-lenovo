package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
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
import com.jayud.common.utils.StringUtils;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.BillOrderRelevance;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/oceanbill")
@Api(tags = "A038-admin-提单接口")
@ApiSort(value = 38)
public class OceanBillController {

    @Autowired
    IOceanBillService oceanBillService;
    @Autowired
    IBillClearanceInfoService billClearanceInfoService;
    @Autowired
    IBillCustomsInfoService billCustomsInfoService;
    @Autowired
    ICounterListInfoService counterListInfoService;
    @Autowired
    IBillOrderRelevanceService billOrderRelevanceService;
    @Autowired
    IOceanCounterService oceanCounterService;
    @Autowired
    ICounterOrderInfoService counterOrderInfoService;
    @Autowired
    ICounterCaseInfoService counterCaseInfoService;
    @Autowired
    ICustomsInfoCaseService customsInfoCaseService;
    @Autowired
    IClearanceInfoCaseService clearanceInfoCaseService;


    @ApiOperation(value = "分页查询提单信息")
    @PostMapping("/findOceanBillByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OceanBillVO>> findOceanBillByPage(@RequestBody QueryOceanBillForm form) {
        IPage<OceanBillVO> pageList = oceanBillService.findOceanBillByPage(form);
        CommonPageResult<OceanBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存提单信息")
    @PostMapping("/saveOceanBill")
    @ApiOperationSupport(order = 2)
    @Deprecated //新街口 -> saveOceanBillByConf
    public CommonResult<OceanBillVO> saveOceanBill(@Valid @RequestBody OceanBillForm form){
        return oceanBillService.saveOceanBill(form);
    }

    @ApiOperation(value = "查看提单详情")
    @PostMapping(value = "lookOceanBill")
    @ApiOperationSupport(order = 3)
    public CommonResult<OceanBillVO> lookOceanBill(@Valid @RequestBody OceanBillParaForm form){
        Long id = form.getId();
        return oceanBillService.lookOceanBill(id);
    }

    //提单-录入费用(根据 提单id 查询)
    @ApiOperation(value = "提单-录入费用(根据 提单id 查询)")
    @PostMapping(value = "billLadingCost")
    @ApiOperationSupport(order = 4)
    public CommonResult<OceanBillVO> billLadingCost(@Valid @RequestBody OceanBillParaForm form){
        Long id = form.getId();
        return oceanBillService.billLadingCost(id);
    }

    //保存提单费用信息(录入提单费用保存)
    @ApiOperation(value = "保存提单费用信息(录入提单费用保存)")
    @PostMapping(value = "saveBillCostInfo")
    @ApiOperationSupport(order = 5)
    public CommonResult<BillCostInfoVO> saveBillCostInfo(@Valid @RequestBody BillCostInfoForm form){
        return oceanBillService.saveBillCostInfo(form);
    }

    //根据计费重,一键均摊
    @ApiOperation(value = "一键均摊提单费用到订单(根据订单计费重,均摊)")
    @PostMapping(value = "shareEqually")
    @ApiOperationSupport(order = 6)
    public CommonResult<OceanBillVO> shareEqually(@Valid @RequestBody BillCostInfoForm form){
        return oceanBillService.shareEqually(form);
    }


    //提单任务-反馈状态(根据 提单id 查询)
    @ApiOperation(value = "提单任务-反馈状态(根据 提单id 查询)")
    @PostMapping("/lookOceanBillTask")
    @ApiOperationSupport(order = 7)
    public CommonResult<OceanBillVO> lookOceanBillTask(@Valid @RequestBody OceanBillParaForm form){
        Long obId = form.getId();
        return oceanBillService.lookOceanBillTask(obId);
    }

    //提单任务-反馈状态(点击已完成)
    @ApiOperation(value = "提单任务-反馈状态(点击已完成)")
    @PostMapping("/confirmCompleted")
    @ApiOperationSupport(order = 8)
    public CommonResult<BillTaskRelevanceVO> confirmCompleted(@Valid @RequestBody BillTaskRelevanceParaForm form){
        Long id = form.getId();
        return oceanBillService.confirmCompleted(id);
    }

    //提单任务-订单操作日志（根据订单id查看）
    @ApiOperation(value = "运单任务-订单操作日志（根据订单id查看）")
    @PostMapping("/lookOperateLog")
    @ApiOperationSupport(order = 9)
    public CommonResult<List<BillTaskRelevanceVO>> lookOperateLog(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        return oceanBillService.lookOperateLog(id);
    }


    //TODO 提单的接口

    @ApiOperation(value = "保存提单信息2(在配载添加提单，默认关联配载)")
    @PostMapping("/saveOceanBillByConf")
    @ApiOperationSupport(order = 10)
    public CommonResult<OceanBillVO> saveOceanBillByConf(@Valid @RequestBody OceanBillForm form){
        return oceanBillService.saveOceanBillByConf(form);
    }

    // 查询
    @ApiOperation(value = "查看-清关清单list")
    @PostMapping("/findBillClearanceInfoByBillId")
    @ApiOperationSupport(order = 11)
    public CommonResult<List<BillClearanceInfoVO>> findBillClearanceInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillClearanceInfoVO> billClearanceInfoVOS = oceanBillService.findBillClearanceInfoByBillId(billId);
        return CommonResult.success(billClearanceInfoVOS);
    }

    @ApiOperation(value = "查看-报关清单list")
    @PostMapping("/findBillCustomsInfoByBillId")
    @ApiOperationSupport(order = 12)
    public CommonResult<List<BillCustomsInfoVO>> findBillCustomsInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillCustomsInfoVO> billCustomsInfoVOS = oceanBillService.findBillCustomsInfoByBillId(billId);
        return CommonResult.success(billCustomsInfoVOS);
    }

    @ApiOperation(value = "查看-柜子list")
    @PostMapping("/findOceanCounterByObId")
    @ApiOperationSupport(order = 13)
    public CommonResult<List<OceanCounterVO>> findOceanCounterByObId(@Valid @RequestBody OceanBillParaForm form){
        Long obId = form.getId();
        List<OceanCounterVO> oceanCounterVOS = oceanBillService.findOceanCounterByObId(obId);
        return CommonResult.success(oceanCounterVOS);
    }

    @ApiOperation(value = "查看-详情(包括费用)")
    @PostMapping("/findOceanBillById")
    @ApiOperationSupport(order = 14)
    public CommonResult<OceanBillVO> findOceanBillById(@Valid @RequestBody OceanBillParaForm form){
        Long id = form.getId();
        OceanBillVO oceanBillVO = oceanBillService.findOceanBillById(id);
        return CommonResult.success(oceanBillVO);
    }

    //添加修改

    @ApiOperation(value = "添加修改-提单清关信息")
    @PostMapping("/saveBillClearanceInfo")
    @ApiOperationSupport(order = 15)
    public CommonResult<BillClearanceInfoVO> saveBillClearanceInfo(@Valid @RequestBody BillClearanceInfoForm form){
        BillClearanceInfoVO billClearanceInfoVO = oceanBillService.saveBillClearanceInfo(form);
        return CommonResult.success(billClearanceInfoVO);
    }

    @ApiOperation(value = "添加修改-提单报关信息")
    @PostMapping("/saveBillCustomsInfo")
    @ApiOperationSupport(order = 16)
    public CommonResult<BillCustomsInfoVO> saveBillCustomsInfo(@Valid @RequestBody BillCustomsInfoForm form){
        BillCustomsInfoVO billCustomsInfoVO = oceanBillService.saveBillCustomsInfo(form);
        return CommonResult.success(billCustomsInfoVO);
    }

    @ApiOperation(value = "添加修改-提单柜子信息")
    @PostMapping("/saveOceanCounter")
    @ApiOperationSupport(order = 17)
    public CommonResult<OceanCounterVO> saveOceanCounter(@Valid @RequestBody OceanCounterForm form){
        OceanCounterVO oceanCounterVO = oceanBillService.saveOceanCounter(form);
        return CommonResult.success(oceanCounterVO);
    }

    //删除

    @ApiOperation(value = "删除-提单清关信息")
    @PostMapping("/delBillClearanceInfo")
    @ApiOperationSupport(order = 18)
    public CommonResult delBillClearanceInfo(@Valid @RequestBody BillClearanceInfoIdForm form){
        oceanBillService.delBillClearanceInfo(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "删除-提单报关信息")
    @PostMapping("/delBillCustomsInfo")
    @ApiOperationSupport(order = 19)
    public CommonResult delBillCustomsInfo(@Valid @RequestBody BillCustomsInfoIdForm form){
        oceanBillService.delBillCustomsInfo(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "删除-提单柜子信息")
    @PostMapping("/delOceanCounter")
    @ApiOperationSupport(order = 20)
    public CommonResult delOceanCounter(@Valid @RequestBody OceanCounterIdForm form){
        oceanBillService.delOceanCounter(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "提单，选择配载的箱子(配载，报价，订单，箱号 -> 展示 箱号) 分页")
    @PostMapping("/findConfCaseByPage")
    @ApiOperationSupport(order = 21)
    public CommonResult<CommonPageResult<ConfCaseVO>> findConfCaseByPage(@Valid @RequestBody ConfCaseForm form){
        IPage<ConfCaseVO> pageList = oceanBillService.findConfCaseByPage(form);
        CommonPageResult<ConfCaseVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询-(提单)清关信息表")
    @PostMapping("/findBillClearanceInfoById")
    @ApiOperationSupport(order = 22)
    public CommonResult<BillClearanceInfoVO> findBillClearanceInfoById(@Valid @RequestBody BillClearanceInfoIdForm form){
        Long id = form.getId();
        BillClearanceInfoVO billClearanceInfoVO = oceanBillService.findBillClearanceInfoById(id);
        return CommonResult.success(billClearanceInfoVO);
    }

    @ApiOperation(value = "查询-(提单)报关信息表")
    @PostMapping("/findBillCustomsInfoById")
    @ApiOperationSupport(order = 23)
    public CommonResult<BillCustomsInfoVO> findBillCustomsInfoById(@Valid @RequestBody BillCustomsInfoIdForm form){
        Long id = form.getId();
        BillCustomsInfoVO billCustomsInfoVO = oceanBillService.findBillCustomsInfoById(id);
        return CommonResult.success(billCustomsInfoVO);
    }


    //提单，柜子，柜子下的清单

    @ApiOperation(value = "添加修改-柜子清单")
    @PostMapping("/saveCounterListInfo")
    @ApiOperationSupport(order = 24)
    public CommonResult<CounterListInfoVO> saveCounterListInfo(@Valid @RequestBody CounterListInfoForm form){
        CounterListInfoVO counterListInfoVO = oceanBillService.saveCounterListInfo(form);
        return CommonResult.success(counterListInfoVO);
    }

    @ApiOperation(value = "删除-柜子清单")
    @PostMapping("/delCounterListInfo")
    @ApiOperationSupport(order = 25)
    public CommonResult delCounterListInfo(@Valid @RequestBody CounterListInfoIdForm form){
        oceanBillService.delCounterListInfo(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "查询-柜子清单及其关联的箱号")
    @PostMapping("/findCounterListInfoById")
    @ApiOperationSupport(order = 26)
    public CommonResult<CounterListInfoVO> findCounterListInfoById(@Valid @RequestBody CounterListInfoIdForm form){
        Long id = form.getId();
        CounterListInfoVO counterListInfoVO = oceanBillService.findCounterListInfoById(id);
        return CommonResult.success(counterListInfoVO);
    }

    @ApiOperation(value = "查询-柜子下的柜子清单信息list")
    @PostMapping("/findCounterListInfoByCounterId")
    @ApiOperationSupport(order = 27)
    public CommonResult<List<CounterListInfoVO>> findCounterListInfoByCounterId(@Valid @RequestBody OceanCounterIdForm form){
        Long id = form.getId();
        List<CounterListInfoVO> counterListInfoVOS = oceanBillService.findCounterListInfoByCounterId(id);
        return CommonResult.success(counterListInfoVOS);
    }

    @ApiOperation(value = "创建虚拟提单号")
    @PostMapping("/createVirtualOrderId")
    @ApiOperationSupport(order = 28)
    public CommonResult<String> createVirtualOrderId(){
        String orderId = String.valueOf(System.currentTimeMillis());
        return CommonResult.success(orderId);
    }

    //导出清单

    //导出清单-清关箱子
    //exportClearanceInfoCase
    @ApiOperation(value = "导出清关清单")
    @ApiOperationSupport(order = 29)
    @GetMapping(value = "/exportClearanceInfoCase")
    public void exportClearanceInfoCase(
            HttpServletResponse response,
            @RequestParam(value = "id",required=false) Long id) throws IOException {
        List<ClearanceInfoCaseExcelVO> rows = billClearanceInfoService.findClearanceInfoCaseBybid(id);
        if(CollUtil.isNotEmpty(rows)){
            ExcelWriter writer = ExcelUtil.getWriter(true);
            writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER); //水平左对齐，垂直中间对齐
            writer.setColumnWidth(0, 40); //第1列40px宽
            writer.setColumnWidth(1, 15); //第2列15px 宽
            writer.setColumnWidth(2, 30); //第3列15px 宽
            writer.setColumnWidth(3, 30); //第4列15px 宽

            //自定义标题别名
            writer.addHeaderAlias("bName", "文件名称");
            writer.addHeaderAlias("billNo", "提单号");
            writer.addHeaderAlias("cartonNo", "箱号");
            writer.addHeaderAlias("orderNo", "订单号");
            Field[] s = ClearanceInfoCaseExcelVO.class.getDeclaredFields();
            int lastColumn = s.length-1;
            // 合并单元格后的标题行，使用默认标题样式
            writer.merge(lastColumn, "导出清单-清关箱子");
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
            //out为OutputStream，需要写出到的目标流

            ServletOutputStream out=response.getOutputStream();
            String name = StringUtils.toUtf8String("导出清单-清关箱子");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
            writer.flush(out);
            writer.close();
            IoUtil.close(out);
        }

    }


    //导出清单-报关箱子
    //exportCustomsInfoCase
    @ApiOperation(value = "导出报关清单")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/exportCustomsInfoCase")
    public void exportCustomsInfoCase(HttpServletResponse response, @RequestParam(value = "id",required=false) Long id) throws IOException {
        CustomsListExcelVO customsListExcelVO = billCustomsInfoService.findCustomsListExcelById(id);
        try {
            String json = JSON.toJSONString(customsListExcelVO, SerializerFeature.DisableCircularReferenceDetect);
            JSONObject jsonObject = JSONObject.parseObject(json);
            ClassPathResource classPathResource = new ClassPathResource("template/customs_info_case.xlsx");
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
            String fileName = "导出报关清单";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            com.alibaba.excel.ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
                JSONArray list1 = jsonObject.getJSONArray("customsGoodsExcelList");
                // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
                excelWriter.fill(new FillWrapper("a", list1), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //导出清单-柜子清单箱子
    //exportCounterCaseInfo
    @ApiOperation(value = "导出清单-柜子清单(装柜清单)")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/exportCounterCaseInfo")
    public void exportCounterCaseInfo(HttpServletResponse response, @RequestParam(value = "id",required=false) Long id) throws IOException {
        CounterListExcelVO counterListExcelVO = counterListInfoService.findCounterListExcelById(id);
        try {
            String json = JSON.toJSONString(counterListExcelVO, SerializerFeature.DisableCircularReferenceDetect);
            JSONObject jsonObject = JSONObject.parseObject(json);
            ClassPathResource classPathResource = new ClassPathResource("template/counter_list.xlsx");
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


            //# 装柜清单-提单号（billNo）-柜号（cntr_no）-装柜清单名称（fileName）
            String billNo = counterListExcelVO.getBillNo();
            String cntrNo = counterListExcelVO.getCntrNo();
            String fileName1 = counterListExcelVO.getFileName();

            String fileName = "装柜清单_"+billNo+"_"+cntrNo+"_"+fileName1;
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
            com.alibaba.excel.ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                WriteSheet sheet = EasyExcel.writerSheet(i).build();
                JSONArray list1 = jsonObject.getJSONArray("counterOrderInfoExcelList");
                // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 a，然后多个list必须用 FillWrapper包裹
                excelWriter.fill(new FillWrapper("a", list1), fillConfig, sheet);
                excelWriter.fill(jsonObject, sheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询-提单关联订单(任务通知表)
    @ApiOperation(value = "查询-提单关联订单(任务通知表)")
    @ApiOperationSupport(order = 31)
    @PostMapping(value = "/findBillOrderRelevanceByBillId")
    public CommonResult<List<BillOrderRelevance>> findBillOrderRelevanceByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillOrderRelevance> billOrderRelevances = billOrderRelevanceService.findBillOrderRelevanceByBillId(billId);
        return CommonResult.success(billOrderRelevances);
    }

    //修改-提单关联订单(任务通知表)
    @ApiOperation(value = "修改-提单关联订单(任务通知表)")
    @ApiOperationSupport(order = 32)
    @PostMapping(value = "/updateBillOrderRelevance")
    public CommonResult updateBillOrderRelevance(@Valid @RequestBody List<BillOrderRelevance> form){
        billOrderRelevanceService.updateBillOrderRelevance(form);
        return CommonResult.success("修改成功");
    }

    //提单-保存轨迹通知
    @ApiOperation(value = "提单-保存轨迹通知")
    @ApiOperationSupport(order = 33)
    @PostMapping(value = "/saveTrackNotice")
    public CommonResult saveTrackNotice(@Valid @RequestBody TrackNoticeForm form){
        oceanBillService.saveTrackNotice(form);
        return CommonResult.success("操作成功");
    }

    //查询提单下的物流轨迹通知
    @ApiOperation(value = "查询提单下的物流轨迹通知")
    @ApiOperationSupport(order = 34)
    @PostMapping(value = "/findBillLogisticsTrackByBillId")
    public CommonResult<List<BillLogisticsTrackVO>> findBillLogisticsTrackByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillLogisticsTrackVO> billLogisticsTrackVOS = oceanBillService.findBillLogisticsTrackByBillId(billId);
        return CommonResult.success(billLogisticsTrackVOS);
    }


    //查询-未选择的订单(柜子清单-绑定订单)
    @ApiOperation(value = "查询-未选择的订单(柜子清单-绑定订单)")
    @ApiOperationSupport(order = 35)
    @PostMapping(value = "/findUnselectedOrderInfo")
    public CommonResult<List<OrderInfoVO>> findUnselectedOrderInfo(@Valid @RequestBody OrderInfoQueryForm form){
        List<OrderInfoVO> orderInfoList = counterListInfoService.findUnselectedOrderInfo(form);
        return CommonResult.success(orderInfoList);
    }

    //查询-已选择的订单(柜子清单-绑定订单)
    @ApiOperation(value = "查询-已选择的订单(柜子清单-绑定订单)")
    @ApiOperationSupport(order = 36)
    @PostMapping(value = "/findSelectedOrderInfo")
    public CommonResult<List<OrderInfoVO>> findSelectedOrderInfo(@Valid @RequestBody OrderInfoQueryForm form){
        List<OrderInfoVO> orderInfoList = counterListInfoService.findSelectedOrderInfo(form);
        return CommonResult.success(orderInfoList);
    }

    //批量移入(柜子清单-绑定订单)
    @ApiOperation(value = "批量移入(柜子清单-绑定订单)")
    @ApiOperationSupport(order = 37)
    @PostMapping(value = "/batchIntoCounterOrderInfo")
    public CommonResult batchIntoCounterOrderInfo(@Valid @RequestBody BatchCounterOrderInfoForm form){
        counterOrderInfoService.batchIntoCounterOrderInfo(form);
        return CommonResult.success("操作成功");
    }

    //批量移除(柜子清单-绑定订单)
    @ApiOperation(value = "批量移除(柜子清单-绑定订单)")
    @ApiOperationSupport(order = 38)
    @PostMapping(value = "/batchRemoveCounterOrderInfo")
    public CommonResult batchRemoveCounterOrderInfo(@Valid @RequestBody BatchCounterOrderInfoForm form){
        counterOrderInfoService.batchRemoveCounterOrderInfo(form);
        return CommonResult.success("操作成功");
    }

    //查询-未选择的箱子(运单-绑定装柜箱子)
    @ApiOperation(value = "查询-未选择的箱子(运单-绑定装柜箱子)")
    @ApiOperationSupport(order = 39)
    @PostMapping(value = "/findUnselectedOrderCase")
    public CommonResult<List<OrderCaseVO>> findUnselectedOrderCase(@Valid @RequestBody OrderCaseQueryForm form){
        List<OrderCaseVO> orderInfoList = counterCaseInfoService.findUnselectedOrderCase(form);
        return CommonResult.success(orderInfoList);
    }

    //查询-已选择的箱子(运单-绑定装柜箱子)
    @ApiOperation(value = "查询-已选择的箱子(运单-绑定装柜箱子)")
    @ApiOperationSupport(order = 40)
    @PostMapping(value = "/findSelectedOrderCase")
    public CommonResult<List<OrderCaseVO>> findSelectedOrderCase(@Valid @RequestBody OrderCaseQueryForm form){
        List<OrderCaseVO> orderInfoList = counterCaseInfoService.findSelectedOrderCase(form);
        return CommonResult.success(orderInfoList);
    }

    //批量移入(运单-绑定装柜箱子)
    @ApiOperation(value = "批量移入(运单-绑定装柜箱子)")
    @ApiOperationSupport(order = 41)
    @PostMapping(value = "/batchIntoCounterCaseInfo")
    public CommonResult batchIntoCounterCaseInfo(@Valid @RequestBody BatchCounterCaseInfoForm form){
        counterCaseInfoService.batchIntoCounterCaseInfo(form);
        return CommonResult.success("操作成功");
    }

    //批量移除(运单-绑定装柜箱子)
    @ApiOperation(value = "批量移除(运单-绑定装柜箱子)")
    @ApiOperationSupport(order = 42)
    @PostMapping(value = "/batchRemoveCounterCaseInfo")
    public CommonResult batchRemoveCounterCaseInfo(@Valid @RequestBody BatchCounterCaseInfoForm form){
        counterCaseInfoService.batchRemoveCounterCaseInfo(form);
        return CommonResult.success("操作成功");
    }

    //查询-柜子清单下运单信息list
    @ApiOperation(value = "查询-柜子清单下运单信息list")
    @PostMapping("/findCounterOrderInfoByBid")
    @ApiOperationSupport(order = 43)
    public CommonResult<List<CounterOrderInfoVO>> findCounterOrderInfoByBid(@Valid @RequestBody CounterListInfoIdForm form){
        Long bId = form.getId();
        List<CounterOrderInfoVO> counterOrderInfoList = counterOrderInfoService.findCounterOrderInfoByBid(bId);
        return CommonResult.success(counterOrderInfoList);
    }

    //报关箱子-查询提单下未生成的订单箱子(分类型)
    @ApiOperation(value = "报关箱子-查询提单下未生成的订单箱子(分类型)")
    @PostMapping("/findUnselectedBillCaseByCustoms")
    @ApiOperationSupport(order = 44)
    public CommonResult<List<BillCaseVO>> findUnselectedBillCaseByCustoms(@Valid @RequestBody BillCustomsInfoQueryForm form){
        List<BillCaseVO> billCaseList = customsInfoCaseService.findUnselectedBillCaseByCustoms(form);
        return CommonResult.success(billCaseList);
    }

    //报关箱子-查询提单下已生成的订单箱子
    @ApiOperation(value = "报关箱子-查询提单下已生成的订单箱子")
    @ApiOperationSupport(order = 45)
    @PostMapping(value = "/findSelectedBillCaseByCustoms")
    public CommonResult<List<BillCaseVO>> findSelectedBillCaseByCustoms(@Valid @RequestBody BillCustomsInfoQueryForm form){
        List<BillCaseVO> billCaseList = customsInfoCaseService.findSelectedBillCaseByCustoms(form);
        return CommonResult.success(billCaseList);
    }

    //提单下的报关-生成报关清单
    @ApiOperation(value = "提单下的报关-生成报关清单")
    @ApiOperationSupport(order = 46)
    @PostMapping(value = "/createCustomsInfoCase")
    public CommonResult createCustomsInfoCase(@Valid @RequestBody CreateCustomsInfoCaseForm form){
        customsInfoCaseService.createCustomsInfoCase(form);
        return CommonResult.success("操作成功");
    }

    //清关箱子-查询提单下未生成的订单箱子(分类型)
    @ApiOperation(value = "清关箱子-查询提单下未生成的订单箱子(分类型)")
    @PostMapping("/findUnselectedBillCaseByClearance")
    @ApiOperationSupport(order = 47)
    public CommonResult<List<BillCaseVO>> findUnselectedBillCaseByClearance(@Valid @RequestBody BillClearanceInfoQueryForm form){
        List<BillCaseVO> billCaseList = clearanceInfoCaseService.findUnselectedBillCaseByClearance(form);
        return CommonResult.success(billCaseList);
    }

    //清关箱子-查询提单下已生成的订单箱子
    @ApiOperation(value = "清关箱子-查询提单下已生成的订单箱子")
    @ApiOperationSupport(order = 48)
    @PostMapping(value = "/findSelectedBillCaseByClearance")
    public CommonResult<List<BillCaseVO>> findSelectedBillCaseByClearance(@Valid @RequestBody BillClearanceInfoQueryForm form){
        List<BillCaseVO> billCaseList = clearanceInfoCaseService.findSelectedBillCaseByClearance(form);
        return CommonResult.success(billCaseList);
    }

    //提单下的清关-生成清关清单
    @ApiOperation(value = "提单下的清关-生成清关清单")
    @ApiOperationSupport(order = 49)
    @PostMapping(value = "/createClearanceInfoCase")
    public CommonResult createClearanceInfoCase(@Valid @RequestBody CreateClearanceInfoCaseForm form){
        clearanceInfoCaseService.createClearanceInfoCase(form);
        return CommonResult.success("操作成功");
    }

    //报关清单-查询关联的订单箱子以及订单报关文件(买单 / 独立)
    @ApiOperation(value = "报关清单-查询关联的订单箱子以及订单报关文件(报关分为：买单 / 独立)")
    @ApiOperationSupport(order = 50)
    @PostMapping(value = "/findSelectOrderInfoByCustoms")
    public CommonResult<List<OrderInfoVO>> findSelectOrderInfoByCustoms(@Valid @RequestBody BillCustomsInfoQueryForm form){
        List<OrderInfoVO> orderInfoList = billCustomsInfoService.findSelectOrderInfoByCustoms(form);
        return CommonResult.success(orderInfoList);
    }

    //清关清单-查询关联的订单箱子以及订单清关文件(清关分为：买单 / 独立)
    @ApiOperation(value = "清关清单-查询关联的订单箱子以及订单清关文件(清关分为：买单 / 独立)")
    @ApiOperationSupport(order = 51)
    @PostMapping(value = "/findSelectOrderInfoByClearance")
    public CommonResult<List<OrderInfoVO>> findSelectOrderInfoByClearance(@Valid @RequestBody BillClearanceInfoQueryForm form){
        List<OrderInfoVO> orderInfoVOList = billClearanceInfoService.findSelectOrderInfoByClearance(form);
        return CommonResult.success(orderInfoVOList);
    }

    //根据当前提单id，选择装柜清单列表
    @ApiOperation(value = "根据当前提单id，选择装柜清单列表")
    @ApiOperationSupport(order = 52)
    @PostMapping(value = "/findCounterListInfoByBillId")
    public CommonResult<List<CounterListInfoVO>> findCounterListInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();//提单id(ocean_bill id)
        List<CounterListInfoVO> counterListInfoList = oceanBillService.findCounterListInfoByBillId(billId);
        return CommonResult.success(counterListInfoList);
    }




}
