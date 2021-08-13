package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerAddressForm;
import com.jayud.scm.model.bo.AddCustomerBankForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerAddressVO;
import com.jayud.scm.model.vo.CustomerBankVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.ICustomerAddressService;
import com.jayud.scm.service.ICustomerBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 客户常用地址表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerAddress")
@Api(tags = "客户地址管理")
public class CustomerAddressController {

    @Autowired
    private ICustomerAddressService customerAddressService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询所有该客户的常住地址")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerAddressVO> page = this.customerAddressService.findByPage(form);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            for (CustomerAddressVO record : page.getRecords()) {
                if(record.getSType() != null){
                    record.setSTypeName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1018",record.getSType()));
                }
                if(record.getRegion() != null){
                    record.setRegionName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1015",record.getRegion()));
                }
            }
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户地址信息")
    @PostMapping(value = "/saveOrUpdateCustomerAddress")
    public CommonResult saveOrUpdateCustomerAddress(@RequestBody AddCustomerAddressForm form) {
        form.setRegion(form.getRegionName());
        form.setSType(form.getSTypeName());
        boolean result = customerAddressService.saveOrUpdateCustomerAddress(form);
        if(!result){
            return CommonResult.error(444,"新增客户常住地址失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改默认值")
    @PostMapping(value = "/modifyDefaultValues")
    public CommonResult modifyDefaultValues(@RequestBody AddCustomerAddressForm form) {
        boolean result = customerAddressService.modifyDefaultValues(form);
        if(!result){
            return CommonResult.error(444,"修改客户常住地址默认值失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取地址信息的详情")
    @PostMapping(value = "/getCustomerAddressById")
    public CommonResult<CustomerAddressVO> getCustomerAddressById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerAddressVO customerAddressVO = customerAddressService.getCustomerAddressById(id);
        return CommonResult.success(customerAddressVO);
    }

}

