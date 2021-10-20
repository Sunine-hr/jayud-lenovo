package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCustomerLineForm;
import com.jayud.oms.model.bo.AddLineForm;
import com.jayud.oms.model.bo.QueryCustomerLineForm;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.po.CustomerLine;
import com.jayud.oms.model.po.CustomerLineRelation;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.po.Line;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户线路管理 前端控制器
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@RestController
@RequestMapping("/customerLine")
@Api(tags = "客户线路管理")
public class CustomerLineController {

    @Autowired
    private ILineService lineService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private IOrderTypeNumberService orderTypeNumberService;

    @Autowired
    private ICustomerLineService customerLineService;

    @Autowired
    private ICustomerLineRelationService customerLineRelationService;

    @Autowired
    private IDriverInfoService driverInfoService;

    @ApiOperation(value = "分页查询客户线路")
    @PostMapping(value = "/findCustomerLineByPage")
    public CommonResult<CommonPageResult<CustomerLineVO>> findCustomerLineByPage(@Valid @RequestBody QueryCustomerLineForm form) {
        IPage<CustomerLineVO> iPage = customerLineService.findCustomerLineByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "新增编辑客户线路")
    @PostMapping(value = "/saveOrUpdateCustomerLine")
    public CommonResult saveOrUpdateCustomerLine(@Valid @RequestBody AddCustomerLineForm form) {
        CustomerLine customerLineTemp = new CustomerLine().setId(form.getId()).setCustomerLineName(form.getCustomerLineName());
        customerLineService.checkUnique(customerLineTemp);

        if (this.customerLineService.saveOrUpdateCustomerLine(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "删除客户线路，id=客户线路id")
    @PostMapping(value = "/delCustomerLine")
    public CommonResult delCustomerLine(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = MapUtil.getLong(map, "id");
        if (!customerLineService.checkExists(id)) {
            return CommonResult.error(400, "客户线路不存在");
        }
        customerLineService.delLine(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "查看客户线路详情 id=客户线路id")
    @PostMapping(value = "/getCustomerLineDetails")
    public CommonResult<CustomerLineDetailsVO> getCustomerLineDetails(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        CustomerLineDetailsVO lineDetailsVO = this.customerLineService.getCustomerLineDetails(id);
        List<CustomerLineRelation> customerLineRelations = customerLineRelationService.getListByCustomerLineId(lineDetailsVO.getId());
        lineDetailsVO.setCustomerLineRelations(customerLineRelations);
        return CommonResult.success(lineDetailsVO);
    }

    @ApiOperation(value = "获取客户线路编号")
    @PostMapping(value = "/getCustomerLineCode")
    public CommonResult getCustomerLineCode() {
        String orderNo = orderTypeNumberService.getNumberOfDay( "KH");
        return CommonResult.success(orderNo);
    }

    @ApiOperation(value = "客户线路新增-下拉框合并返回")
    @PostMapping(value = "/findComboxs")
    public CommonResult<Map<String, Object>> findComboxs() {
        Map<String, Object> resultMap = new HashMap<>();
        //线路规则
        resultMap.put("lineRules", initLineRule().getData());
        //司机
        resultMap.put("driverInfos", initEnableDriverInfo(null).getData());
        //审核通过的线路
        resultMap.put("lines", initEnableLine(null).getData());
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "客户线路新增-下拉-线路规则")
    @PostMapping(value = "/initLineRule")
    public CommonResult<List<InitComboxStrVO>> initLineRule() {
        List<InitComboxStrVO> list = dictService.getInitComboxByDictTypeCode("lineRule");
        return CommonResult.success(list);
    }

    @ApiOperation(value = "客户线路新增-下拉-有效司机 driverName=查询审核通过的司机")
    @PostMapping(value = "/initEnableDriverInfo")
    public CommonResult<List<DriverInfo>> initEnableDriverInfo(@RequestBody Map<String, Object> map) {
        String driverName = MapUtil.getStr(map, "driverName");
        return CommonResult.success(this.driverInfoService.getEnableDriverInfo(driverName));
    }

    @ApiOperation(value = "客户线路新增-下拉-审核通过的线路 lineName=查询审核通过的线路")
    @PostMapping(value = "/initEnableLine")
    public CommonResult<List<Line>> initEnableLine(@RequestBody Map<String, Object> map) {
        String lineName = MapUtil.getStr(map, "lineName");
        return CommonResult.success(this.lineService.getEnableLine(lineName));
    }

}

