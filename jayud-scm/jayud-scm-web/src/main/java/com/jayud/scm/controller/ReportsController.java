package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddAcctPayForm;
import com.jayud.scm.model.bo.AddReportsForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.Reports;
import com.jayud.scm.model.vo.ReportsVO;
import com.jayud.scm.service.IReportsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-17
 */
@RestController
@RequestMapping("/reports")
@Api(tags = "打印报表管理")
public class ReportsController {

    @Autowired
    private IReportsService reportsService;

    @ApiOperation(value = "新增或修改打印报表")
    @PostMapping(value = "/getReportsById")
    public CommonResult<ReportsVO> getReportsById(@RequestBody QueryCommonForm form) {
        Reports reports = reportsService.getById(form.getId());
        ReportsVO reportsVO = ConvertUtil.convert(reports, ReportsVO.class);
        return CommonResult.success(reportsVO);
    }

    @ApiOperation(value = "新增或修改打印报表")
    @PostMapping(value = "/saveOrUpdateReports")
    public CommonResult saveOrUpdateReports(@RequestBody AddReportsForm form) {
        boolean result = reportsService.saveOrUpdateReports(form);
        if(!result){
            return CommonResult.error(444,"付款失败");
        }
        return CommonResult.success();
    }

}

