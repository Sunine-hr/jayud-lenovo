package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerTaxVO;
import com.jayud.scm.model.vo.HubReceivingEntryVO;
import com.jayud.scm.service.IHubReceivingEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 入库明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubReceivingEntry")
@Api(tags = "入库明细管理")
public class HubReceivingEntryController {

    @Autowired
    private IHubReceivingEntryService hubReceivingEntryService;

    @ApiOperation(value = "根据入库单id获取入库单详情")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<HubReceivingEntryVO> page = this.hubReceivingEntryService.findByPage(form);
        for (HubReceivingEntryVO record : page.getRecords()) {

        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

