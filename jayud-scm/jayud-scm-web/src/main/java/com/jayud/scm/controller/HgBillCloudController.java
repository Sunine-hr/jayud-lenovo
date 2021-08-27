package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.CustomerTaxVO;
import com.jayud.scm.model.vo.HgBillCloudVO;
import com.jayud.scm.service.IHgBillCloudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 报关单一对接跟踪记录表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-26
 */
@RestController
@RequestMapping("/hgBillCloud")
@Api(tags = "报关单一对接跟踪记录管理")
public class HgBillCloudController {

    @Autowired
    private IHgBillCloudService hgBillCloudService;

    @ApiOperation(value = "根据报关单id查询所有报关的报关信息")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<HgBillCloudVO>> findByPage(@RequestBody QueryCommonForm form) {
        IPage<HgBillCloudVO> page = this.hgBillCloudService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

}

