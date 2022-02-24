package com.jayud.auth.controller;

import com.jayud.auth.model.po.SysEnterpriseWechatConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysSmsConfigService;
import com.jayud.auth.model.po.SysSmsConfig;

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
 * 系统短信配置表 控制类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Slf4j
@Api(tags = "系统短信配置表")
@RestController
@RequestMapping("/sysSmsConfig")
public class SysSmsConfigController {



    @Autowired
    public ISysSmsConfigService sysSmsConfigService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-24
     * @param: sysSmsConfig
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysSmsConfig>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysSmsConfig>> selectPage(SysSmsConfig sysSmsConfig,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(sysSmsConfigService.selectPage(sysSmsConfig,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-02-24
    * @param: sysSmsConfig
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysSmsConfig>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysSmsConfig>> selectList(SysSmsConfig sysSmsConfig,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysSmsConfigService.selectList(sysSmsConfig));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-02-24
    * @param: sysSmsConfig
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysSmsConfig sysSmsConfig ){
        sysSmsConfigService.save(sysSmsConfig);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-02-24
     * @param: sysSmsConfig
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysSmsConfig sysSmsConfig ){
        sysSmsConfigService.updateById(sysSmsConfig);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysSmsConfigService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        sysSmsConfigService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysSmsConfig>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysSmsConfig> queryById(@RequestParam(name="id",required=true) int id) {
        SysSmsConfig sysSmsConfig = sysSmsConfigService.getById(id);
        return BaseResult.ok(sysSmsConfig);
    }

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2022/2/24 13:02
     * @param: sysEmamilConfig
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("保存数据")
    @PostMapping("/saveConfig")
    public BaseResult saveConfig(@Valid @RequestBody SysSmsConfig sysSmsConfig ){
        return sysSmsConfigService.saveConfig(sysSmsConfig);
    }


    /**
     * @description 根据租户编码查询
     * @author  ciro
     * @date   2022/2/24 13:02
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysSmsConfig>
     **/
    @ApiOperation("根据租户编码查询")
    @ApiImplicitParam(name = "tenantCode",value = "租户编码",dataType = "String",required = true)
    @GetMapping(value = "/selectByTenantCode")
    public BaseResult<SysSmsConfig> selectByTenantCode(@RequestParam(name="tenantCode",required=true) String tenantCode) {
        return sysSmsConfigService.selectByTenantCode(tenantCode);
    }


}
