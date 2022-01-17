package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.CorrespondEnum;
import com.jayud.scm.model.po.Customer;
import com.jayud.scm.model.po.CustomerTax;
import com.jayud.scm.model.po.VFeeModel;
import com.jayud.scm.model.vo.CustomerFormVO;
import com.jayud.scm.model.vo.CustomerOperatorVO;
import com.jayud.scm.model.vo.CustomerVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.ICustomerClassService;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ICustomerTaxService;
import io.swagger.annotations.*;
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

    @Autowired
    private ICustomerTaxService customerTaxService;

    @ApiOperation(value = "根据条件分页查询所有客户信息")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCustomerForm form) {
        form.setTime();
        if(form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null){
            return CommonResult.error(444,"该条件无法搜索");
        }
//        if(form.getClassType() != null){
//            form.setClassType(ibDataDicEntryService.getTextByDicCodeAndDataValue("1012",form.getClassType()));
//        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

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
//                    if(record.getCustomerStyle() != null){
//                        record.setCustomerStyle(ibDataDicEntryService.getTextByDicCodeAndDataValue("1010",record.getCustomerStyle()));
//
//                    }
//                    if(record.getCustomerState() != null){
//                        record.setCustomerState(ibDataDicEntryService.getTextByDicCodeAndDataValue("1013",record.getCustomerState()));
//                    }
//                    if(record.getArea() != null){
//                        record.setArea(ibDataDicEntryService.getTextByDicCodeAndDataValue("1015",record.getArea()));
//                    }
                    if(record.getBusinessType() != null){
                        String[] split = record.getBusinessType().split(",");
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String s : split) {
                            stringBuffer.append(ibDataDicEntryService.getTextByDicCodeAndDataValue("1011",s)).append(",");
                        }
                        record.setBusinessType(stringBuffer.toString().substring(0,stringBuffer.length()-1));
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
            customerVO.setCustomerStyleName(customerVO.getCustomerStyle());

        }
        if(customerVO.getCustomerState() != null){
            customerVO.setCustomerStateName(customerVO.getCustomerState());
        }
        if(customerVO.getArea() != null){
            customerVO.setAreaName(customerVO.getArea());
        }
        CustomerTax customerTax = customerTaxService.getCustomerTaxByCustomerId(customerVO.getId());
        if(customerTax != null){
            customerVO.setTaxName(customerTax.getTaxName());
            customerVO.setTaxAddress(customerTax.getTaxAddress());
            customerVO.setTaxBank(customerTax.getTaxBank());
            customerVO.setTaxTel(customerTax.getTaxTel());
            customerVO.setTaxBankNo(customerTax.getTaxBankNo());
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
        if(customer == null){
            customer = new Customer();
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

    @ApiOperation(value = "根据客户id，查询结算方案(结算条款)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customerId", dataType = "Integer", value = "客户id", required = true)
    })
    @PostMapping(value = "/findVFeeModelByCustomerId")
    public CommonResult<List<VFeeModel>> findVFeeModelByCustomerId(@RequestBody Map<String,Object> map){
        Integer customerId = MapUtil.getInt(map, "customerId");
        Integer modelType = MapUtil.getInt(map, "modelType");
        if(ObjectUtil.isEmpty(modelType)){
            return CommonResult.error(-1,"业务类型不能为空");
        }
        if(ObjectUtil.isEmpty(customerId)){
            return CommonResult.error(-1,"客户id不能为空");
        }
        List<VFeeModel> vFeeModelList = customerService.findVFeeModelByCustomerId(customerId,modelType);
        return CommonResult.success(vFeeModelList);
    }

    @ApiOperation(value = "根据客户id，查询客户的操作人员list信息（`商务员`、`业务员`、`客户下单人`）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="customerId", dataType = "Integer", value = "客户id", required = true)
    })
    @PostMapping(value = "/findCustomerOperatorByCustomerId")
    public CommonResult<CustomerOperatorVO> findCustomerOperatorByCustomerId(@RequestBody Map<String,Object> map){
        Integer customerId = MapUtil.getInt(map, "customerId");
        Integer modelType = MapUtil.getInt(map, "modelType");
        if(ObjectUtil.isEmpty(modelType)){
            return CommonResult.error(-1,"业务类型不能为空");
        }
        if(ObjectUtil.isEmpty(customerId)){
            return CommonResult.error(-1,"客户id不能为空");
        }
        CustomerOperatorVO customerOperatorVO = customerService.findCustomerOperatorByCustomerId(customerId,modelType);
        return CommonResult.success(customerOperatorVO);
    }

    @ApiOperation(value = "根据客户类型获取客户集合 ")
    @PostMapping(value = "/getCustomerByClassType")
    public CommonResult getCustomerByClassType(@RequestBody Map<String,Object> map) {
        String classType = MapUtil.getStr(map, "classType");
        if(null == classType ){
            return CommonResult.error(444,"客户类型不能为空");
        }
        List<CustomerVO> customerVOS = this.customerService.getCustomerByClassType(classType);
        if(CollectionUtil.isEmpty(customerVOS)){
            customerVOS = new ArrayList<>();
        }
        return CommonResult.success(customerVOS);
    }

}

