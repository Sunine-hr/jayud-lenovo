package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddLineForm;
import com.jayud.oms.model.bo.AuditLineForm;
import com.jayud.oms.model.bo.LineBatchOprForm;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.po.Dict;
import com.jayud.oms.model.po.Line;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.LineDetailsVO;
import com.jayud.oms.model.vo.LineVO;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 线路管理 前端控制器
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
@RestController
@RequestMapping("/line")
@Api(tags = "线路管理")
public class LineController {

    @Autowired
    private ILineService lineService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IOrderTypeNumberService orderTypeNumberService;

    @Autowired
    private ICustomerLineService customerLineService;

    @ApiOperation(value = "分页查询线路")
    @PostMapping(value = "/findLineByPage")
    public CommonResult<CommonPageResult<LineVO>> findLineByPage(@Valid @RequestBody QueryLineForm form) {
        IPage<LineVO> iPage = lineService.findLineByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑线路")
    @PostMapping(value = "/saveOrUpdateLine")
    public CommonResult saveOrUpdateLine(@Valid @RequestBody AddLineForm form) {
        Line lineTemp = new Line().setId(form.getId()).setLineName(form.getLineName());
        if (this.lineService.checkUniqueByName(lineTemp)) {
            return CommonResult.error(400, "线路名称已存在");
        }

        lineTemp = new Line().setId(form.getId()).setLineCode(form.getLineCode());
        if (this.lineService.checkUniqueByCode(lineTemp)) {
            return CommonResult.error(400, "线路编号已存在");
        }

        Line line = ConvertUtil.convert(form, Line.class);
        if (this.lineService.saveOrUpdateLine(line)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "批量操作")
    @PostMapping(value = "/batchOprLine")
    public CommonResult batchOprLine(@Valid @RequestBody LineBatchOprForm form) {
        lineService.batchOprLine(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "删除线路，id=线路id")
    @PostMapping(value = "/delLine")
    public CommonResult delLine(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = MapUtil.getLong(map, "id");
        if (!lineService.checkExists(id)) {
            return CommonResult.error(400, "线路不存在");
        }
        // 检查客户线路关联数据
        boolean result = customerLineService.isExistLineRelation(id);
        if (!result) {
            return CommonResult.error(400, "该线路存在客户线路关联数据，不允许删除");
        }
        lineService.delLine(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "查看线路详情 id=线路id")
    @PostMapping(value = "/getLineDetails")
    public CommonResult<LineDetailsVO> getLineDetails(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        LineDetailsVO lineDetailsVO = this.lineService.getLineDetails(id);
        return CommonResult.success(lineDetailsVO);
    }

    @ApiOperation(value = "获取线路编号")
    @PostMapping(value = "/getLineCode")
    public CommonResult getLineCode() {
        String orderNo = orderTypeNumberService.getNumberOfDay( "XL");
        return CommonResult.success(orderNo);
    }

    @ApiOperation(value = "线路新增-下拉选择-线路类型")
    @PostMapping(value = "/initLineType")
    public CommonResult initLineType() {
        List<InitComboxStrVO> list = dictService.getInitComboxByDictTypeCode("lineType");
        return CommonResult.success(list);
    }

    @ApiOperation(value = "线路新增-下拉选择-路由属性")
    @PostMapping(value = "/initRouteAttribute")
    public CommonResult initRouteAttribute() {
        List<InitComboxStrVO> list = dictService.getInitComboxByDictTypeCode("routeAttribute");
        return CommonResult.success(list);
    }

    @ApiOperation(value = "线路审核")
    @PostMapping(value = "/auditLine")
    public CommonResult auditLine(@Valid @RequestBody AuditLineForm form) {
        if (!lineService.checkExists(form.getId())) {
            return CommonResult.error(400, "线路不存在");
        }
        lineService.auditLine(form);
        return CommonResult.success();
    }

}

