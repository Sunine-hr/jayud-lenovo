package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageDraftResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOrderInfoService;
import com.jayud.mall.service.IOrderInteriorStatusService;
import com.jayud.mall.utils.ExcelTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "A043-admin-订单接口")
@ApiSort(value = 43)
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    IOrderInteriorStatusService orderInteriorStatusService;


    @ApiOperation(value = "分页查询订单")
    @PostMapping("/findOrderInfoByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageDraftResult<OrderInfoVO>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        Map<String,Long> totalMap = orderInfoService.findOrderInfoAfterCount(form);
        CommonPageDraftResult<OrderInfoVO> draftResult = new CommonPageDraftResult(pageList, totalMap);
        return CommonResult.success(draftResult);
    }

    @ApiOperation(value = "订单管理-查看审核文件")
    @PostMapping("/lookOrderInfoFile")
    @ApiOperationSupport(order = 2)
    public CommonResult<OrderInfoVO> lookOrderInfoFile(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoFile(id);
    }

    //pass
    @ApiOperation(value = "审核通过-订单对应报关文件")
    @PostMapping("/passOrderCustomsFile")
    @ApiOperationSupport(order = 3)
    public CommonResult<OrderCustomsFileVO> passOrderCustomsFile(@RequestBody OrderCustomsFileForm form){
        Long id = form.getId();
        return orderInfoService.passOrderCustomsFile(id);
    }

    @ApiOperation(value = "审核通过-订单对应清关文件")
    @PostMapping("/passOrderClearanceFile")
    @ApiOperationSupport(order = 4)
    public CommonResult<OrderClearanceFileVO> passOrderClearanceFile(@RequestBody OrderClearanceFileForm form){
        Long id = form.getId();
        return orderInfoService.passOrderClearanceFile(id);
    }

    //no pass
    @ApiOperation(value = "审核不通过-订单对应报关文件")
    @PostMapping("/onPassCustomsFile")
    @ApiOperationSupport(order = 5)
    public CommonResult<OrderCustomsFileVO> onPassCustomsFile(@RequestBody OrderCustomsFileForm form){
        Long id = form.getId();
        return orderInfoService.onPassCustomsFile(id);
    }

    @ApiOperation(value = "审核不通过-订单对应清关文件")
    @PostMapping("/onPassOrderClearanceFile")
    @ApiOperationSupport(order = 6)
    public CommonResult<OrderClearanceFileVO> onPassOrderClearanceFile(@RequestBody OrderClearanceFileForm form){
        Long id = form.getId();
        return orderInfoService.onPassOrderClearanceFile(id);
    }

    //订单管理-查看货物信息
    @ApiOperation(value = "订单管理-查看货物信息")
    @PostMapping("/lookOrderInfoGoods")
    @ApiOperationSupport(order = 7)
    public CommonResult<OrderInfoVO> lookOrderInfoGoods(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoGoods(id);
    }

    @ApiOperation(value = "订单管理-修改订单箱号(长宽高等)")
    @PostMapping("/updateOrderCase")
    @ApiOperationSupport(order = 8)
    public CommonResult updateOrderCase(@RequestBody List<OrderCaseForm> list){
        orderInfoService.updateOrderCase(list);
        return CommonResult.success("保存箱号信息，成功！");
    }

    //订单管理-查看配载信息
    @ApiOperation(value = "订单管理-查看配载信息")
    @PostMapping("/lookOrderInfoConf")
    @ApiOperationSupport(order = 9)
    public CommonResult<OrderInfoVO> lookOrderInfoConf(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoConf(id);
    }

    //订单管理-修改配载信息
    @ApiOperation(value = "订单管理-修改配载信息(废弃)")
    @PostMapping("/updateOrderCaseConf")
    @ApiOperationSupport(order = 10)
    @Deprecated
    public CommonResult updateOrderCaseConf(@Valid @RequestBody SaveCounterCaseForm form){
        return orderInfoService.updateOrderCaseConf(form);
    }

    //订单管理-修改配载信息2
    @ApiOperation(value = "订单管理-修改配载信息2")
    @PostMapping("/updateOrderCaseConf2")
    @ApiOperationSupport(order = 11)
    public CommonResult updateOrderCaseConf2(@Valid @RequestBody SaveCounterCase2Form form){
        return orderInfoService.updateOrderCaseConf2(form);
    }

    //订单管理-查看费用信息
    @ApiOperation(value = "订单管理-查看费用信息")
    @PostMapping("/lookOrderInfoCost")
    @ApiOperationSupport(order = 12)
    public CommonResult<OrderInfoVO> lookOrderInfoCost(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoCost(id);
    }

    //订单管理-修改费用信息
    @ApiOperation(value = "订单管理-修改费用信息")
    @PostMapping("/updateOrderInfoCost")
    @ApiOperationSupport(order = 13)
    public CommonResult updateOrderInfoCost(@RequestBody OrderInfoCostForm form){
        return orderInfoService.updateOrderInfoCost(form);
    }

    //订单管理-查看订单详细
    @ApiOperation(value = "订单管理-查看订单详细")
    @PostMapping("/lookOrderInfoDetails")
    @ApiOperationSupport(order = 14)
    public CommonResult<OrderInfoVO> lookOrderInfoDetails(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoDetails(id);
    }

    //根据订单的运单id，查找配载，在查找配载单关联的提单
    @ApiOperation(value = "根据订单的运单id，查找配载，在查找配载单关联的提单")
    @PostMapping("/findOceanBillByOfferInfoId")
    @ApiOperationSupport(order = 15)
    public CommonResult<List<OceanBillVO>> findOceanBillByOfferInfoId(@Valid @RequestBody OfferInfoIdForm form){
        Integer offerInfoId = form.getOfferInfoId();
        return orderInfoService.findOceanBillByOfferInfoId(offerInfoId);
    }

    //根据提单id，查找提单关联的柜号id list(其实是1对1的)
    @ApiOperation(value = "根据提单id，查找提单关联的柜号id list(其实是1对1的)")
    @PostMapping("/findOceanCounterByTdId")
    @ApiOperationSupport(order = 16)
    public CommonResult<List<OceanCounterVO>> findOceanCounterByTdId(@Valid @RequestBody TdIdForm form){
        Long tdId = form.getTdId();
        return orderInfoService.findOceanCounterByTdId(tdId);
    }

    //根据订单id，查询待生成的账单（应收、应付）
    @ApiOperation(value = "订单待生成账单-生成账单(根据 订单id 查询)")
    @PostMapping("/findOrderBill")
    @ApiOperationSupport(order = 17)
    public CommonResult<OrderBillVO> findOrderBill(@Valid @RequestBody OrderInfoParaForm form){
        Long orderId = form.getId();
        return orderInfoService.findOrderBill(orderId);
    }

    //查看订单任务反馈状态
    @ApiOperation(value = "运单任务-反馈状态(根据 订单id 查询)")
    @PostMapping("/lookOrderInfoTask")
    @ApiOperationSupport(order = 18)
    public CommonResult<OrderInfoVO> lookOrderInfoTask(@Valid @RequestBody OrderInfoParaForm form){
        Long orderId = form.getId();
        return orderInfoService.lookOrderInfoTask(orderId);
    }

    //运单任务-反馈状态(点击已完成)
    @ApiOperation(value = "运单任务-反馈状态(点击已完成)")
    @PostMapping("/confirmCompleted")
    @ApiOperationSupport(order = 19)
    public CommonResult<WaybillTaskRelevanceVO> confirmCompleted(@Valid @RequestBody WaybillTaskRelevanceParaForm form){
        Long id = form.getId();
        return orderInfoService.confirmCompleted(id);
    }

    //订单操作日志（根据订单id查看）
    @ApiOperation(value = "运单任务-订单操作日志（根据订单id查看）")
    @PostMapping("/lookOperateLog")
    @ApiOperationSupport(order = 20)
    public CommonResult<List<WaybillTaskRelevanceVO>> lookOperateLog(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        return orderInfoService.lookOperateLog(id);
    }

    //同步订单(根据南京新智慧api查询运单同步订单)
    @ApiOperation(value = "同步订单(根据南京新智慧api查询运单同步订单)")
    @PostMapping("/syncOrder")
    @ApiOperationSupport(order = 21)
    public CommonResult syncOrder(@Valid @RequestBody SyncOrderForm form){
        return orderInfoService.syncOrder(form);
    }

    //订单-草稿-提交-进入编辑订单详情(从草稿进去，可以编辑，暂存和提交)给新智慧用的
    @ApiOperation(value = "订单-草稿-提交-进入编辑订单详情(从草稿进去，可以编辑，暂存和提交)给新智慧用的")
    @PostMapping("/newEditOrderInfo")
    @ApiOperationSupport(order = 22)
    public CommonResult<OrderInfoVO> newEditOrderInfo(@Valid @RequestBody OrderInfoNewForm form){
        return orderInfoService.newEditOrderInfo(form);
    }

    //订单下单-暂存订单
    @ApiOperation(value = "订单下单-暂存订单(新智慧订单)")
    @PostMapping("/temporaryStorageOrderInfo")
    @ApiOperationSupport(order = 23)
    public CommonResult temporaryStorageOrderInfo(@Valid @RequestBody OrderInfoForm form){
        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        if(CollUtil.isEmpty(orderCaseVOList)){
            return CommonResult.error(-1, "订单箱号不能为空");
        }
        //订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        if(CollUtil.isEmpty(orderShopVOList)){
            return CommonResult.error(-1, "订单商品不能为空");
        }
        Integer isPick = form.getIsPick();//是否上门提货(0否 1是,order_pick) is_pick=1
        if(isPick == 1){
            //订单对应提货信息表：order_pick
            List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
            if(CollUtil.isEmpty(orderPickVOList)){
                return CommonResult.error(-1, "订单提货信息不能为空");
            }
            Integer totalCartonSum = 0;
            for (int i=0; i<orderPickVOList.size(); i++){
                OrderPickVO orderPickVO = orderPickVOList.get(i);
                Integer totalCarton = orderPickVO.getTotalCarton();
                if(ObjectUtil.isEmpty(totalCarton) || totalCarton <= 0){
                    return CommonResult.error(-1, "提货地址的箱数，不能为空或者小于等于0");
                }
                totalCartonSum += totalCarton;
            }
            int size = orderCaseVOList.size();
            if(!totalCartonSum.equals(size)){
                return CommonResult.error(-1, "提货箱数不等于订单箱数");
            }
        }
        return orderInfoService.temporaryStorageOrderInfo(form);
    }

    //订单下单-提交订单
    @ApiOperation(value = "订单下单-提交订单(新智慧订单)")
    @ApiOperationSupport(order = 24)
    @PostMapping("/submitOrderInfo")
    public CommonResult<OrderInfoVO> submitOrderInfo(@Valid @RequestBody OrderInfoForm form){
        //订单对应箱号信息:order_case
        List<OrderCaseVO> orderCaseVOList = form.getOrderCaseVOList();
        if(CollUtil.isEmpty(orderCaseVOList)){
            return CommonResult.error(-1, "订单箱号不能为空");
        }
        //订单对应商品：order_shop
        List<OrderShopVO> orderShopVOList = form.getOrderShopVOList();
        if(CollUtil.isEmpty(orderShopVOList)){
            return CommonResult.error(-1, "订单商品不能为空");
        }
        Integer isPick = form.getIsPick();//是否上门提货(0否 1是,order_pick) is_pick=1
        if(isPick == 1){
            //订单对应提货信息表：order_pick
            List<OrderPickVO> orderPickVOList = form.getOrderPickVOList();
            if(CollUtil.isEmpty(orderPickVOList)){
                return CommonResult.error(-1, "订单提货信息不能为空");
            }
            Integer totalCartonSum = 0;
            for (int i=0; i<orderPickVOList.size(); i++){
                OrderPickVO orderPickVO = orderPickVOList.get(i);
                Integer totalCarton = orderPickVO.getTotalCarton();
                if(ObjectUtil.isEmpty(totalCarton) || totalCarton <= 0){
                    return CommonResult.error(-1, "提货地址的箱数，不能为空或者小于等于0");
                }
                totalCartonSum += totalCarton;
            }
            int size = orderCaseVOList.size();
            if(!totalCartonSum.equals(size)){
                return CommonResult.error(-1, "提货箱数不等于订单箱数");
            }
        }
        return orderInfoService.submitOrderInfo(form);
    }

    // 后台-订单确认
    @ApiOperation(value = "后台-订单确认")
    @ApiOperationSupport(order = 25)
    @PostMapping("/afterAffirm")
    public CommonResult<OrderInfoVO> afterAffirm(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        OrderInfoVO orderInfoVO = orderInfoService.afterAffirm(id);
        return CommonResult.success(orderInfoVO);
    }

    // 查询订单内部状态，
    // IS_AUDIT_ORDER("is_audit_order", "是否审核单据(1已审单 2未审单)")
    // 订单已下单-内部状态审核(已审单 未审单)
    @ApiOperation(value = "查询-订单是否审核单据状态")
    @ApiOperationSupport(order = 26)
    @PostMapping("/findOrderIsAuditOrder")
    public CommonResult<IsAuditOrderVO> findOrderIsAuditOrder(@Valid @RequestBody OrderInfoParaForm form){
        Long orderId = form.getId();
        IsAuditOrderVO isAuditOrderVO = orderInfoService.findOrderIsAuditOrder(orderId);
        return CommonResult.success(isAuditOrderVO);
    }

    @ApiOperation(value = "审核-订单内部状态(是否审核单据)")
    @ApiOperationSupport(order = 27)
    @PostMapping("/auditOrderIsAuditOrder")
    public CommonResult auditOrderIsAuditOrder(@Valid @RequestBody IsAuditOrderForm form){
        orderInfoService.auditOrderIsAuditOrder(form);
        return CommonResult.success("操作成功");
    }

    //订单列表-草稿-取消
    @ApiOperation(value = "订单列表-草稿-取消")
    @ApiOperationSupport(order = 28)
    @PostMapping("/draftCancelOrderInfo")
    public CommonResult<OrderInfoVO> draftCancelOrderInfo(@RequestBody OrderInfoParaForm form){
        return orderInfoService.draftCancelOrderInfo(form);
    }

    //补资料操作
    @ApiOperation(value = "补资料操作")
    @ApiOperationSupport(order = 29)
    @PostMapping("/fillMaterial")
    public CommonResult fillMaterial(@Valid @RequestBody OrderInfoFillForm form){
        orderInfoService.fillMaterial(form);
        return CommonResult.success("操作成功");
    }

    //订单-仓库收货(订单箱号收货)
    @ApiOperation(value = "订单-仓库收货(订单箱号收货)")
    @ApiOperationSupport(order = 30)
    @PostMapping("/orderCaseReceipt")
    public CommonResult orderCaseReceipt(@Valid @RequestBody OrderCaseReceiptForm form){
        orderInfoService.orderCaseReceipt(form);
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "运单-保存轨迹通知")
    @ApiOperationSupport(order = 31)
    @PostMapping(value = "/saveTrackNotice")
    public CommonResult saveTrackNotice(@Valid @RequestBody OrderTrackNoticeForm form){
        orderInfoService.saveTrackNotice(form);
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "后台-确认收货")
    @ApiOperationSupport(order = 32)
    @PostMapping("/affirmReceived")
    public CommonResult<OrderInfoVO> affirmReceived(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        OrderInfoVO orderInfoVO = orderInfoService.affirmReceived(id);
        return CommonResult.success(orderInfoVO);
    }

    @ApiOperation(value = "订单-取消按钮前验证")
    @ApiOperationSupport(order = 33)
    @PostMapping(value = "/cancelStatusVerify")
    public CommonResult cancelStatusVerify(@Valid @RequestBody OrderInfoCancelForm form){
        orderInfoService.cancelStatusVerify(form);
        return CommonResult.success("验证成功");
    }

    //使用新智慧Excel，修改订单箱子的数据
    @ApiOperation(value = "使用新智慧Excel，修改订单箱子的数据")
    @RequestMapping(value = "/importExcelUpdateCaseByNewWisdom", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperationSupport(order = 34)
    public CommonResult<OrderInfoVO> importExcelUpdateCaseByNewWisdom(@RequestHeader(value = "orderId") Long orderId, @RequestParam("file") MultipartFile file){
        OrderInfoVO orderInfoVO = orderInfoService.importExcelUpdateCaseByNewWisdom(orderId,file);
        return CommonResult.success(orderInfoVO);
    }

    // 后台-订单签收
    @ApiOperation(value = "后台-订单签收")
    @ApiOperationSupport(order = 35)
    @PostMapping("/afterSigned")
    public CommonResult<OrderInfoVO> afterSigned(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        OrderInfoVO orderInfoVO = orderInfoService.afterSigned(id);
        return CommonResult.success(orderInfoVO);
    }

    @ApiOperation(value = "下载Excel模板-订单箱号")
    @RequestMapping(value = "/downloadExcelTemplateByOrderCase", method = RequestMethod.GET)
    @ApiOperationSupport(order = 36)
    public void downloadExcelTemplateByOrderCase(HttpServletResponse response){
        new ExcelTemplateUtil().downloadExcel(response, "order_case_update.xlsx", "订单箱号模板.xlsx");
    }


    @ApiOperation(value = "上传文件-导入订单箱号")
    @RequestMapping(value = "/importExcelByOrderCase", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperationSupport(order = 37)
    public CommonResult<List<OrderCaseVO>> importExcelByOrderCase(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        // 1.获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用用 hutool 方法读取数据 默认调用第一个sheet
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        //配置别名
        Map<String,String> aliasMap=new HashMap<>();
        aliasMap.put("FBA箱号","fabNo");
        aliasMap.put("货箱重量(KG)","wmsWeight");
        aliasMap.put("货箱长度(CM)","wmsLength");
        aliasMap.put("货箱宽度(CM)","wmsWidth");
        aliasMap.put("货箱高度(CM)","wmsHeight");
        excelReader.setHeaderAlias(aliasMap);
        // 第一个参数是指表头所在行，第二个参数是指从哪一行开始读取
        List<OrderCaseVO> list= excelReader.read(0, 1, OrderCaseVO.class);

        orderInfoService.importExcelByOrderCase(list);

        return CommonResult.success(list);
    }


    //订单详情-确认计费重信息
    @ApiOperation(value = "查询-订单计费重确认状态")
    @PostMapping("/findOrderIsConfirmBilling")
    @ApiOperationSupport(order = 38)
    public CommonResult<IsConfirmBillingVO> findOrderIsConfirmBilling(@RequestBody OrderInfoParaForm form){
        Long orderId = form.getId();
        IsConfirmBillingVO isConfirmBillingVO = orderInfoService.findOrderIsConfirmBilling(orderId);
        return CommonResult.success(isConfirmBillingVO);
    }


}
