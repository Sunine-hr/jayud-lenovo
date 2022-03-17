package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsWaveToOutboundInfo;
import com.jayud.wms.service.IWmsWaveToOutboundInfoService;
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
 * 波次-出库单关系 控制类
 *
 * @author jyd
 * @since 2021-12-27
 */
@Api(tags = "波次-出库单关系")
@RestController
@RequestMapping("/wmsWaveToOutboundInfo")
public class WmsWaveToOutboundInfoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsWaveToOutboundInfoService wmsWaveToOutboundInfoService;

    /**
     * 分页查询数据
     *
     * @param wmsWaveToOutboundInfo   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsWaveToOutboundInfo>>> selectPage(WmsWaveToOutboundInfo wmsWaveToOutboundInfo,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsWaveToOutboundInfoService.selectPage(wmsWaveToOutboundInfo,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsWaveToOutboundInfo   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsWaveToOutboundInfo>> selectList(WmsWaveToOutboundInfo wmsWaveToOutboundInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsWaveToOutboundInfoService.selectList(wmsWaveToOutboundInfo));
    }

    /**
    * 新增
    * @param wmsWaveToOutboundInfo
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsWaveToOutboundInfo wmsWaveToOutboundInfo ){
        wmsWaveToOutboundInfoService.save(wmsWaveToOutboundInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsWaveToOutboundInfo
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsWaveToOutboundInfo wmsWaveToOutboundInfo ){
        wmsWaveToOutboundInfoService.updateById(wmsWaveToOutboundInfo);
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
        wmsWaveToOutboundInfoService.phyDelById(id);
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
        wmsWaveToOutboundInfoService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsWaveToOutboundInfo> queryById(@RequestParam(name="id",required=true) int id) {
        WmsWaveToOutboundInfo wmsWaveToOutboundInfo = wmsWaveToOutboundInfoService.getById(id);
        return BaseResult.ok(wmsWaveToOutboundInfo);
    }

    /**
     * 根据查询条件导出波次-出库单关系
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出波次-出库单关系")
    @PostMapping(path = "/exportWmsWaveToOutboundInfo")
    public void exportWmsWaveToOutboundInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库单号",
                "波次号",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsWaveToOutboundInfoService.queryWmsWaveToOutboundInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "波次-出库单关系", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
