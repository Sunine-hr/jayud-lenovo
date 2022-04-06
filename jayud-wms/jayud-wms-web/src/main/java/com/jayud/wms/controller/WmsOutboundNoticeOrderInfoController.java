package com.jayud.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfo;
import com.jayud.wms.model.vo.WmsOutboundNoticeDictVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.wms.service.IWmsOutboundNoticeOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 出库通知单 控制类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Api(tags = "出库通知单")
@RestController
@RequestMapping("/wmsOutboundNoticeOrderInfo")
public class WmsOutboundNoticeOrderInfoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsOutboundNoticeOrderInfoService wmsOutboundNoticeOrderInfoService;


    /**
     * @description 分页查询数据
     * @author  jyd
     * @date   2022/1/26 10:55
     * @param: wmsOutboundNoticeOrderInfoVO
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jyd.component.commons.result.Result<com.jyd.component.commons.utils.CommonPageResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.model.vo.WmsOutboundNoticeOrderInfoVO>>>
     **/
//    @SysDataPermission(clazz = WmsOutboundNoticeOrderInfoVO.class)
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<CommonPageResult<WmsOutboundNoticeOrderInfoVO>> selectPage(@RequestBody WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO,
                                                                                    @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        IPage<WmsOutboundNoticeOrderInfoVO> iPage = wmsOutboundNoticeOrderInfoService.selectPage(wmsOutboundNoticeOrderInfoVO,currentPage,pageSize,req);
        CommonPageResult<WmsOutboundNoticeOrderInfoVO> result= new CommonPageResult<WmsOutboundNoticeOrderInfoVO>(iPage);
        return BaseResult.ok(result);
    }

    /**
     * 列表查询数据
     *
     * @param wmsOutboundNoticeOrderInfoVO   查询条件
     * @return
     */
    @SysDataPermission
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsOutboundNoticeOrderInfoVO>> selectList(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO,
                                                                HttpServletRequest req) {
        return BaseResult.ok(wmsOutboundNoticeOrderInfoService.selectList(wmsOutboundNoticeOrderInfoVO));
    }

    /**
    * 新增
    * @param wmsOutboundNoticeOrderInfo
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsOutboundNoticeOrderInfo wmsOutboundNoticeOrderInfo ){
        wmsOutboundNoticeOrderInfoService.save(wmsOutboundNoticeOrderInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsOutboundNoticeOrderInfo
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsOutboundNoticeOrderInfo wmsOutboundNoticeOrderInfo ){
        wmsOutboundNoticeOrderInfoService.updateById(wmsOutboundNoticeOrderInfo);
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
        wmsOutboundNoticeOrderInfoService.phyDelById(id);
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
        wmsOutboundNoticeOrderInfoService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param wmsOutboundNoticeOrderInfoVO
    */
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult<WmsOutboundNoticeOrderInfoVO> queryById(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO) {
        WmsOutboundNoticeOrderInfoVO vo = new WmsOutboundNoticeOrderInfoVO();
        List<WmsOutboundNoticeOrderInfoVO> list = wmsOutboundNoticeOrderInfoService.selectList(wmsOutboundNoticeOrderInfoVO);
        if (list.isEmpty()){
            vo = list.get(0);
        }
        return BaseResult.ok(vo);
    }

    /**
     * 根据查询条件导出出库通知单
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出出库通知单")
    @PostMapping(path = "/exportWmsOutboundNoticeOrderInfo")
    public void exportWmsOutboundNoticeOrderInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "出库通知单号",
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
                "确认人",
                "确认时间",
                "订单状态(1创建，2上游取消，3转出库)",
                "实际出库时间",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsOutboundNoticeOrderInfoService.queryWmsOutboundNoticeOrderInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "出库通知单", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }


//    @SysDataPermission(clazz = WmsOutboundNoticeOrderInfoVO.class)
    @ApiOperation("根据通知单编码查询详情")
    @GetMapping(value = "/queryByCode")
    public BaseResult<WmsOutboundNoticeOrderInfoVO> queryByCode(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO){
        return BaseResult.ok(wmsOutboundNoticeOrderInfoService.queryByCode(wmsOutboundNoticeOrderInfoVO));
    }

    @ApiOperation("保存数据")
    @PostMapping("/saveInfo")
    public BaseResult saveInfo(@Valid @RequestBody WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO ){
        return wmsOutboundNoticeOrderInfoService.saveInfo(wmsOutboundNoticeOrderInfoVO);
    }

    @ApiOperation("根据id删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "主键id",dataType = "String",required = true),
            @ApiImplicitParam(name = "orderNumber",value = "出库通知单编码",dataType = "String",required = true)})
    @DeleteMapping("/delById")
    public BaseResult delById(String id,String orderNumber){
        wmsOutboundNoticeOrderInfoService.delById(Long.parseLong(id),orderNumber);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * @description 根据集合删除
     * @author  ciro
     * @date   2021/12/28 10:24
     * @param: delList
     * @return: com.jyd.component.commons.result.Result
     **/
    @ApiOperation("根据集合删除")
    @ApiImplicitParam(name = "delList",value = "删除集合",dataType = "List<WmsOutboundNoticeOrderInfoVO>",required = true)
    @PostMapping("/dels")
    public BaseResult dels(@RequestBody List<WmsOutboundNoticeOrderInfoVO> delList){
        delList.forEach(x->{
            wmsOutboundNoticeOrderInfoService.delById(x.getId(),x.getOrderNumber());
        });

        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    @ApiOperation("获取字典数据")
    @PostMapping("/getDictmsg")
    public BaseResult<WmsOutboundNoticeDictVO> getDictmsg(){
        return BaseResult.ok(wmsOutboundNoticeOrderInfoService.getDictmsg());
    }

}
