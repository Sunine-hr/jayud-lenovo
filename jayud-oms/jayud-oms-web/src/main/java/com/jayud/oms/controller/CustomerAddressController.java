package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCustomerAddrForm;
import com.jayud.oms.model.bo.AddCustomerAddressForm;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CustomerAddress;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.DeliveryAddress;
import com.jayud.oms.model.po.RegionCity;
import com.jayud.oms.model.vo.CustomerAddrVO;
import com.jayud.oms.model.vo.CustomerAddressVO;
import com.jayud.oms.service.ICustomerAddressService;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.IRegionCityService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 客户地址 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/customerAddr")
public class CustomerAddressController {
    @Autowired
    private IRegionCityService regionCityService;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private ICustomerInfoService customerInfoService;
//    @Autowired
//    private RedisUtils redisUtils;


    @ApiOperation(value = "分页查询客户地址")
    @PostMapping(value = "/findCustomerAddressByPage")
    public CommonResult<CommonPageResult<CustomerAddrVO>> findCustomerAddressByPage(@RequestBody QueryCustomerAddressForm form) {
        IPage<CustomerAddrVO> iPage = customerAddressService.findCustomerAddressByPage(form);
//        iPage.getRecords().forEach(CustomerAddrVO::splicingAddress);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "查询客户地址")
    @PostMapping(value = "/list")
    public CommonResult<List<CustomerAddrVO>> list(@RequestBody QueryCustomerAddressForm form) {
        form.setPageSize(100000000);
        IPage<CustomerAddrVO> iPage = customerAddressService.findCustomerAddressByPage(form);
        return CommonResult.success(iPage.getRecords());
    }

    @ApiOperation(value = "新增编辑客户地址")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult saveOrUpdate(@Valid @RequestBody AddCustomerAddrForm form) {
        CustomerAddress customerAddress = ConvertUtil.convert(form, CustomerAddress.class);

        RegionCity province = regionCityService.getById(form.getProvince());
        RegionCity city = regionCityService.getById(form.getCity());
        RegionCity area = new RegionCity();
        if ((form.getArea() != null)) {
            area = regionCityService.getById(form.getArea());
        }
        form.assemblyLastAddr(province, city, area);
        customerAddress.setProvinceCode(province.getCode())
                .setCityCode(city.getCode()).setAreaCode(area.getCode())
                .setFinalAddress(form.getFinalAddress());

        return CommonResult.success(this.customerAddressService.saveOrUpdateAddr(customerAddress));
    }

    @ApiOperation(value = "启用/禁用客户地址,id是客户地址主键")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        CustomerAddress tmp = this.customerAddressService.getById(id);
        String status = StatusEnum.ENABLE.getCode().equals(String.valueOf(tmp.getStatus())) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();
        CustomerAddress customerAddress = new CustomerAddress().setId(id).setStatus(Integer.valueOf(status));
        return CommonResult.success(this.customerAddressService.saveOrUpdateAddr(customerAddress));
    }


    @ApiOperation(value = "根据主键获取客户地址详情,id是客户地址主键")
    @PostMapping(value = "/getAddrById")
    public CommonResult getAddrById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        CustomerAddress customerAddress = this.customerAddressService.getById(id);
        return CommonResult.success(ConvertUtil.convert(customerAddress, CustomerAddrVO.class));
    }


    @ApiOperation(value = "分页查询中港客户地址")
    @PostMapping(value = "/findTmsCustomerAddressByPage")
    public CommonResult<CommonPageResult<CustomerAddrVO>> findTmsCustomerAddressByPage(@RequestBody QueryCustomerAddressForm form) {
        IPage<CustomerAddrVO> iPage = customerAddressService.findCustomerAddressByPage(form);
//        iPage.getRecords().forEach(CustomerAddrVO::splicingAddress);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "分页查询订单客户地址")
    @PostMapping(value = "/findOrderCustomerAddressByPage")
    public CommonResult<CommonPageResult<CustomerAddrVO>> findOrderCustomerAddressByPage(@RequestBody QueryCustomerAddressForm form) {
        IPage<CustomerAddrVO> iPage = customerAddressService.findCustomerAddressByPage(form);

//        iPage.getRecords().forEach(CustomerAddrVO::splicingAddress);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }


    @ApiOperation(value = "查询订单客户地址")
    @PostMapping(value = "/getOrderCustomerAddress")
    public CommonResult<List<CustomerAddrVO>> getOrderCustomerAddress(@RequestBody QueryCustomerAddressForm form) {
        if (StringUtils.isEmpty(form.getCustomerCode())) {
            return CommonResult.error(400, "请选择客户");
        }
        form.setPageSize(1000000000);
        CustomerInfo customerInfo = this.customerInfoService.getByCode(form.getCustomerCode());
        form.setCustomerId(customerInfo.getId());
        IPage<CustomerAddrVO> iPage = customerAddressService.findCustomerAddressByPage(form);
        List<CustomerAddrVO> records = iPage.getRecords();
        if (records.size() == 0) {
            return CommonResult.success();
        }
        Map<Integer, String> citiesMap = this.regionCityService.list().stream().collect(Collectors.toMap(e -> e.getId().intValue(), e -> e.getName()));
        for (CustomerAddrVO record : records) {
            record.setProvinceDesc(citiesMap.get(record.getProvince()))
                    .setCityDesc(citiesMap.get(record.getCity()))
                    .setAreaDesc(citiesMap.get(record.getArea()));
        }
        return CommonResult.success(records);
    }


    @ApiOperation(value = "新增编辑订单客户地址")
    @PostMapping(value = "/saveOrUpdateOrderAddr")
    public CommonResult saveOrUpdateOrderAddr(@RequestBody AddCustomerAddrForm form) {
        form.checkOrderOptAdd();
        CustomerAddress customerAddress = ConvertUtil.convert(form, CustomerAddress.class);
        CustomerInfo customerInfo = this.customerInfoService.getByCode(form.getCustomerCode());
        form.setCustomerId(customerInfo.getId());

        RegionCity province = regionCityService.getById(form.getProvince());
        RegionCity city = regionCityService.getById(form.getCity());
        RegionCity area = new RegionCity();
        if ((form.getArea() != null)) {
            area = regionCityService.getById(form.getArea());
        }
        form.assemblyLastAddr(province, city, area);
        customerAddress.setProvinceCode(province.getCode())
                .setCityCode(city.getCode()).setAreaCode(area.getCode())
                .setFinalAddress(form.getFinalAddress());

        return CommonResult.success(this.customerAddressService.saveOrUpdateAddr(customerAddress));
    }
}

