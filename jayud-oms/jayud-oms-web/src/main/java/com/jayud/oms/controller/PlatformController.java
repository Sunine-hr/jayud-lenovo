package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.feign.StorageClient;
import com.jayud.oms.model.bo.AddPlatformForm;
import com.jayud.oms.model.bo.QueryPlatformForm;
import com.jayud.oms.model.po.Platform;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.PlatformDetailsVO;
import com.jayud.oms.model.vo.PlatformVO;
import com.jayud.oms.service.IPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 月台管理 前端控制器
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
@RestController
@RequestMapping("/platform")
@Api(tags = "月台管理")

public class PlatformController {

    @Autowired
    private IPlatformService platformService;

    @Autowired
    private StorageClient storageClient;

    @ApiOperation(value = "分页查询月台")
    @PostMapping(value = "/findPlatformByPage")
    public CommonResult<CommonPageResult<PlatformVO>> findPlatformByPage(@Valid @RequestBody QueryPlatformForm form) {
        IPage<PlatformVO> iPage = platformService.findPlatformByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑月台")
    @PostMapping(value = "/saveOrUpdatePlatform")
    public CommonResult saveOrUpdatePlatform(@Valid @RequestBody AddPlatformForm form) {
        Platform platformTemp = new Platform().setId(form.getId()).setPlatformName(form.getPlatformName());
        if (this.platformService.checkUniqueByName(platformTemp)) {
            return CommonResult.error(400, "月台名称已存在");
        }

        platformTemp = new Platform().setId(form.getId()).setPlatformCode(form.getPlatformCode());
        if (this.platformService.checkUniqueByCode(platformTemp) && form.getId() == null) {
            // 月台编号已存在自动获取新的编号
            form.setPlatformCode(this.platformService.autoGenerateNum());
        }

        Platform platform = ConvertUtil.convert(form, Platform.class);
        if (this.platformService.saveOrUpdatePlatform(platform)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除月台，id=月台id")
    @PostMapping(value = "/delPlatform")
    public CommonResult delPlatform(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = MapUtil.getLong(map, "id");
        if (!platformService.checkExists(id)) {
            return CommonResult.error(400, "月台不存在");
        }
        platformService.delPlatform(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "查看月台详情 id=月台id")
    @PostMapping(value = "/getPlatformDetails")
    public CommonResult<PlatformDetailsVO> getPlatformDetails(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        if (!platformService.checkExists(id)) {
            return CommonResult.error(400, "月台不存在");
        }
        PlatformDetailsVO platformDetailsVO = this.platformService.getPlatformDetails(id);
        return CommonResult.success(platformDetailsVO);
    }

    @ApiOperation(value = "获取月台编号")
    @PostMapping(value = "/getPlatformCode")
    public CommonResult getPlatformCode() {
        return CommonResult.success(this.platformService.autoGenerateNum());
    }

    @ApiOperation(value = "下拉-审核通过的仓库 name=仓库名称")
    @PostMapping(value = "/initEnableWarehouse")
    public CommonResult<List<InitComboxStrVO>> initEnableWarehouse(@RequestBody Map<String, Object> map) {
        String name = MapUtil.getStr(map, "name");
        return storageClient.initEnableWarehouse(name);
    }

}

