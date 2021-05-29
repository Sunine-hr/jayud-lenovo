package com.jayud.storage.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.excel.ExcelUtils;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.vo.RelocationRecordFormVO;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.service.IRelocationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 移库信息表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@RestController
@Api(tags = "移库订单管理")
@RequestMapping("/relocationRecord")
public class RelocationRecordController {

    @Autowired
    private IRelocationRecordService relocationRecordService;

    @ApiOperation(value = "分页查询移库订单数据")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<RelocationRecordVO>> findByPage(@RequestBody QueryRelocationRecordForm form){
        form.setStartTime();
        IPage<RelocationRecordVO> page = this.relocationRecordService.findByPage(form);
        CommonPageResult<RelocationRecordVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "导出移库数据")
    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam("orderNo") String orderNo, @RequestParam("warehouseName")String warehouseName, @RequestParam("areaName")String areaName,
                            @RequestParam("shelvesName")String shelvesName, @RequestParam("sku")String sku, @RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime, HttpServletResponse response){

        //获取导出的数据
        List<RelocationRecordVO> recordVOList = relocationRecordService.getList(orderNo,warehouseName,areaName,shelvesName,sku,startTime,endTime);

        List<RelocationRecordFormVO> relocationRecordFormVOS = ConvertUtil.convertList(recordVOList, RelocationRecordFormVO.class);
//        System.out.println("recordVOList==================="+recordVOList);

        String fileName = "移库数据.xlsx";

        ExcelUtils.exportSinglePageExcel(fileName,relocationRecordFormVOS,response);

        // 这里 需要指定写用哪个class去写
//        ExcelWriter excelWriter = null;
//        try {
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");// 定义输出类型
//            excelWriter = EasyExcel.write(fileName, RelocationRecordFormVO.class).build();
//            WriteSheet writeSheet = EasyExcel.writerSheet("移库").build();
//            excelWriter.write(relocationRecordFormVOS, writeSheet);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 千万别忘记finish 会帮忙关闭流
//            if (excelWriter != null) {
//                excelWriter.finish();
//            }
//        }
    }

}

