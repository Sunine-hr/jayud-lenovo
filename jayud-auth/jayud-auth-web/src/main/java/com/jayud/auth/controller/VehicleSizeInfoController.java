package com.jayud.auth.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.IVehicleSizeInfoService;
import com.jayud.auth.model.po.VehicleSizeInfo;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 车辆尺寸信息 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "车辆尺寸信息")
@RestController
@RequestMapping("/vehicleSizeInfo")
public class VehicleSizeInfoController {



    @Autowired
    public IVehicleSizeInfoService vehicleSizeInfoService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: vehicleSizeInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.VehicleSizeInfo>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<VehicleSizeInfo>> selectPage(VehicleSizeInfo vehicleSizeInfo,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(vehicleSizeInfoService.selectPage(vehicleSizeInfo,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: vehicleSizeInfo
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.VehicleSizeInfo>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<VehicleSizeInfo>> selectList(VehicleSizeInfo vehicleSizeInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(vehicleSizeInfoService.selectList(vehicleSizeInfo));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: vehicleSizeInfo
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody VehicleSizeInfo vehicleSizeInfo ){
        vehicleSizeInfoService.save(vehicleSizeInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: vehicleSizeInfo
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody VehicleSizeInfo vehicleSizeInfo ){
        vehicleSizeInfoService.updateById(vehicleSizeInfo);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        vehicleSizeInfoService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        vehicleSizeInfoService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.VehicleSizeInfo>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<VehicleSizeInfo> queryById(@RequestParam(name="id",required=true) int id) {
        VehicleSizeInfo vehicleSizeInfo = vehicleSizeInfoService.getById(id);
        return BaseResult.ok(vehicleSizeInfo);
    }

    @ApiOperation(value = "下拉所有车型")
    @PostMapping(value = "/api/initVehicleSize")
    public CommonResult<List<InitComboxStrVO>> initVehicleSize() {
        //获取下拉车型
        List<VehicleSizeInfo> vehicleSizeInfoVOS = vehicleSizeInfoService.findVehicleSize();
        List<com.jayud.common.entity.InitComboxStrVO> cabinetSizes = new ArrayList<>();
        for (VehicleSizeInfo obj : vehicleSizeInfoVOS) {
            com.jayud.common.entity.InitComboxStrVO initComboxVO = new com.jayud.common.entity.InitComboxStrVO();
            initComboxVO.setId(obj.getId());
            initComboxVO.setName(obj.getVehicleSize());
            cabinetSizes.add(initComboxVO);
        }
        return CommonResult.success(cabinetSizes);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-11
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出车辆尺寸信息")
    @PostMapping(path = "/exportVehicleSizeInfo")
    public void exportVehicleSizeInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "车型尺寸ID",
                "尺寸",
                "车型(2-柜车 1-吨车)",
                "创建人",
                "创建时间"
            );
            List<LinkedHashMap<String, Object>> dataList = vehicleSizeInfoService.queryVehicleSizeInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "车辆尺寸信息", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
