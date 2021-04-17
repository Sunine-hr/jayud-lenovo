package com.jayud.mall.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "A043-admin-订单接口")
@ApiSort(value = 43)
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "分页查询订单")
    @PostMapping("/findOrderInfoByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OrderInfoVO>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
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

    //订单待生成账单-生成账单(根据 订单id 查询)
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
    @PostMapping("/submitOrderInfo")
    @ApiOperationSupport(order = 24)
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




}
