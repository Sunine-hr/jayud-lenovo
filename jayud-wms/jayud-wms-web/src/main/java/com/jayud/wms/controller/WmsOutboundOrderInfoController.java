package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.aop.annotations.SysWarehousePermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.WmsOutboundOrderInfo;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.wms.service.IWmsOutboundOrderInfoService;
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
 * 出库单 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "出库单")
@RestController
@RequestMapping("/wmsOutboundOrderInfo")
public class WmsOutboundOrderInfoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;

    /**
     * 分页查询数据
     *
     * @param wmsOutboundOrderInfoVO   查询条件
     * @return
     */
    @SysWarehousePermission(clazz = WmsOutboundOrderInfoVO.class)
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsOutboundOrderInfoVO>>> selectPage(@RequestBody WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                                                              @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                              HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsOutboundOrderInfoService.selectPage(wmsOutboundOrderInfoVO,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsOutboundOrderInfoVO   查询条件
     * @return
     */
    @SysWarehousePermission(clazz = WmsOutboundOrderInfoVO.class)
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundOrderInfoVO>> selectList(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundOrderInfoService.selectList(wmsOutboundOrderInfoVO));
    }

    /**
    * 新增
    * @param wmsOutboundOrderInfo
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundOrderInfo wmsOutboundOrderInfo ){
        wmsOutboundOrderInfoService.save(wmsOutboundOrderInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsOutboundOrderInfo
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundOrderInfo wmsOutboundOrderInfo ){
        wmsOutboundOrderInfoService.updateById(wmsOutboundOrderInfo);
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
        wmsOutboundOrderInfoService.phyDelById(id);
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
        wmsOutboundOrderInfoService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundOrderInfo> queryById(@RequestParam(name="id",required=true) int id) {
        WmsOutboundOrderInfo wmsOutboundOrderInfo = wmsOutboundOrderInfoService.getById(id);
        return BaseResult.ok(wmsOutboundOrderInfo);
    }

    /**
     * 根据查询条件导出出库单
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出出库单")
    @PostMapping(path = "/exportWmsOutboundOrderInfo")
    public void exportWmsOutboundOrderInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库通知单号",
                "出库单号",
                "波次号",
                "仓库id",
                "仓库编码",
                "仓库名称",
                "货主id",
                "货主编码",
                "货主名称",
                "单据类型(1采购入库单-原材料仓,2生产领料退货单-原材料仓,3半成品入库单-半成品仓,4生产领料退货单-半成品仓,5成品生产入库单-成品仓,6销售退货入库单)",
                "订单来源(1ERP下发,2EMS下发,3手工创建)",
                "外部订单号1",
                "外部订单号2",
                "客户id",
                "客户编码",
                "客户名称",
                "预计出库时间",
                "备用字段1",
                "备用字段2",
                "合计数量",
                "合计重量",
                "合计体积",
                "分配人",
                "分配时间",
                "订单状态(1未分配，2已分配，3缺货中，4已出库)",
                "实际出库时间",
                "物料总数",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundOrderInfoService.queryWmsOutboundOrderInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库单", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * @description 出库通知单转出库单
     * @author  ciro
     * @date   2021/12/23 14:25
     * @param: orderNumber
     * @return: com.jyd.component.commons.result.Result
     **/
    @ApiOperation("出库通知单转出库单")
    @PostMapping(value = "/transferOut")
    public BaseResult transferOut(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO) {
        return  wmsOutboundOrderInfoService.transferOut(outboundOrderNumberVO);
    }


    /**
     * @description 根据通知单编码查询详情
     * @author  ciro
     * @date   2021/12/23 14:25
     * @param: wmsOutboundOrderInfoVO
     * @return: com.jyd.component.commons.result.Result<com.jayud.model.vo.WmsOutboundOrderInfoVO>
     **/
//    @SysDataPermission(clazz = WmsOutboundNoticeOrderInfoVO.class)
    @ApiOperation("根据通知单编码查询详情")
    @GetMapping(value = "/queryByCode")
    public BaseResult<WmsOutboundOrderInfoVO> queryByCode(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO){
        return BaseResult.ok(wmsOutboundOrderInfoService.queryByCode(wmsOutboundOrderInfoVO));
    }


    /**
     * @description 根据条件查找未分配、可生成波次单得出库单
     * @author  ciro
     * @date   2021/12/23 14:26
     * @param: wmsOutboundOrderInfoVO
     * @return: com.jyd.component.commons.result.Result<java.util.List<com.jayud.model.vo.WmsOutboundOrderInfoVO>>
     **/
    @SysDataPermission(clazz = WmsOutboundOrderInfoVO.class)
    @ApiOperation("根据条件查找未分配、可生成波次单得出库单")
    @GetMapping(value = "/selectUnStockToWave")
    public BaseResult<CommonPageResult<IPage<WmsOutboundOrderInfoVO>>> selectUnStockToWave(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                                                    @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        return BaseResult.ok(new CommonPageResult(wmsOutboundOrderInfoService.selectUnStockToWave(wmsOutboundOrderInfoVO,currentPage,pageSize)));
    }

    @SysDataPermission(clazz = WmsOutboundOrderInfoVO.class)
    @ApiOperation("根据条件查找未分配、可生成波次单得出库单列表")
    @GetMapping(value = "/selectUnStockToWaveList")
    public BaseResult<List<WmsOutboundOrderInfoVO>> selectUnStockToWaveList(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO){
        return BaseResult.ok(wmsOutboundOrderInfoService.selectUnStockToWaveList(wmsOutboundOrderInfoVO));
    }




    /**
     * @description 库存分配
     * @author  ciro
     * @date   2021/12/23 14:26
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    @ApiOperation("库存分配")
    @PostMapping(value = "/allocateInventory")
    public BaseResult allocateInventory(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO) {
        return wmsOutboundOrderInfoService.allocateInventory(outboundOrderNumberVO);
    }

    /**
     * @description 撤销库存分配
     * @author  ciro
     * @date   2021/12/25 9:47
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    @ApiOperation("撤销库存分配")
    @PostMapping(value = "/cancelAllocateInventory")
    public BaseResult cancelAllocateInventory(@RequestBody OutboundOrderNumberVO outboundOrderNumberVO) {
        return wmsOutboundOrderInfoService.cancelAllocateInventory(outboundOrderNumberVO);
    }



    @ApiOperation("根据集合删除")
    @ApiImplicitParam(name = "delList",value = "删除集合",dataType = "List<WmsOutboundOrderInfoVO>",required = true)
    @PostMapping("/dels")
    public BaseResult dels(@RequestBody List<WmsOutboundOrderInfoVO> delList) {
        return wmsOutboundOrderInfoService.delByIdAndOrderNumber(delList);
    }

    @ApiOperation("保存数据")
    @PostMapping("saveInfo")
    public BaseResult saveInfo(@RequestBody  WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO){
        return wmsOutboundOrderInfoService.saveInfo(wmsOutboundOrderInfoVO);
    }

    @ApiOperation("订单作废")
    @PostMapping("cancelOrder")
    public BaseResult cancelOrder(@RequestBody DeleteForm deleteForm){
        return wmsOutboundOrderInfoService.cancelOrder(deleteForm);
    }

    @ApiOperation("转发运复核")
    @PostMapping("changeToReview")
    public BaseResult changeToReview(@RequestBody DeleteForm deleteForm){
        return wmsOutboundOrderInfoService.changeToReview(deleteForm);
    }



}
