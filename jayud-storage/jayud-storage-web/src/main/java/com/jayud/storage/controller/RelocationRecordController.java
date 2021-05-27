package com.jayud.storage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaForm;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import com.jayud.storage.service.IRelocationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void exportExcel(@RequestParam("orderNo") String orderNo,@RequestParam("warehouseName")String warehouseName,@RequestParam("areaName")String areaName,
                            @RequestParam("shelvesName")String shelvesName,@RequestParam("sku")String sku,@RequestParam("startTime")String startTime,@RequestParam("endTime")String endTime){


    }

}

