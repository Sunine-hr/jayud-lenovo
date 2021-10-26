package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.oms.model.bo.AddElectronicFenceForm;
import com.jayud.oms.model.bo.QueryElectronicFenceForm;
import com.jayud.oms.model.po.ElectronicFence;
import com.jayud.oms.model.vo.ElectronicFenceVO;
import com.jayud.oms.service.IElectronicFenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 电子围栏 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-25
 */
@RestController
@Api(tags = "电子围栏")
@RequestMapping("/electronicFence")
public class ElectronicFenceController {

    @Autowired
    private IElectronicFenceService electronicFenceService;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<ElectronicFenceVO>> findByPage(@RequestBody QueryElectronicFenceForm form) {
        IPage<ElectronicFenceVO> iPage = this.electronicFenceService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增/编辑电子围栏")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddElectronicFenceForm form) {
        form.setLoAndLa(form.getLo() + "," + form.getLa());
        this.electronicFenceService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "查询电子围栏详情")
    @PostMapping(value = "/getById")
    public CommonResult<ElectronicFence> getById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        ElectronicFence electronicFence = this.electronicFenceService.getById(id);
        return CommonResult.success(electronicFence);
    }

    @ApiOperation(value = "删除电子围栏信息")
    @PostMapping(value = "/delete")
    public CommonResult<ElectronicFence> delete(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.electronicFenceService.updateById(new ElectronicFence().setId(id)
                .setStatus(StatusEnum.DELETE.getCode()).setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken()));
        return CommonResult.success();
    }
}

