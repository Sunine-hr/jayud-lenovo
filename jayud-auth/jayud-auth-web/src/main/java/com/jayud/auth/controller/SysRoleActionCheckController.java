package com.jayud.auth.controller;

import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.bo.SysRoleActionCheckForm;
import com.jayud.auth.model.vo.SysRoleActionCheckVO;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysRoleActionCheckService;
import com.jayud.auth.model.po.SysRoleActionCheck;

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
 * 角色审核级别权限 控制类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Api(tags = "角色审核级别权限")
@RestController
@RequestMapping("/sysRoleActionCheck")
public class SysRoleActionCheckController {



    @Autowired
    public ISysRoleActionCheckService sysRoleActionCheckService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: sysRoleActionCheck
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysRoleActionCheck>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysRoleActionCheckVO>> selectPage(QueryForm form,
                                                              @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                              HttpServletRequest req) {
        if (CurrentUserUtil.hasRole(CommonConstant.SUPER_TENANT)) {
            form.setTenantCode(null);
        } else {
            form.setTenantCode(CurrentUserUtil.getUserTenantCode());
        }
        return BaseResult.ok(sysRoleActionCheckService.selectPage(form,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-01
    * @param: sysRoleActionCheck
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysRoleActionCheck>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysRoleActionCheck>> selectList(QueryForm form,
                                                           HttpServletRequest req) {
        if (CurrentUserUtil.hasRole(CommonConstant.SUPER_TENANT)) {
            form.setTenantCode(null);
        } else {
            form.setTenantCode(CurrentUserUtil.getUserTenantCode());
        }
        return BaseResult.ok(sysRoleActionCheckService.selectList(form));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-01
    * @param: sysRoleActionCheck
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysRoleActionCheckForm sysRoleActionCheck ){
        sysRoleActionCheckService.saveSysRoleActionCheck(sysRoleActionCheck);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-01
     * @param: sysRoleActionCheck
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysRoleActionCheck sysRoleActionCheck ){
        sysRoleActionCheckService.updateById(sysRoleActionCheck);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysRoleActionCheckService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm form){
        sysRoleActionCheckService.logicDel(form.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysRoleActionCheck>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysRoleActionCheck> queryById(@RequestParam(name="id",required=true) int id) {
        SysRoleActionCheck sysRoleActionCheck = sysRoleActionCheckService.getById(id);
        return BaseResult.ok(sysRoleActionCheck);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-01
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出角色审核级别权限")
    @PostMapping(path = "/exportSysRoleActionCheck")
    public void exportSysRoleActionCheck(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "角色ID",
                "权限ID",
                "审核CODE",
                "审核级别",
                "权限最高金额,0不限制",
                "备注",
                "创建人ID",
                "创建人名称",
                "创建时间",
                "最后修改人ID",
                "最后修改人名称",
                "最后修改时间",
                "删除标记"
            );
            List<LinkedHashMap<String, Object>> dataList = sysRoleActionCheckService.querySysRoleActionCheckForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "角色审核级别权限", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
