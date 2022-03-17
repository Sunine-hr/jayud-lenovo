package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsWaveOrderInfo;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsWaveInfoVO;
import com.jayud.wms.service.IWmsWaveOrderInfoService;
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
 * 波次单 控制类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Api(tags = "波次单")
@RestController
@RequestMapping("/wmsWaveOrderInfo")
public class WmsWaveOrderInfoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsWaveOrderInfoService wmsWaveOrderInfoService;

    /**
     * 分页查询数据
     *
     * @param wmsWaveOrderInfo   查询条件
     * @return
     */
    @SysDataPermission(clazz = WmsWaveOrderInfo.class)
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsWaveOrderInfo>>> selectPage(WmsWaveOrderInfo wmsWaveOrderInfo,
                                                                @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsWaveOrderInfoService.selectPage(wmsWaveOrderInfo,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsWaveOrderInfo   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsWaveOrderInfo>> selectList(WmsWaveOrderInfo wmsWaveOrderInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsWaveOrderInfoService.selectList(wmsWaveOrderInfo));
    }

    /**
    * 新增
    * @param wmsWaveOrderInfo
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsWaveOrderInfo wmsWaveOrderInfo ){
        wmsWaveOrderInfoService.save(wmsWaveOrderInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsWaveOrderInfo
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsWaveOrderInfo wmsWaveOrderInfo ){
        wmsWaveOrderInfoService.updateById(wmsWaveOrderInfo);
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
        wmsWaveOrderInfoService.phyDelById(id);
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
        wmsWaveOrderInfoService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据波次号查询
    * @param waveNumber
    */
    @ApiOperation("根据波次号查询")
    @ApiImplicitParam(name = "waveNumber",value = "波次号",dataType = "String",required = true)
    @GetMapping(value = "/queryByWaveOrderNumber")
    public BaseResult<WmsWaveOrderInfo> queryByWaveOrderNumber(@RequestParam(name="waveNumber",required=true) String waveNumber) {
        WmsWaveOrderInfo wmsWaveOrderInfo = wmsWaveOrderInfoService.queryByWaveOrderNumber(waveNumber);
        return BaseResult.ok(wmsWaveOrderInfo);
    }

    /**
     * 根据查询条件导出波次单
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出波次单")
    @PostMapping(path = "/exportWmsWaveOrderInfo")
    public void exportWmsWaveOrderInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "波次号",
                "仓库id",
                "仓库编码",
                "仓库名称",
                "货主id",
                "货主编码",
                "货主名称",
                "状态(1 新建,2 处理中,3 已分配,4 缺货中,5 已撤销,6已出库)",
                "订单数",
                "完成订单数",
                "分配量",
                "拣货量",
                "订单物料总量",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsWaveOrderInfoService.queryWmsWaveOrderInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "波次单", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    @ApiOperation("创建波次单")
    @PostMapping(value = "/createWave")
    public BaseResult createWave(@RequestBody WmsWaveInfoVO wmsWaveInfoVO) {
        return wmsWaveOrderInfoService.createWave(wmsWaveInfoVO);
    }

    @ApiOperation("修改波次单和出库单关联")
    @PostMapping(value = "/changeOrder")
    public BaseResult changeOrder(@RequestBody WmsWaveInfoVO wmsWaveInfoVO) {
        wmsWaveInfoVO.setIsDel(false);
        return wmsWaveOrderInfoService.changeOrder(wmsWaveInfoVO);
    }

    @ApiOperation("删除波次")
    @PostMapping(value = "/delWave")
    public BaseResult delWave(OutboundOrderNumberVO outboundOrderNumberVO) {
        return wmsWaveOrderInfoService.delWave(outboundOrderNumberVO);
    }



    @ApiOperation("波次库存分配")
    @PostMapping(value = "/allocateWaveInventory")
    public BaseResult allocateWaveInventory(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO){
        return wmsWaveOrderInfoService.allocateWaveInventory(outboundOrderNumberVO);
    }

    @ApiOperation("取消波次分配")
    @PostMapping(value = "/caneclAllocateWaveInventory")
    public BaseResult caneclAllocateWaveInventory(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO){
        return wmsWaveOrderInfoService.caneclAllocateWaveInventory(outboundOrderNumberVO);
    }

    @ApiOperation("增加波次-出库单临时数据")
    @PostMapping(value = "/addOrderRelation")
    public BaseResult addOrderRelation(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO){
        return wmsWaveOrderInfoService.caneclAllocateWaveInventory(outboundOrderNumberVO);
    }




}
