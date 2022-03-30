package com.jayud.wms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysDataPermission;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.QueryClientReceiptNoticeForm;
import com.jayud.common.utils.ExcelUtils;
import com.jayud.wms.model.bo.QueryReceiptNoticeForm;
import com.jayud.wms.model.bo.ReceiptNoticeForm;
import com.jayud.wms.model.enums.ReceiptNoticeStatusEnum;
import com.jayud.wms.model.po.ReceiptNotice;
import com.jayud.wms.model.vo.ReceiptNoticeVO;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.service.AuthService;
import com.jayud.wms.service.IReceiptNoticeService;
import com.jayud.wms.service.IWarehouseService;
import com.jayud.wms.service.IWmsOwerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * 收货通知单 控制类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Api(tags = "收货通知单")
@RestController
@RequestMapping("/receiptNotice")
public class ReceiptNoticeController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    public IReceiptNoticeService receiptNoticeService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthClient authClient;

    @Autowired
    public IWarehouseService warehouseService;

    @Autowired
    public IWmsOwerInfoService wmsOwerInfoService;

    /**
     * 分页查询数据
     *
     * @param receiptNoticeForm 查询条件
     * @return
     */

    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<ReceiptNoticeVO>> selectPage(@RequestBody QueryReceiptNoticeForm receiptNoticeForm, HttpServletRequest req) {

        IPage<ReceiptNoticeVO> receiptNoticeIPage = receiptNoticeService.selectPage(receiptNoticeForm, receiptNoticeForm.getCurrentPage(), receiptNoticeForm.getPageSize(), req);
        return BaseResult.ok(receiptNoticeIPage);
    }


    /**
     * 列表查询数据
     *
     * @param receiptNotice 查询条件
     * @return
     */
    @ApiOperation("列表查询数据")
    @PostMapping("/selectList")
    public BaseResult<List<ReceiptNotice>> selectList(@RequestBody ReceiptNotice receiptNotice,
                                                  HttpServletRequest req) {
        return BaseResult.ok(receiptNoticeService.selectList(receiptNotice));
    }

    /**
     * 新增/编辑
     *
     * @param form
     **/
    @ApiOperation("新增/编辑")
    @PostMapping("/addOrUpdate")
    public BaseResult addOrUpdate(@Valid @RequestBody ReceiptNoticeForm form) {
        if (!ReceiptNoticeStatusEnum.CREATE.getCode().equals(form.getStatus())) {
            return BaseResult.error("该状态无法操作");
        }
        form.setOrderSource("手工创建");
        form.setOrderSourceCode(1);
        receiptNoticeService.createOrder(form);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    /**
     * 编辑
     *
     * @param receiptNotice
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody ReceiptNotice receiptNotice) {
        receiptNoticeService.updateById(receiptNotice);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    /**
     * 逻辑删除
     *
     * @param id
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/del")
    public BaseResult logicDel(@RequestParam(name = "id") Long id) {

        boolean b = receiptNoticeService.deletedReceiptNotice(id);
        if (b) {
            return BaseResult.ok(SysTips.DEL_SUCCESS);
        }
        return BaseResult.error(SysTips.ERROR_MSG);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<ReceiptNotice> queryById(@RequestParam(name = "id", required = true) int id) {
        ReceiptNotice receiptNotice = receiptNoticeService.getById(id);
        return BaseResult.ok(receiptNotice);
    }

    @ApiOperation("收货通知单部分下拉值")
    @GetMapping(value = "/getNoticeOfDeliveryPaidType")
    public BaseResult getReceiptOfGoodsAdviceOrderSourceType() {
        //收货通知单部分下拉值
        List<LinkedHashMap<String, Object>> receiptOfGoodsAdviceOrderSource = authService.queryDictByDictType("receiptOfGoodsAdviceOrderSource");
        List<LinkedHashMap<String, Object>> notifyReceiptOfOrderStatus = authService.queryDictByDictType("notifyReceiptOfOrderStatus");
        List<LinkedHashMap<String, Object>> receiptNotificationDocument = authService.queryDictByDictType("receiptNotificationDocument");
        notifyReceiptOfOrderStatus.stream().forEach(ro -> {
            ro.put("value", Integer.parseInt(ro.get("value").toString()));
        });
        receiptOfGoodsAdviceOrderSource.stream().forEach(ro -> {
            ro.put("value", Integer.parseInt(ro.get("value").toString()));
        });
        Map<String, Object> map = new HashMap<>();
        map.put("receiptOfGoodsAdviceOrderSource", receiptOfGoodsAdviceOrderSource);
        map.put("receiptNotificationDocument", receiptNotificationDocument);
        map.put("notifyReceiptOfOrderStatus", notifyReceiptOfOrderStatus);
        return BaseResult.ok(map);
    }


    /**
     * 根据查询条件导出收货通知单信息
     *
     * @param response          响应对象
     * @param receiptNoticeForm 参数receiptNoticeForm
     */

    @ApiOperation("根据查询条件导出收货通知单信息")
    @PostMapping(path = "/exportReceiptNoticeLocation")
    public void exportReceiptNoticeLocation(HttpServletResponse response, @RequestBody QueryReceiptNoticeForm receiptNoticeForm, HttpServletRequest req) {
        try {
            List<String> headList = Arrays.asList("收货通知单号", "仓库名称", "货主", "订单状态", "合计数量", "合计重量", "合计体积", "创建人", "预计到货时间", "外部单号1", "外部单号2", "收货单号", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = receiptNoticeService.queryReceiptNoticeForExcel(receiptNoticeForm, req);
            ExcelUtils.exportExcel(headList, dataList, "收货通知单信息", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    //调试单号
    @ApiOperation("*收货通知单号")
    @GetMapping(value = "/receiptNoteNumber")
    public BaseResult receiptNoteNumber() {
        //查询客户类型为供应商的ID
        BaseResult baseResult = authClient.getOrderFeign("receipt_notice",new Date());
        String order = "";
        if (baseResult.isSuccess()){
            HashMap data = (HashMap)baseResult.getResult();
            order = data.get("order").toString();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        return BaseResult.ok(map);
    }

    /**
     * 获取收货通知单详情信息
     *
     * @param id
     */
    @ApiOperation("获取收货通知单详情信息")
    @GetMapping(value = "/getDetails")
    public BaseResult<ReceiptNoticeVO> getDetails(@RequestParam(name = "id") Long id) {
        ReceiptNoticeVO receiptNoticeVO = receiptNoticeService.getDetails(id);
        return BaseResult.ok(receiptNoticeVO);
    }


    @ApiOperation("终端增加物料操作")
    @GetMapping(value = "/addMaterialToTerminal")
    public BaseResult addMaterialToTerminal(@RequestBody Map<String, Object> map) {
        String serialNum = MapUtil.getStr(map, "serialNum");
        String materialCode = MapUtil.getStr(map, "materialCode");
        if (StringUtils.isEmpty(serialNum) && StringUtils.isEmpty(materialCode)) {
            return BaseResult.error("请输入物料序列号/物料编码");
        }
        receiptNoticeService.addMaterialToTerminal(map);
        return BaseResult.ok();
    }


    @ApiOperation("转收货单")
    @PostMapping(value = "/transferReceipt")
    public BaseResult transferReceipt(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.PARAM_ERROR);
        }
        receiptNoticeService.transferReceipt(id);
        return BaseResult.ok();
    }


    /**
     * 外部 调用   新增/编辑
     *
     * @param form
     **/
    @ApiOperation("新增/编辑")
    @PostMapping("/client/addOrUpdateFeign")
    public BaseResult addOrUpdateFeign(@RequestBody QueryClientReceiptNoticeForm form) {

       //进到这里面已经经过权限校验
        BaseResult result = receiptNoticeService.addOrUpdateFeign(form);


        return BaseResult.ok();
    }

    /**
     * ERP获取收货通知单详情信息
     *
     * @param id
     */
    @ApiOperation("ERP获取收货通知单详情信息")
    @GetMapping(value = "/client/getDetails")
    public BaseResult<ReceiptNoticeVO> getDetailsFeign(@RequestParam(name = "id") Long id) {
        ReceiptNoticeVO receiptNoticeVO = receiptNoticeService.getDetails(id);
        return BaseResult.ok(receiptNoticeVO);
    }





}
