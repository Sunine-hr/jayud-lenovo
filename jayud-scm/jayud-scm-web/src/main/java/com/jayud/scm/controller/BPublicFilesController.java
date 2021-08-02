package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddBPublicFilesForm;
import com.jayud.scm.model.vo.BPublicFilesVO;
import com.jayud.scm.service.IBPublicFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 上传附件表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/bPublicFiles")
@Api(tags = "上传附件管理")
public class BPublicFilesController {

    @Autowired
    private IBPublicFilesService bPublicFilesService;

    @ApiOperation(value = "通过类型和订单id查询所有附件")
    @PostMapping(value = "/findPublicFile")
    public CommonResult findPublicFile(@RequestBody Map<String,Object> map) {
        Integer fileModel = MapUtil.getInt(map,"fileModel");
        Integer businessId = MapUtil.getInt(map, "businessId");
        List<BPublicFilesVO> bPublicFilesVOS = bPublicFilesService.getPublicFileList(fileModel,businessId);
        return CommonResult.success(bPublicFilesVOS);
    }

    @ApiOperation(value = "添加附件")
    @PostMapping(value = "/AddPublicFile")
    public CommonResult AddPublicFile(@RequestBody AddBPublicFilesForm filesForm) {
        boolean result = bPublicFilesService.AddPublicFile(filesForm);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"添加附件失败");
    }

}

