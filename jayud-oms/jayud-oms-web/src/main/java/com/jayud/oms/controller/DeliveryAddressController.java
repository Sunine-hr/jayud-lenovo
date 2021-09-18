//package com.jayud.oms.controller;
//
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.jayud.common.CommonPageResult;
//import com.jayud.common.CommonResult;
//import com.jayud.common.enums.ResultEnum;
//import com.jayud.common.utils.ConvertUtil;
//import com.jayud.oms.model.bo.AddCustomerAddressForm;
//import com.jayud.oms.model.bo.QueryCustomerAddressForm;
//import com.jayud.oms.model.enums.StatusEnum;
//import com.jayud.oms.model.po.DeliveryAddress;
//import com.jayud.oms.model.po.RegionCity;
//import com.jayud.oms.model.vo.CustomerAddressVO;
//import com.jayud.oms.model.vo.DriverInfoVO;
//import com.jayud.oms.service.IDeliveryAddressService;
//import com.jayud.oms.service.IRegionCityService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.util.Map;
//
///**
// * <p>
// * 提货地址基础数据表 前端控制器
// * </p>
// *
// * @author 李达荣
// * @since 2020-11-12
// */
//@RestController
//@RequestMapping("/customerAddress")
//@Api(tags = "客户地址维护")
//public class DeliveryAddressController {
//
//    @Autowired
//    private IDeliveryAddressService deliveryAddressService;
//    @Autowired
//    private IRegionCityService regionCityService;
//
//    @ApiOperation(value = "分页查询客户地址")
//    @PostMapping(value = "/findCustomerAddressByPage")
//    public CommonResult<CommonPageResult<CustomerAddressVO>> findCustomerAddressByPage(@RequestBody QueryCustomerAddressForm form) {
//        IPage<CustomerAddressVO> iPage = deliveryAddressService.findCustomerAddressByPage(form);
//        iPage.getRecords().forEach(CustomerAddressVO::splicingAddress);
//        return CommonResult.success(new CommonPageResult<>(iPage));
//    }
//
//    @ApiOperation(value = "新增编辑客户地址")
//    @PostMapping(value = "/saveOrUpdateCustomerAddress")
//    public CommonResult saveOrUpdateCustomerAddress(@Valid @RequestBody AddCustomerAddressForm form) {
//        DeliveryAddress deliveryAddress = ConvertUtil.convert(form, DeliveryAddress.class);
//
//        //查询省，市代码,默认国家代码
//        RegionCity province = regionCityService.getById(form.getProvince());
//        RegionCity city = regionCityService.getById(form.getCity());
//        deliveryAddress.setCountryName("中国")
//                .setCountryCode(56)
//                .setStateCode(province.getCode())
//                .setStateName(province.getName())
//                .setCityCode(city.getCode())
//                .setCityName(city.getName());
//
//
//        if (this.deliveryAddressService.saveOrUpdateCustomerAddress(deliveryAddress)) {
//            return CommonResult.success();
//        } else {
//            return CommonResult.error(ResultEnum.OPR_FAIL);
//        }
//    }
//
//    @ApiOperation(value = "启用/禁用客户地址,id是客户地址主键")
//    @PostMapping(value = "/enableOrDisableCustomerAddress")
//    public CommonResult enableOrDisableCustomerAddress(@RequestBody Map<String, String> map) {
//        if (StringUtils.isEmpty(map.get("id"))) {
//            return CommonResult.error(500, "id is required");
//        }
//        Integer id = Integer.valueOf(map.get("id"));
//        DeliveryAddress tmp = this.deliveryAddressService.getById(id);
//        String status = StatusEnum.ENABLE.getCode().equals(String.valueOf(tmp.getStatus())) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();
//        DeliveryAddress deliveryAddress = new DeliveryAddress().setId(id).setStatus(Integer.valueOf(status));
//        if (this.deliveryAddressService.saveOrUpdateCustomerAddress(deliveryAddress)) {
//            return CommonResult.success();
//        } else {
//            return CommonResult.error(ResultEnum.OPR_FAIL);
//        }
//    }
//
//    @ApiOperation(value = "根据主键获取客户地址详情,id是客户地址主键")
//    @PostMapping(value = "/getCustomerAddressById")
//    public CommonResult getCustomerAddressById(@RequestBody Map<String, String> map) {
//        if (StringUtils.isEmpty(map.get("id"))) {
//            return CommonResult.error(500, "id is required");
//        }
//        Long id = Long.parseLong(map.get("id"));
//        DeliveryAddress deliveryAddress = this.deliveryAddressService.getById(id);
//        return CommonResult.success(ConvertUtil.convert(deliveryAddress, CustomerAddressVO.class));
//    }
//}
//
