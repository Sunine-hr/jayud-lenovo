package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddBPublicFilesForm;
import com.jayud.scm.model.vo.BPublicFilesVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.IBPublicFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "通过类型和订单id查询所有附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileModel", dataType = "Integer", value = "附件类型,1:商品库,2:客户主体,3委托订单,4:付款单,5:收款单,6:入库单,7:出库单,8:应收款,9:提验货,10:中港运输", required = true),
            @ApiImplicitParam(name="businessId", dataType = "Integer", value = "业务单据ID", required = true)
    })
    @PostMapping(value = "/findPublicFile")
    public CommonResult<List<BPublicFilesVO>> findPublicFile(@RequestBody Map<String,Object> map) {
        Integer fileModel = MapUtil.getInt(map,"fileModel");
        Integer businessId = MapUtil.getInt(map, "businessId");
        List<BPublicFilesVO> bPublicFilesVOS = bPublicFilesService.getPublicFileList(fileModel,businessId);
        if(CollectionUtils.isNotEmpty(bPublicFilesVOS)){
            for (BPublicFilesVO bPublicFilesVO : bPublicFilesVOS) {
                if(bPublicFilesVO.getFileType() != null){
                    bPublicFilesVO.setFileModelCopy(ibDataDicEntryService.getTextByDicCodeAndDataValue("1016",bPublicFilesVO.getFileType()));
                }
            }
        }

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

