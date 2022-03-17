package com.jayud.wms.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.AddWmsOutboundSeedingForm;
import com.jayud.wms.model.po.WmsOutboundSeeding;
import com.jayud.wms.service.IWmsOutboundSeedingService;
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
 * 出库播种 控制类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Api(tags = "出库播种")
@RestController
@RequestMapping("/wmsOutboundSeeding")
public class WmsOutboundSeedingController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsOutboundSeedingService wmsOutboundSeedingService;

    /**
     * 分页查询数据
     *
     * @param wmsOutboundSeeding   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsOutboundSeeding>>> selectPage(WmsOutboundSeeding wmsOutboundSeeding,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsOutboundSeedingService.selectPage(wmsOutboundSeeding,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsOutboundSeeding   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundSeeding>> selectList(WmsOutboundSeeding wmsOutboundSeeding,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundSeedingService.selectList(wmsOutboundSeeding));
    }

    /**
    * 新增
    * @param wmsOutboundSeeding
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundSeeding wmsOutboundSeeding ){
        wmsOutboundSeedingService.save(wmsOutboundSeeding);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsOutboundSeeding
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundSeeding wmsOutboundSeeding ){
        wmsOutboundSeedingService.updateById(wmsOutboundSeeding);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * 物理删除
     * @param id
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam int id){
        wmsOutboundSeedingService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 逻辑删除
    * @param id
    **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam int id){
        wmsOutboundSeedingService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundSeeding> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundSeeding wmsOutboundSeeding = wmsOutboundSeedingService.getById(id);
        return BaseResult.ok(wmsOutboundSeeding);
    }

    /**
     * 根据查询条件导出出库播种
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出出库播种")
    @PostMapping(path = "/exportWmsOutboundSeeding")
    public void exportWmsOutboundSeeding(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "播种位id",
                "播种位编码",
                "播种位名称",
                "物料id",
                "物料编码",
                "物料名称",
                "旧-容器id",
                "旧-容器编码",
                "旧-容器名称",
                "旧-数量",
                "旧-播种数量",
                "旧-播种后数量",
                "旧-单位",
                "新-容器id",
                "新-容器编码",
                "新-容器名称",
                "新-数量",
                "新-播种数量",
                "新-播种后数量",
                "新-单位",
                "波次单id",
                "波次号",
                "出库单id",
                "出库单号",
                "拣货下架单id",
                "拣货下架单号",
                "出库通知单id",
                "出库通知单号",
                "分配物料明细id",
                "状态(0-未确认，1-已确认)",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundSeedingService.queryWmsOutboundSeedingForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库播种", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 确认更换
     *
     * @param form 参数form
     */
    @ApiOperation("出库播种-确认更换")
    @PostMapping(path = "/confirmReplacement")
    public BaseResult confirmReplacement(@RequestBody List<AddWmsOutboundSeedingForm> form) {
        if(CollectionUtil.isEmpty(form)){
            return BaseResult.error("任务信息不为空");
        }
        boolean result = this.wmsOutboundSeedingService.saveOrUpdateWmsOutboundSeeding(form);
        if(!result){
            return BaseResult.error("出库播种确认更换失败");
        }
        return BaseResult.ok();
    }
}
