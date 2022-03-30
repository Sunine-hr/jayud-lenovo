package com.jayud.auth.controller;

import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.SysTenantForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysTenantService;
import com.jayud.auth.model.po.SysTenant;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 多租户信息表 控制类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Api(tags = "多租户信息表")
@RestController
@RequestMapping("/sysTenant")
public class SysTenantController {



    @Autowired
    public ISysTenantService sysTenantService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: sysTenant
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysTenant>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysTenant>> selectPage(SysTenant sysTenant,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(sysTenantService.selectPage(sysTenant,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-02-22
    * @param: sysTenant
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysTenant>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysTenant>> selectList(SysTenant sysTenant,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysTenantService.selectList(sysTenant));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-02-22
    * @param: sysTenantForm
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysTenantForm sysTenantForm ){
        return sysTenantService.saveTenant(sysTenantForm);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-02-22
     * @param: sysTenantForm
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysTenantForm sysTenantForm ){
        return sysTenantService.saveTenant(sysTenantForm);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysTenantService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        sysTenantService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysTenant>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysTenantForm> queryById(@RequestParam(name="id",required=true) Long id) {
        SysTenantForm sysTenantForm = sysTenantService.selectByTenantId(id);
        return BaseResult.ok(sysTenantForm);
    }

    /**
     * @description 初始化租户数据
     * @author  ciro
     * @date   2022/2/24 9:02
     * @param: tenantId
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult
     **/
    @GetMapping(value = "/initTenantData")
    public BaseResult initTenantData(Long tenantId,String tenantCode){
        SysTenantForm sysTenantForm = new SysTenantForm();
        sysTenantForm.setId(tenantId);
        sysTenantForm.setCode(tenantCode);
        sysTenantService.initCreateTenant(sysTenantForm);
        return BaseResult.ok();
    }

    /**
     * @description 根据租户编码查询
     * @author  ciro
     * @date   2022/2/25 10:52
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysTenant>
     **/
    @ApiOperation("根据租户编码查询")
    @ApiImplicitParam(name = "tenantCode",value = "租户编码",dataType = "String",required = true)
    @GetMapping(value = "/queryByTenantCode")
    public BaseResult<SysTenant> queryByTenantCode(@RequestParam(name="tenantCode",required=true) String tenantCode) {
        SysTenant sysTenant = sysTenantService.selectByTenantCode(tenantCode);
        return BaseResult.ok(sysTenant);
    }

    /**
     * @description 更新
     * @author  ciro
     * @date   2022/2/25 10:52
     * @param: sysTenant
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("更新")
    @PostMapping("/update")
    public BaseResult update(@Valid @RequestBody SysTenant sysTenant ){
        sysTenantService.updateById(sysTenant);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * @description 根据id批量删除
     * @author  ciro
     * @date   2022/2/25 13:49
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("根据id批量删除")
    @PostMapping("/delByIds")
    public BaseResult delByIds (@Valid @RequestBody DeleteForm deleteForm) {
        sysTenantService.delByIds(deleteForm);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


}
