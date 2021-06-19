package com.jayud.storage.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryOrderStorageForm;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.po.Good;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageOrder;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IGoodService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStorageOrderService;
import com.jayud.storage.utils.StorageTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-05-27
 */
@RestController
@Slf4j
@Api("存仓订单")
@RequestMapping("/storageOrder")
public class StorageOrderController {

    @Autowired
    private IStorageOrderService storageOrderService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private OmsClient omsClient;

    @ApiOperation(value = "分页查询存仓订单数据")
    @PostMapping("/findByPage")
    public CommonResult<IPage<StorageOrderVO>> findByPage(@RequestBody QueryOrderStorageForm form){

        IPage<StorageOrderVO> page = this.storageOrderService.findByPage(form);
        for (StorageOrderVO record : page.getRecords()) {
            record.setTime(StorageTime.getStorageTime(record.getStartTime().toString().replace("T"," "),record.getEndTime().toString().replace("T"," ")));
        }
        return CommonResult.success(page);
    }

    @ApiOperation(value = "导出存仓数据")
    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam("orderNo") String orderNo, @RequestParam("outOrderNo")String outOrderNo, @RequestParam("customerName")String customerName,
                            @RequestParam("month")String month, HttpServletResponse response){

        //获取存仓订单信息
        List<StorageOrderVO> storageOrderVOList = storageOrderService.getList(orderNo,outOrderNo,customerName,month);

        List<StorageOrderFormVO> storageOutOrderFormVOS = ConvertUtil.convertList(storageOrderVOList, StorageOrderFormVO.class);
//        System.out.println("storageOrderVOList==================="+storageOrderVOList);
//        System.out.println("storageOutOrderFormVOS==================="+storageOutOrderFormVOS);

        String fileName = "存仓订单数据.xlsx";

        ExcelUtils.exportSinglePageExcel(fileName,storageOutOrderFormVOS,response);

        // 这里 需要指定写用哪个class去写
//        ExcelWriter excelWriter = null;
//        try {
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
//            excelWriter = EasyExcel.write(fileName, StorageOrderFormVO.class).build();
//            WriteSheet writeSheet = EasyExcel.writerSheet("存仓").build();
//            excelWriter.write(storageOutOrderFormVOS, writeSheet);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 千万别忘记finish 会帮忙关闭流
//            if (excelWriter != null) {
//                excelWriter.finish();
//            }
//        }

    }

    @ApiOperation(value = "每个月月末结算仓库数据，生成存仓订单")
    @GetMapping("/createOrder")
    @Scheduled(cron = "0 30 8 28-31 * ?")
    public void createStorageOrder(){
        final Calendar c = Calendar.getInstance();

        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            //获取分批次获取所有未出库数据
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getList1();
            for (InGoodsOperationRecord inGoodsOperationRecord : inGoodsOperationRecords) {
                StorageOrder storageOrder = new StorageOrder();
                storageOrder.setWarehousingBatchNo(inGoodsOperationRecord.getWarehousingBatchNo());
                storageOrder.setSku(inGoodsOperationRecord.getSku());
                storageOrder.setStartTime(inGoodsOperationRecord.getCreateTime());
                storageOrder.setOutOrderNo(null);
                storageOrder.setEndTime(LocalDateTime.now());
                boolean b = storageOrderService.saveStorageOrder(storageOrder);
                if(!b){
                    log.warn(inGoodsOperationRecord.getSku()+"生成存仓订单失败");
                }
            }
        }
    }

}

