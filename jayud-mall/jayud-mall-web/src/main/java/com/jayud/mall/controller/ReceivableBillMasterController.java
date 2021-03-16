package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.BillMasterForm;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IReceivableBillMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/receivablebillmaster")
@Api(tags = "C017-client-应收账单接口")
@ApiSort(value = 17)
public class ReceivableBillMasterController {

    @Autowired
    IReceivableBillMasterService receivableBillMasterService;
    @Autowired
    BaseService baseService;

    //应收账单分页查询
    @ApiOperation(value = "应收账单分页查询")
    @PostMapping("/findReceivableBillMasterByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<ReceivableBillMasterVO>> findReceivableBillMasterByPage(
            @RequestBody QueryReceivableBillMasterForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId());
        IPage<ReceivableBillMasterVO> pageList = receivableBillMasterService.findReceivableBillMasterByPage(form);
        CommonPageResult<ReceivableBillMasterVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //应收账单-查看明细
    //lookDetail
    @ApiOperation(value = "应收账单-查看明细")
    @PostMapping("/lookDetail")
    @ApiOperationSupport(order = 2)
    public CommonResult<ReceivableBillMasterVO> lookDetail(@Valid @RequestBody BillMasterForm form){
        Long id = form.getId();
        return receivableBillMasterService.lookDetail(id);
    }



}
