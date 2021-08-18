package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.ICustomerRelationerService;
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
 * 客户联系人表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/customerRelationer")
@Api(tags = "客户联系人")
public class CustomerRelationerController {

    @Autowired
    private ICustomerRelationerService customerRelationerService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询所有该客户的所有联系人")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<CustomerRelationerVO> page = this.customerRelationerService.findByPage(form);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            for (CustomerRelationerVO record : page.getRecords()) {
                record.setSTypeName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1014",record.getSType()));
            }
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户联系人")
    @PostMapping(value = "/saveOrUpdateCustomerRelationer")
    public CommonResult saveOrUpdateCustomerRelationer(@RequestBody AddCustomerRelationerForm form) {
        boolean result = customerRelationerService.saveOrUpdateCustomerRelationer(form);
        if(!result){
            return CommonResult.error(444,"新增客户联系人失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改默认值")
    @PostMapping(value = "/modifyDefaultValues")
    public CommonResult modifyDefaultValues(@RequestBody AddCustomerRelationerForm form) {
        boolean result = customerRelationerService.modifyDefaultValues(form);
        if(!result){
            return CommonResult.error(444,"修改客户联系人默认值失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取联系人信息的详情")
    @PostMapping(value = "/getCustomerRelationerById")
    public CommonResult<CustomerRelationerVO> getCustomerRelationerById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        CustomerRelationerVO customerRelationerVO = customerRelationerService.getCustomerRelationerById(id);
        customerRelationerVO.setSTypeName(ibDataDicEntryService.getTextByDicCodeAndDataValue("1014",customerRelationerVO.getSType()));

        return CommonResult.success(customerRelationerVO);
    }

}

