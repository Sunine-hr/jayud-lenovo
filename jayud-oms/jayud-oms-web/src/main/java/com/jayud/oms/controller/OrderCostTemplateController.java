package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.model.bo.OrderCostTemplateDTO;
import com.jayud.oms.model.bo.OrderCostTemplateInfoDTO;
import com.jayud.oms.model.bo.QueryCostTemplateForm;
import com.jayud.oms.model.po.OrderCostTemplate;
import com.jayud.oms.model.vo.CurrencyInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.service.ICurrencyInfoService;
import com.jayud.oms.service.IOrderCostTemplateInfoService;
import com.jayud.oms.service.IOrderCostTemplateService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 费用模板 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/orderCostTemplate")
public class OrderCostTemplateController {

    @Autowired
    private IOrderCostTemplateService orderCostTemplateService;
    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private IOrderCostTemplateInfoService orderCostTemplateInfoService;


    @ApiOperation(value = "创建/编辑模板信息")
    @PostMapping(value = "/saveOrUpdateInfo")
    public CommonResult saveOrUpdateInfo(@RequestBody OrderCostTemplateDTO orderCostTemplateDTO) {
        //检验参数
        orderCostTemplateDTO.checkAddOpt();

        OrderCostTemplate tmp = new OrderCostTemplate().setId(orderCostTemplateDTO.getId())
                .setName(orderCostTemplateDTO.getName());
        if (this.orderCostTemplateService.checkUnique(tmp)) {
            return CommonResult.error(400, "模板名称已经存在");
        }
        //查询币种
        List<InitComboxStrVO> initComboxStrVOS = this.currencyInfoService.initCurrencyInfo();
        Map<String, String> currencyMap = initComboxStrVOS.stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));

        if (CollectionUtils.isNotEmpty(orderCostTemplateDTO.getCostTemplateInfo())) {
            orderCostTemplateDTO.getCostTemplateInfo().stream().map(e -> e.setCurrency(currencyMap.get(e.getCostCode())));
        }

        //创建/修改模板数据
        this.orderCostTemplateService.saveOrUpdateInfo(orderCostTemplateDTO);
        return CommonResult.success();
    }


    @ApiOperation(value = "分页查询模板")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<OrderCostTemplateDTO>> findByPage(@RequestBody QueryCostTemplateForm form) {
        form.setCreateUser(UserOperator.getToken());
        IPage<OrderCostTemplateDTO> page = this.orderCostTemplateService.findByPage(form);
        return CommonResult.success(new CommonPageResult(page));
    }


    @ApiOperation(value = "更改启用/禁用状态,id是主键")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.orderCostTemplateService.enableOrDisableCostInfo(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "查询费用模板详情信息")
    @PostMapping(value = "/getCostTemplateInfo")
    public CommonResult getCostTemplateInfo(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        return CommonResult.success(this.orderCostTemplateService.getCostTemplateInfo(id));
    }


    @ApiOperation(value = "下拉模板数据")
    @PostMapping(value = "/initCostTemplate")
    public CommonResult initCostTemplate(@RequestBody Map<String, String> map) {
//        Long name = MapUtil.getLong(map, "name");
//        String type = MapUtil.getStr(map, "type");
//        if (StringUtils.isEmpty(type)) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
        List<InitComboxStrVO> receivables = new ArrayList<>();
        List<InitComboxStrVO> pays = new ArrayList<>();
        for (InitComboxStrVO initComboxVO : this.orderCostTemplateService.initCostTemplate()) {
            if ("0".equals(initComboxVO.getNote())) {
                receivables.add(initComboxVO);
            } else {
                pays.add(initComboxVO);
            }
        }
        Map<String, Object> obj = new HashMap<>();
        obj.put("receivables", receivables);
        obj.put("payment", pays);
        return CommonResult.success(obj);
    }


    @ApiOperation(value = "返回导入模板数据")
    @PostMapping(value = "/returnImportTemplateData")
    public CommonResult<List<OrderCostTemplateInfoDTO>> returnImportTemplateData(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        String createdTimeStr = MapUtil.getStr(map, "createdTimeStr");
        if (id == null || StringUtils.isEmpty(createdTimeStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
       OrderCostTemplateDTO costTemplateInfo = this.orderCostTemplateService.getCostTemplateInfo(id);

        List<OrderCostTemplateInfoDTO> costTemplateInfos = costTemplateInfo.getCostTemplateInfo();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo(createdTimeStr);
        Map<String, CurrencyInfoVO> tmp = currencyInfos.stream().collect(Collectors.toMap(CurrencyInfoVO::getCurrencyCode, e -> e));
        List<OrderCostTemplateInfoDTO> list = costTemplateInfos.stream()
                .map(e -> {
                    CurrencyInfoVO currencyInfoVO = tmp.get(e.getCurrencyCode());
                    e.setCurrencyCode(currencyInfoVO==null?null:currencyInfoVO.getCurrencyCode());
                    return e.setCurrency(currencyInfoVO==null?null:currencyInfoVO.getCurrencyName());
                }).collect(Collectors.toList());

        return CommonResult.success(list);
    }
}

