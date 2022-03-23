package com.jayud.auth.controller;

import com.jayud.auth.model.bo.SysLogForm;
import com.jayud.auth.model.vo.SysLogVO;
import com.jayud.auth.model.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysLogService;
import com.jayud.auth.model.po.SysLog;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 系统日志表 控制类
 *
 * @author jayud
 * @since 2022-03-22
 */
@Slf4j
@Api(tags = "系统日志表")
@RestController
@RequestMapping("/sysLog")
public class SysLogController {


    @Autowired
    public ISysLogService sysLogService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-22
     * @param: sysLog
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysLog>>
     **/
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<SysLogVO>> selectPage(@RequestBody SysLogForm sysLogForm, HttpServletRequest req) {
        return BaseResult.ok(sysLogService.selectPage(sysLogForm, sysLogForm.getCurrentPage(), sysLogForm.getPageSize(), req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-22
     * @param: sysLog
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysLog>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysLog>> selectList(SysLog sysLog,
                                               HttpServletRequest req) {
        return BaseResult.ok(sysLogService.selectList(sysLog));
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-03-22
     * @param: sysLog
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysLog sysLog) {
        sysLogService.save(sysLog);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author jayud
     * @date 2022-03-22
     * @param: sysLog
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysLog sysLog) {
        sysLogService.updateById(sysLog);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-03-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id) {
        sysLogService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        sysLogService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-22
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysLog>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysLog> queryById(@RequestParam(name = "id", required = true) int id) {
        SysLog sysLog = sysLogService.getById(id);
        return BaseResult.ok(sysLog);
    }


    /**
     * @description 根据查询条件导出收货单
     * @author jayud
     * @date 2022-03-22
     * @param: response  响应对象
     * @param: queryReceiptForm  参数queryReceiptForm
     * @param: req
     * @return: void
     **/
    @ApiOperation("根据查询条件导出系统日志表")
    @PostMapping(path = "/exportSysLog")
    public void exportSysLog(HttpServletResponse response, @RequestBody SysLogForm sysLogForm ) {
        try {
            List<String> headList = Arrays.asList(
                    "登录时间",
                    "登录账号",
                    "真是姓名",
                    "日志内容",
                    "登录路径",
                    "登录IP"

            );
            List<LinkedHashMap<String, Object>> dataList = sysLogService.querySysLogForExcel(sysLogForm);
            ExcelUtils.exportExcel(headList, dataList, "系统日志表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
