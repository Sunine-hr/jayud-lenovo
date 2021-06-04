package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
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
    @ApiOperation(value = "配载，提单（4个窗口），查看-清关")
    @PostMapping("/findBillClearanceInfoByBillId")
    @ApiOperationSupport(order = 11)
    public CommonResult<List<BillClearanceInfoVO>> findBillClearanceInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillClearanceInfoVO> billClearanceInfoVOS = oceanBillService.findBillClearanceInfoByBillId(billId);
        return CommonResult.success(billClearanceInfoVOS);
    }

    @ApiOperation(value = "配载，提单（4个窗口），查看-报关")
    @PostMapping("/findBillCustomsInfoByBillId")
    @ApiOperationSupport(order = 12)
    public CommonResult<List<BillCustomsInfoVO>> findBillCustomsInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillCustomsInfoVO> billCustomsInfoVOS = oceanBillService.findBillCustomsInfoByBillId(billId);
        return CommonResult.success(billCustomsInfoVOS);
    }

    @ApiOperation(value = "配载，提单（4个窗口），查看-柜子")
    @PostMapping("/findOceanCounterByObId")
    @ApiOperationSupport(order = 13)
    public CommonResult<List<OceanCounterVO>> findOceanCounterByObId(@Valid @RequestBody OceanBillParaForm form){
        Long obId = form.getId();
        List<OceanCounterVO> oceanCounterVOS = oceanBillService.findOceanCounterByObId(obId);
        return CommonResult.success(oceanCounterVOS);
    }

    @ApiOperation(value = "配载，提单（4个窗口），查看-详情(包括费用)")
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
    @ApiOperation(value = "导出清单-清关箱子")
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
    @ApiOperation(value = "导出清单-报关箱子")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/exportCustomsInfoCase")
    public void exportCustomsInfoCase(HttpServletResponse response, @RequestParam(value = "id",required=false) Long id) throws IOException {
        List<CustomsInfoCaseExcelVO> rows = billCustomsInfoService.findCustomsInfoCaseBybid(id);
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
            writer.merge(lastColumn, "导出清单-报关箱子");
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
            //out为OutputStream，需要写出到的目标流

            ServletOutputStream out=response.getOutputStream();
            String name = StringUtils.toUtf8String("导出清单-报关箱子");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
            writer.flush(out);
            writer.close();
            IoUtil.close(out);
        }

    }


    //导出清单-柜子清单箱子
    //exportCounterCaseInfo
    @ApiOperation(value = "导出清单-柜子清单箱子")
    @ApiOperationSupport(order = 30)
    @GetMapping(value = "/exportCounterCaseInfo")
    public void exportCounterCaseInfo(HttpServletResponse response, @RequestParam(value = "id",required=false) Long id) throws IOException {
        List<CounterCaseInfoExcelVO> rows = counterListInfoService.findCounterCaseInfoBybid(id);
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
            writer.merge(lastColumn, "导出清单-柜子清单箱子");
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
            //out为OutputStream，需要写出到的目标流

            ServletOutputStream out=response.getOutputStream();
            String name = StringUtils.toUtf8String("导出清单-柜子清单箱子");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");
            writer.flush(out);
            writer.close();
            IoUtil.close(out);
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






}
