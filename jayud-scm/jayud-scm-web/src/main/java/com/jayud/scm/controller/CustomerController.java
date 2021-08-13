package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.Customer;
import com.jayud.scm.model.vo.CustomerFormVO;
import com.jayud.scm.model.vo.CustomerVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.ICustomerClassService;
import com.jayud.scm.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "客户主体管理")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerClassService customerClassService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询所有客户信息")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCustomerForm form) {
        form.setTime();

        List list = new ArrayList();
        //获取表头信息
        Class<CustomerFormVO> customerFormVOClass = CustomerFormVO.class;
        Field[] declaredFields = customerFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                Map map = new HashMap<>();
                map.put("key", declaredField.getName());
                map.put("name", annotation.value());
                list.add(map);
            }
        }
        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<CustomerFormVO> page = this.customerService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
        }else {
            if(CollectionUtils.isNotEmpty(page.getRecords())){
                for (CustomerFormVO record : page.getRecords()) {
                    if(record.getCustomerStyle() != null){
                        record.setCustomerStyleName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1010",record.getCustomerStyle()));

                    }
                    if(record.getCustomerState() != null){
                        record.setCustomerStateName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1011",record.getCustomerState()));
                    }
                    if(record.getArea() != null){
                        record.setAreaName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1015",record.getArea()));
                    }
                }
            }

            CommonPageResult<CustomerFormVO> pageVO = new CommonPageResult(page);
            map1.put("pageInfo", pageVO);
        }

        return CommonResult.success(map1);
    }

    @ApiOperation(value = "根据id查询客户信息")
    @PostMapping(value = "/getCustomerById")
    public CommonResult<CustomerVO> getCustomerById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerVO customerVO = this.customerService.getCustomerById(id);
        if(customerVO.getCustomerStyle() != null){
            customerVO.setCustomerStyleName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1010",customerVO.getCustomerStyle()));

        }
        if(customerVO.getCustomerState() != null){
            customerVO.setCustomerStateName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1011",customerVO.getCustomerState()));
        }
        if(customerVO.getArea() != null){
            customerVO.setAreaName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1015",customerVO.getArea()));
        }
        return CommonResult.success(customerVO);
    }

    @ApiOperation(value = "添加客户信息")
    @PostMapping(value = "/addCustomer")
    public CommonResult addCustomer(@RequestBody @Valid AddCustomerNameForm form) {
        Customer customer = customerService.getCustomer(form.getCustomerName());
        if(customer != null){
            return CommonResult.error(444,"客户名称已存在");
        }
        boolean result = customerService.addCustomer(form);
        if(!result){
            return CommonResult.error(444,"客户添加失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "判断客户名称是否重复")
    @PostMapping(value = "/isRepeat")
    public CommonResult isRepeat(@RequestBody Map<String,Object> map) {
        String customerName = MapUtil.getStr(map, "customerName");
        Customer customer = customerService.getCustomer(customerName);
        if(customer != null){
            return CommonResult.error(444,"客户名称已存在");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改客户信息")
    @PostMapping(value = "/updateCustomer")
    public CommonResult updateCustomer(@RequestBody @Valid AddCustomerForm form) {
        boolean result = customerService.updateCustomer(form);
        if(!result){
            return CommonResult.error(444,"客户修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改客户名称")
    @PostMapping(value = "/updateCustomerName")
    public CommonResult updateCustomerName(@RequestBody Map<String,Object> map) {
        String customerName = MapUtil.getStr(map, "customerName");
        Integer id = MapUtil.getInt(map, "id");
        Customer customer = customerService.getCustomer(customerName);
        if(customer != null && !customer.getId().equals(id)){
            return CommonResult.error(444,"客户名称已存在");
        }
        customer.setId(id);
        customer.setCustomerName(customerName);
        boolean result = customerService.updateCustomerName(customer);
        if(!result){
            return CommonResult.error(444,"客户修改失败");
        }
        return CommonResult.success();

    }

    @ApiOperation(value = "客户类型设置")
    @PostMapping(value = "/customerTypeSettings")
    public CommonResult customerTypeSettings(@RequestBody @Valid AddCustomerTypeForm form) {
        boolean result = customerClassService.updateCustomerClass(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"客户类型设置失败");
    }

    @ApiOperation(value = "财务编号设置")
    @PostMapping(value = "/financialNumberSetting")
    public CommonResult financialNumberSetting(@RequestBody @Valid AddCustomerTypeForm form) {
        boolean result = customerClassService.financialNumberSetting(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"财务编号设置失败");
    }

    @ApiOperation(value = "客户跟进")
    @PostMapping(value = "/AddCustomerFollow")
    public CommonResult AddCustomerFollow(@RequestBody AddCustomerFollowForm followForm) {
        boolean result = customerService.AddCustomerFollow(followForm);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"跟踪信息添加失败");
    }


}

