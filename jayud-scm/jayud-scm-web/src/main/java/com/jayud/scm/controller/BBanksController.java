package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.vo.BBanksVO;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.service.IBBanksService;
import com.jayud.scm.service.IBDataDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 公司银行账户 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@RestController
@RequestMapping("/bBanks")
@Api(tags = "公司银行账户管理")
public class BBanksController {

    @Autowired
    private IBBanksService banksService;

    @ApiOperation(value = "根据条件分页查询字典信息")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<BBanksVO>> findByPage(@RequestBody QueryForm form) {

        IPage<BBanksVO> page = this.banksService.findByPage(form);
        CommonPageResult<BBanksVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);

    }

}

