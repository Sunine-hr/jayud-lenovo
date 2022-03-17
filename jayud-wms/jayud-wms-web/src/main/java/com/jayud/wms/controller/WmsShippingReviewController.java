package com.jayud.wms.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.WmsShippingReviewForm;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.wms.model.bo.AddWmsShippingReviewForm;
import com.jayud.wms.model.bo.CloseTheBoxForm;
import com.jayud.wms.model.po.WmsShippingReview;
import com.jayud.wms.model.po.WmsWaveOrderInfo;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsShippingReviewVO;
import com.jayud.wms.service.*;
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
import java.util.*;

/**
 * 发运复核 控制类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Api(tags = "发运复核")
@RestController
@RequestMapping("/wmsShippingReview")
public class WmsShippingReviewController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public IWmsShippingReviewService wmsShippingReviewService;

    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;

    @Autowired
    private IWmsWaveToMaterialService wmsWaveToMaterialService;

    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;

    @Autowired
    private IWmsWaveOrderInfoService wmsWaveOrderInfoService;

    /**
     * 分页查询数据
     *
     * @param wmsShippingReview   查询条件
     * @return
     */
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<CommonPageResult<IPage<WmsShippingReviewVO>>> selectPage(WmsShippingReview wmsShippingReview,
                                                                           @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                           HttpServletRequest req) {
        return BaseResult.ok(new CommonPageResult(wmsShippingReviewService.selectPage(wmsShippingReview,currentPage,pageSize,req)));
    }

    /**
     * 列表查询数据
     *
     * @param wmsShippingReview   查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<WmsShippingReviewVO>> selectList(WmsShippingReview wmsShippingReview,
                                                HttpServletRequest req) {
        return BaseResult.ok(wmsShippingReviewService.selectList(wmsShippingReview));
    }

    /**
    * 新增
    * @param wmsShippingReview
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody WmsShippingReview wmsShippingReview ){
        wmsShippingReviewService.save(wmsShippingReview);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     * @param wmsShippingReview
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody WmsShippingReview wmsShippingReview ){
        wmsShippingReviewService.updateById(wmsShippingReview);
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
        wmsShippingReviewService.phyDelById(id);
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
        wmsShippingReviewService.removeById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
    * 根据id查询
    * @param id
    */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<WmsShippingReview> queryById(@RequestParam(name="id",required=true) int id) {
        WmsShippingReview wmsShippingReview = wmsShippingReviewService.getById(id);
        return BaseResult.ok(wmsShippingReview);
    }

    /**
     * 根据查询条件导出发运复核
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("根据查询条件导出发运复核")
    @PostMapping(path = "/exportWmsShippingReview")
    public void exportWmsShippingReview(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键id",
                "波次单号",
                "出库单号",
                "是否完成(0否，1是)",
                "拣货下架单id",
                "拣货下架单号",
                "仓库id",
                "货主id",
                "物料id",
                "物料编码",
                "物料名称",
                "拣货数量",
                "已扫描数量",
                "未扫描数量",
                "单位",
                "复核人",
                "复核时间",
                "批次号",
                "生产时间",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsShippingReviewService.queryWmsShippingReviewForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "发运复核", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }


    /**
     * 根据查询条件导出发运复核
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @ApiOperation("导出发运复核差异")
    @PostMapping(path = "/exportShipmentReviewVariance")
    public void exportShipmentReviewVariance(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "波次单号",
                    "出库单号",
                    "拣货下架单号",
                    "仓库名称",
                    "货主名称",
                    "物料编码",
                    "物料名称",
                    "拣货数量",
                    "已扫描数量",
                    "未扫描数量",
                    "箱号",
                    "单位",
                    "复核人",
                    "复核时间",
                    "批次号",
                    "生产时间",
                    "备注"
            );
            List<LinkedHashMap<String, Object>> dataList = wmsShippingReviewService.exportShipmentReviewVariance(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "发运复核差异", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 根据出库单号/波次号、物料编号查询扫描信息
     *
     * @param paramMap 参数Map
     */
    @ApiOperation("根据出库单号/波次号、物料编号查询扫描信息")
    @PostMapping(path = "/queryScanInformation")
    public BaseResult<List<QueryScanInformationVO>> queryScanInformation(@RequestBody Map<String, Object> paramMap) {
        String orderNumber = MapUtil.getStr(paramMap, "orderNumber");
        if(StringUtils.isEmpty(orderNumber)){
            return BaseResult.error("查询单号不为空");
        }
        String materialCode = MapUtil.getStr(paramMap, "materialCode");
        List<QueryScanInformationVO> queryScanInformationVOS = new ArrayList<>();
        if(orderNumber.substring(0,2).equals("ON")){
//            WmsOutboundOrderInfo wmsOutboundOrderInfo = wmsOutboundOrderInfoService.getWmsOutboundOrderInfoByOrderNumber(orderNumber);
//            if(wmsOutboundOrderInfo.getOrderStatusType().equals(4)){
//                return BaseResult.error("该订单已经关箱完成");
//            }
            queryScanInformationVOS = wmsShippingReviewService.queryScanInformation(orderNumber,materialCode);
        }else{
            WmsWaveOrderInfo wmsWaveOrderInfo = wmsWaveOrderInfoService.getWmsWaveOrderInfoByWaveNumber(orderNumber);
//            if(wmsWaveOrderInfo.getStatus().equals(6)){
//                return BaseResult.error("该订单已经关箱完成");
//            }
            queryScanInformationVOS = wmsShippingReviewService.queryScanInformation(orderNumber,materialCode);
        }
        return BaseResult.ok(queryScanInformationVOS);
    }

    /**
     * 复核提交
     *
     * @param form 参数form
     */
    @ApiOperation("复核提交")
    @PostMapping(path = "/saveWmsShippingReview")
    public BaseResult saveWmsShippingReview(@RequestBody List<AddWmsShippingReviewForm> form) {
        for (AddWmsShippingReviewForm addWmsShippingReviewForm : form) {
            if(addWmsShippingReviewForm.getIsEnd().equals(2)){
                return BaseResult.error("该订单已关箱，不能进行发运复核操作");
            }
        }

        if(CollectionUtil.isEmpty(form)){
            return BaseResult.error("提交数据不为空");
        }
        boolean result = this.wmsShippingReviewService.saveWmsShippingReview(form);
        if(!result){
            return BaseResult.error("复核提交失败");
        }
        return BaseResult.ok();
    }

    /**
     * 复核提交
     *
     * @param form 参数form
     */
    @ApiOperation("单件复核提交")
    @PostMapping(path = "/singleReviewSubmission")
    public BaseResult singleReviewSubmission(@RequestBody AddWmsShippingReviewForm form) {
        if(form == null){
            return BaseResult.error("复核数据不为空");
        }
        WmsShippingReview wmsShippingReview = this.wmsShippingReviewService.getById(form.getId());
        if(wmsShippingReview.getIsEnd().equals(1)){
            return BaseResult.error("该订单已发运复核完成");
        }
        boolean result = this.wmsShippingReviewService.singleReviewSubmission(form);
        if(!result){
            return BaseResult.error("复核提交失败");
        }
        return BaseResult.ok();
    }

    /**
     * 关箱
     *
     * @param form 参数form
     */
    @ApiOperation("关箱")
    @PostMapping(path = "/CloseTheBox")
    public BaseResult CloseTheBox(@RequestBody CloseTheBoxForm form) {
        if(CollectionUtil.isEmpty(form.getIds())){
            return BaseResult.error("关箱数据不能为空");
        }
        for (Long id : form.getIds()) {
            WmsShippingReview wmsShippingReview = this.wmsShippingReviewService.getById(id);
            if(!wmsShippingReview.getIsEnd().equals(1)){
                return  BaseResult.error("该数据未发运复核完成，无法进行关箱操作");
            }
            if(wmsShippingReview.getNotScannedAccount()>0){
                return  BaseResult.error("发运复核数量不对");
            }
        }
        boolean result = this.wmsShippingReviewService.CloseTheBox(form);
        if(!result){
            return BaseResult.error("关箱失败");
        }
        return BaseResult.ok();
    }

    @ApiOperation("发运")
    @PostMapping(path = "/shipping")
    public BaseResult shipping(@RequestBody Map<String, Object> paramMap) {
        String orderNumber = MapUtil.getStr(paramMap, "orderNumber");
        if(StringUtils.isEmpty(orderNumber)){
            return BaseResult.error("查询单号不为空");
        }

        if(orderNumber.substring(0,2).equals("ON")){
            List<WmsShippingReview> wmsShippingReviews = wmsShippingReviewService.getWmsShippingReviewByOrderNumber(orderNumber);

            if(CollectionUtil.isNotEmpty(wmsShippingReviews)){
                return BaseResult.error("该订单没有复核完成");
            }
            boolean result = wmsShippingReviewService.updateWmsShippingReviewStatusByOrderNumber(orderNumber);
            if(!result){
                return BaseResult.error("发运失败");
            }
        }else{
            List<WmsShippingReview> wmsShippingReviews = wmsShippingReviewService.getWmsShippingReviewByWaveNumber(orderNumber);
            if(CollectionUtil.isNotEmpty(wmsShippingReviews)){
                return BaseResult.error("该订单没有复核完成");
            }
            boolean result = wmsShippingReviewService.updateWmsShippingReviewStatusByWaveNumber(orderNumber);
            if(!result){
                return BaseResult.error("发运失败");
            }
        }
        return BaseResult.ok();
    }

    @ApiOperation("撤箱")
    @PostMapping(path = "/unpacking")
    public BaseResult unpacking(@RequestBody Map<String, Object> paramMap) {
        Long id = MapUtil.getLong(paramMap, "id");
        if(StringUtils.isEmpty(id.toString())){
            return BaseResult.error("撤箱数据不为空");
        }
        boolean result = wmsShippingReviewService.unpacking(id);
        if(!result){
            return BaseResult.error("撤箱失败");
        }
        return BaseResult.ok();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "orderNumber",value = "单号",dataType = "String",required = true),
            @ApiImplicitParam(name = "materialCode",value = "物料编号",dataType = "String",required = true)})
    @ApiOperation("APP获取发运复核数据")
    @GetMapping(path = "/getShippingDataForApp")
    public BaseResult getShippingDataForApp(String orderNumber,String materialCode){
        WmsShippingReview shippingReview = new WmsShippingReview();
        if(orderNumber.substring(0,2).equals("ON")){
            shippingReview.setOrderNumber(orderNumber);
        }else {
            shippingReview.setWareNumber(orderNumber);
        }
        shippingReview.setMaterialCode(materialCode);
        return BaseResult.ok(wmsShippingReviewService.selectList(shippingReview));
    }


    @ApiOperation("APP发运复核确认")
    @PostMapping(path = "/confirmForApp")
    public BaseResult confirmForApp(@RequestBody WmsShippingReviewForm wmsShippingReviewForm){
        return wmsShippingReviewService.confirmForApp(wmsShippingReviewForm);
    }

}
