package com.jayud.auth.controller;

import com.jayud.common.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysUrlService;
import com.jayud.auth.model.po.SysUrl;

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
 * 系统链接表 控制类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Api(tags = "系统链接表")
@RestController
@RequestMapping("/sysUrl")
public class SysUrlController {



    @Autowired
    public ISysUrlService sysUrlService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUrl
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUrl>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysUrl>> selectPage(SysUrl sysUrl,
                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysUrlService.selectPage(sysUrl,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-02-21
    * @param: sysUrl
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysUrl>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysUrl>> selectList(SysUrl sysUrl,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysUrlService.selectList(sysUrl));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-02-21
    * @param: sysUrl
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SysUrl sysUrl ){
        return sysUrlService.saveUrl(sysUrl);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUrl
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SysUrl sysUrl ){
        return sysUrlService.saveUrl(sysUrl);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        sysUrlService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        sysUrlService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUrl>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SysUrl> queryById(@RequestParam(name="id",required=true) int id) {
        SysUrl sysUrl = sysUrlService.getById(id);
        return BaseResult.ok(sysUrl);
    }

    /**
     * @description 根据租户获取系统列表
     * @author  ciro
     * @date   2022/2/24 9:46
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult
     **/
    @GetMapping(value = "getSystemByTenantCode")
    public BaseResult<List<SysUrl>> getSystemByTenantCode(String tenantCode){
        return BaseResult.ok(sysUrlService.getSystemByTenantCode(tenantCode));
    }


}
