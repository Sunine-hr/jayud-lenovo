package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddWarehouseInfoForm;
import com.jayud.oms.model.bo.AuditWarehouseForm;
import com.jayud.oms.model.bo.QueryWarehouseInfoForm;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.WarehouseInfo;
import com.jayud.oms.model.vo.WarehouseInfoVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.IRegionCityService;
import com.jayud.oms.service.IWarehouseInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 中转仓库信息表 前端控制器
 * </p>
 *
 * @author
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/warehouseInfo")
@Api(tags = "中转仓库信息")
public class WarehouseInfoController {


    @Autowired
    private IWarehouseInfoService warehouseInfoService;

    @Autowired
    private IRegionCityService regionCityService;

    @Autowired
    private IAuditInfoService auditInfoService;

    @ApiOperation(value = "分页查询中转仓库信息")
    @PostMapping(value = "/findWarehouseInfoByPage")
    public CommonResult<CommonPageResult<WarehouseInfoVO>> findWarehouseInfoByPage(@RequestBody QueryWarehouseInfoForm form) {
        IPage<WarehouseInfoVO> iPage = warehouseInfoService.findWarehouseInfoByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑中转仓库信息")
    @PostMapping(value = "/saveOrUpdateWarehouseInfo")
    public CommonResult saveOrUpdateWarehouseInfo(@Valid @RequestBody AddWarehouseInfoForm form) {
        WarehouseInfo info = new WarehouseInfo().setId(form.getId())
                .setWarehouseName(form.getWarehouseName()).setWarehouseCode(form.getWarehouseCode());
        if (this.warehouseInfoService.checkUnique(info)) {
            return CommonResult.error(400, "中转仓仓库已存在");
        }

        WarehouseInfo warehouseInfo = ConvertUtil.convert(form, WarehouseInfo.class);
        if (this.warehouseInfoService.saveOrUpdateWarehouseInfo(warehouseInfo)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键获取中转仓库信息详情,id是中转仓库信息主键")
    @PostMapping(value = "/getWarehouseInfoById")
    public CommonResult getWarehouseInfoById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        WarehouseInfo warehouseInfo = this.warehouseInfoService.getById(id);
        return CommonResult.success(ConvertUtil.convert(warehouseInfo, WarehouseInfoVO.class));
    }

    @ApiOperation(value = "更改启用/禁用中转仓库状态,id是中转仓库信息主键")
    @PostMapping(value = "/enableOrDisableWarehouse")
    public CommonResult enableOrDisableWarehouse(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.warehouseInfoService.enableOrDisableWarehouse(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "审核中转仓")
    @PostMapping(value = "/auditWarehouse")
    public CommonResult auditWarehouse(@RequestBody AuditWarehouseForm form) {
        WarehouseInfo warehouseInfo = new WarehouseInfo();
        warehouseInfo.setId(form.getId());
        warehouseInfo.setWarehouseCode(form.getWarehouseCode());
        warehouseInfo.setAuditStatus(form.getAuditStatus());
        warehouseInfoService.updateById(warehouseInfo);

        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setExtId(form.getId());
        auditInfo.setAuditTypeDesc("中转仓审核");
        auditInfo.setAuditStatus(form.getAuditStatus());
        auditInfo.setAuditUser(form.getLoginUserName());
        auditInfo.setCreatedUser(form.getLoginUserName());
        auditInfo.setAuditTime(LocalDateTime.now());
        auditInfo.setExtDesc(SqlConstant.WAREHOUSE_INFO);
        auditInfoService.save(auditInfo);//保存操作记录
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑及审核中转仓代码是否可填 id = 客户ID")
    @PostMapping(value = "/isFillWarehouseCode")
    public CommonResult<Boolean> isFillWarehouseCode(@RequestBody Map<String,Object> param) {
        String warehouseIdStr = MapUtil.getStr(param, "id");
        if(StringUtil.isNullOrEmpty(warehouseIdStr)){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ext_id",Long.parseLong(warehouseIdStr));
        queryWrapper.eq("ext_desc", SqlConstant.WAREHOUSE_INFO);
        queryWrapper.eq("audit_status", "2");
        List<AuditInfo> auditInfos = auditInfoService.list(queryWrapper);
        if(auditInfos != null && auditInfos.size() > 0){
            return CommonResult.success(false);
        }
        return CommonResult.success(true);
    }
}
