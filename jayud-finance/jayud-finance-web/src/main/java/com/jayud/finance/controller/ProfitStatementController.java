package com.jayud.finance.controller;


import com.jayud.common.CommonResult;
import com.jayud.finance.bo.QueryProfitStatementForm;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.service.IProfitStatementService;
import com.jayud.finance.vo.ProfitStatementVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 利润报表 前端控制器
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@RestController
@RequestMapping("/profitStatement")
public class ProfitStatementController {

    @Autowired
    private IProfitStatementService profitStatementService;

    @ApiOperation(value = "利润报表查询")
    @PostMapping("list")
    private CommonResult<List<ProfitStatementVO>> list(@RequestBody QueryProfitStatementForm form) {
        List<ProfitStatementVO> list = this.profitStatementService.list(form);
        return CommonResult.success(list);
    }
}

