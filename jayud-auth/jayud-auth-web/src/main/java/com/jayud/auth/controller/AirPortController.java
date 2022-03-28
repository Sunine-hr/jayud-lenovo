package com.jayud.auth.controller;

import com.jayud.auth.model.po.SeaPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.IAirPortService;
import com.jayud.auth.model.po.AirPort;

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
 * 空运港口表 控制类
 *
 * @author jayud
 * @since 2022-03-24
 */
@Slf4j
@Api(tags = "空运港口表")
@RestController
@RequestMapping("/airPort")
public class AirPortController {



    @Autowired
    public IAirPortService airPortService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-24
     * @param: airPort
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.AirPort>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<AirPort>> selectPage(AirPort airPort,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(airPortService.selectPage(airPort,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-24
    * @param: airPort
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.AirPort>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<AirPort>> selectList(AirPort airPort,
                                                HttpServletRequest req) {
        return BaseResult.ok(airPortService.selectList(airPort));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-24
    * @param: airPort
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody AirPort airPort ){
        //判断代码是否存在，判断名称是否存在
        AirPort airPort1 = airPortService.isCodeExistence(airPort.getCode());
        AirPort airPort2 = airPortService.isNameExistence(airPort.getName());
        if(airPort.getId() != null){

            if(airPort1 != null && !airPort1.getId().equals(airPort.getId())){
                return BaseResult.error(444,"港口代码已存在");
            }
            if(airPort2 != null && !airPort2.getId().equals(airPort.getId())){
                return BaseResult.error(444,"港口名称已存在");
            }
        }else{
            if(airPort1 != null){
                return BaseResult.error(444,"港口代码已存在");
            }
            if(airPort1 != null){
                return BaseResult.error(444,"港口名称已存在");
            }
        }
        airPortService.saveAirPort(airPort);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-24
     * @param: airPort
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody AirPort airPort ){
        airPortService.updateById(airPort);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-24
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        airPortService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-24
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        airPortService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-24
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.AirPort>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<AirPort> queryById(@RequestParam(name="id",required=true) int id) {
        AirPort airPort = airPortService.getById(id);
        return BaseResult.ok(airPort);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-24
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出空运港口表")
    @PostMapping(path = "/exportAirPort")
    public void exportAirPort(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "",
                "",
                "创建时间",
                "创建人",
                "状态(0无效 1有效)",
                "国家",
                "航线",
                "港口中文名"
            );
            List<LinkedHashMap<String, Object>> dataList = airPortService.queryAirPortForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "空运港口表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
