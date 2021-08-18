package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddHubShippingEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.HubReceivingEntryVO;
import com.jayud.scm.model.vo.HubShippingEntryVO;
import com.jayud.scm.service.IHubShippingEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 出库单明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubShippingEntry")
@Api(tags = "出库明细管理")
public class HubShippingEntryController {

    @Autowired
    private IHubShippingEntryService hubShippingEntryService;

    @ApiOperation(value = "根据出库单id获取出库单详情")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<HubShippingEntryVO> page = this.hubShippingEntryService.findByPage(form);
        for (HubShippingEntryVO record : page.getRecords()) {

        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增或修改出库单明细")
    @PostMapping(value = "/saveOrUpdateHubShippingEntry")
    public CommonResult saveOrUpdateHubShippingEntry(@RequestBody List<AddHubShippingEntryForm> form) {
        //todo 新增或修改，所填的值不能超过带出来的委托单的值


        boolean result = hubShippingEntryService.saveOrUpdateHubShippingEntry(form);
        if(!result){
            return CommonResult.error(444,"新增或修改出库订单明细失败");
        }
        return CommonResult.success();
    }
}

