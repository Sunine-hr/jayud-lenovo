package com.jayud.wms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.SeedingWall;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.ISeedingWallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 播种墙信息 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "播种墙信息")
@RestController
@RequestMapping("/seedingWall")
public class SeedingWallController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ISeedingWallService seedingWallService;

    @Autowired
    private AuthService authService;


    /**
     * 分页查询数据
     *
     * @param seedingWall   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SeedingWall>> selectPage(SeedingWall seedingWall,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        return BaseResult.ok(seedingWallService.selectPage(seedingWall,pageNo,pageSize,req));
    }

    /**
     * 列表查询数据
     *
     * @param seedingWall   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SeedingWall>> selectList(SeedingWall seedingWall,
                                                HttpServletRequest req) {
        return BaseResult.ok(seedingWallService.selectList(seedingWall));
    }

    /**
    * 新增
    * @param seedingWall
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody SeedingWall seedingWall ){
        //seedingWallService.save(seedingWall);
        SeedingWall seedingWall1 = seedingWallService.saveOrUpdateSeedingWall(seedingWall);
        return BaseResult.ok(seedingWall1);
    }

//    /**
//     * 编辑
//     * @param seedingWall
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody SeedingWall seedingWall ){
//        seedingWallService.updateById(seedingWall);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }

    /**
     * 删除
     * @param id
     **/
    @ApiOperation("删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @PostMapping("/del")
    public BaseResult del(@RequestParam(name="id", required=true) int id){
        //seedingWallService.removeById(id);
        seedingWallService.delSeedingWall(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<SeedingWall> queryById(@RequestParam(name="id",required=true) int id) {
        SeedingWall seedingWall = seedingWallService.getById(id);
        return BaseResult.ok(seedingWall);
    }

    /**
     * 根据查询条件导出播种墙
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出播种墙")
    @PostMapping(path = "/exportSeedingWall")
    public void exportSeedingWall(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList("主键", "播种墙编号", "播种墙名称", "分播类型", "状态", "备注信息");
            List<LinkedHashMap<String, Object>> dataList = seedingWallService.querySeedingWallForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "播种墙", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 播种墙名称、分播类型 的下拉框数据
     *
     * @return
     */
    @ApiOperation("播种墙名称、分播类型 的下拉框数据")
    @GetMapping(path = "/querySeedingWallAndSeedingType")
    public BaseResult<Object> queryWorkbenchType() {
        Map<String, Object> data = MapUtil.newHashMap();
        List<LinkedHashMap<String, Object>> seedingWallData = authService.queryDictByDictType("seeding_wall");//播种墙名称
        List<LinkedHashMap<String, Object>> seedingTypeData = authService.queryDictByDictType("seeding_type");//分播类型
        seedingTypeData.forEach(map -> {
            //字符串转数字
            Long value = MapUtil.getLong(map, "value");
            map.put("value", value);
        });
        data.put("seedingWallData",seedingWallData);
        data.put("seedingTypeData",seedingTypeData);
        return BaseResult.ok(data);
    }

}
