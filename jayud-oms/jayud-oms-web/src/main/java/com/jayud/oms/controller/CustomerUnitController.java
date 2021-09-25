package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.AddCustomerUnitForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.CustomerUnit;
import com.jayud.oms.model.vo.CustomerUnitVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.ICustomerUnitService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户结算单位 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/customerUnit")
public class CustomerUnitController {

    @Autowired
    private ICustomerUnitService customerUnitService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private ICustomerInfoService customerInfoService;

    @ApiOperation(value = "新增/编辑")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddCustomerUnitForm form) {
        CustomerUnit convert = ConvertUtil.convert(form, CustomerUnit.class);
        //业务类型+操作部门的唯一性
        if (!this.customerUnitService.checkUnique(form.getId(), form.getCustomerId(), form.getBusinessType()
                , form.getOptDepartmentId())) {
            return CommonResult.error(400, "该结算单位已存在");
        }
        this.customerUnitService.saveOrUpdateUnit(convert);

        return CommonResult.success();
    }

    @ApiOperation(value = "获取客户结算单位数据")
    @PostMapping("/list")
    public CommonResult<List<CustomerUnitVO>> list(@RequestBody Map<String, Object> map) {

        Long customerId = MapUtil.getLong(map, "customerId");
        if (customerId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }

        List<CustomerUnit> list = this.customerUnitService.getByCondition(new CustomerUnit().setCustomerId(customerId).setStatus(StatusEnum.ENABLE.getCode()));

//        List<CustomerUnit> list = this.customerUnitService.list();
        List<CustomerUnitVO> results = new ArrayList<>();
        if (list.size() == 0) {
            return CommonResult.success(results);
        }
        Map<Long, String> departmentMap = this.oauthClient.findDepartment().getData().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        Map<String, String> customerMap = customerInfoService.list().stream().filter(e -> !StringUtils.isEmpty(e.getIdCode())).collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
        for (CustomerUnit customerUnit : list) {
            CustomerUnitVO customerUnitVO = ConvertUtil.convert(customerUnit, CustomerUnitVO.class);
            String desc = SubOrderSignEnum.getEnum(customerUnit.getBusinessType()).getDesc();
            customerUnitVO.setBusinessTypeDesc(desc);
            customerUnitVO.setOptDepartmentDesc(departmentMap.get(customerUnit.getOptDepartmentId()));
            customerUnitVO.setUnitDesc(customerMap.get(customerUnit.getUnitCode()));
            results.add(customerUnitVO);
        }

        return CommonResult.success(results);
    }

    @ApiOperation(value = "删除客户结算单位")
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.customerUnitService.updateById(new CustomerUnit().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }


    @ApiOperation(value = "下拉客户结算单位")
    @PostMapping(value = "/initCustomerUnit")
    public CommonResult<Map<String, Object>> initCustomerUnit(@RequestBody Map<String, Object> param) {

        //获取当前用户所属法人主体
        Map<String, Object> resultMap = new HashMap<>();
        //客户
        List<CustomerInfo> allCustomerInfoList = customerInfoService.getCustomerInfoByCondition(null);
        List<CustomerInfo> customerInfoList = allCustomerInfoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CustomerInfo::getName))), ArrayList::new));
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getIdCode());
            comboxStrVO.setName(customerInfo.getName());
            comboxStrVO.setId(customerInfo.getId());
            comboxStrVOS.add(comboxStrVO);
        }

        //业务所属部门
        List<InitComboxVO> initComboxVOS = oauthClient.findDepartment().getData();

        List<com.jayud.common.entity.InitComboxStrVO> initComboxStrVOS = SubOrderSignEnum.initBusinessType();
        resultMap.put("units", comboxStrVOS);
        resultMap.put(CommonConstant.DEPARTMENTS, initComboxVOS);
        resultMap.put("businessType", initComboxStrVOS);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "获取客户结算单位")
    @PostMapping("/getCustomerSettlementUnit")
    public CommonResult getCustomerSettlementUnit(@RequestBody Map<String, Object> map) {
        String customerCode = MapUtil.getStr(map, "customerCode");
        String type = MapUtil.getStr(map, "type");
        Long departmentId = MapUtil.getLong(map, "departmentId");
        if (StringUtils.isEmpty(type) || departmentId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(customerCode)) {
            return CommonResult.error(400, "请选择客户");
        }
        CustomerInfo customerInfo = this.customerInfoService.getByCode(customerCode);
        List<CustomerUnit> list = this.customerUnitService.getByCondition(new CustomerUnit().setCustomerId(customerInfo.getId()).setStatus(StatusEnum.ENABLE.getCode())
                .setBusinessType(type).setOptDepartmentId(departmentId));

        return CommonResult.success(list.size() > 0 ? list.get(0).getUnitCode() : "");
    }
}

