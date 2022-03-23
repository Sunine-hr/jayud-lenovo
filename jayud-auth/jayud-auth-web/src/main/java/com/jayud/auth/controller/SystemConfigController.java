package com.jayud.auth.controller;

import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISystemConfigService;
import com.jayud.auth.model.po.SystemConfig;

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
 * 系统配置表 控制类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Api(tags = "系统配置表")
@RestController
@RequestMapping("/systemConfig")
public class SystemConfigController {



    @Autowired
    public ISystemConfigService systemConfigService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: systemConfig
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SystemConfig>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<SystemConfig>> selectPage(SystemConfig systemConfig,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(systemConfigService.selectPage(systemConfig,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-23
    * @param: systemConfig
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SystemConfig>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SystemConfig>> selectList(SystemConfig systemConfig,
                                                HttpServletRequest req) {
        return BaseResult.ok(systemConfigService.selectList(systemConfig));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-23
    * @param: systemConfig
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SystemConfig systemConfig ){
        if(StringUtils.isEmpty(systemConfig.getConfigCode())){
            return BaseResult.error(444,"系统配置code不为空");
        }
        boolean result = systemConfigService.saveOrUpdateSystemConfig(systemConfig);
        if(result){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.error(SysTips.ADD_FAIL);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-23
     * @param: systemConfig
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody SystemConfig systemConfig ){
        systemConfigService.updateById(systemConfig);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        systemConfigService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        systemConfigService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SystemConfig>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SystemConfig> queryById(@RequestParam(name="id",required=true) int id) {
        SystemConfig systemConfig = systemConfigService.getById(id);
        return BaseResult.ok(systemConfig);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-23
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出系统配置表")
    @PostMapping(path = "/exportSystemConfig")
    public void exportSystemConfig(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "系统配置code",
                "字段1",
                "字段2",
                "字段3",
                "字段4",
                "字段5",
                "字段6",
                "字段7",
                "字段8",
                "字段9",
                "字段10",
                "是否同步在线",
                "备注",
                "组织机构ID",
                "多租户ID",
                "创建人",
                "创建人名称",
                "创建时间",
                "最后修改人",
                "最后修改人名称",
                "最后修改时间",
                "删除标志",
                "删除人",
                "删除时间"
            );
            List<LinkedHashMap<String, Object>> dataList = systemConfigService.querySystemConfigForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "系统配置表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
