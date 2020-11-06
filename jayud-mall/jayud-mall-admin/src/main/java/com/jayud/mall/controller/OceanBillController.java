package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.service.IOceanBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oceanbill")
@Api(tags = "提单接口")
public class OceanBillController {

    @Autowired
    IOceanBillService oceanBillService;

    @ApiOperation(value = "分页查询提单信息")
    @PostMapping("/findOceanBillByPage")
    public CommonResult<CommonPageResult<OceanBillVO>> findOceanBillByPage(@RequestBody QueryOceanBillForm form) {
        IPage<OceanBillVO> pageList = oceanBillService.findOceanBillByPage(form);
        CommonPageResult<OceanBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存提单信息")
    @PostMapping("/saveOceanBill")
    public CommonResult saveOceanBill(@RequestBody OceanBillForm form){
        oceanBillService.saveOceanBill(form);
        return CommonResult.success("保存提单信息，成功！");

    }


}
