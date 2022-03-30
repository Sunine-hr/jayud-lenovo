package com.jayud.oms.order.controller;

import com.jayud.oms.order.model.bo.CheckForm;
import com.jayud.oms.order.model.bo.InputOrderForm;
import com.jayud.oms.order.model.bo.OmsOrderForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.oms.order.service.IOmsOrderService;
import com.jayud.oms.order.model.po.OmsOrder;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 订单管理——订单主表 控制类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Api(tags = "订单管理——订单主表")
@RestController
@RequestMapping("/omsOrder")
public class OmsOrderController {



    @Autowired
    public IOmsOrderService omsOrderService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrder
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrder>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<OmsOrder>> selectPage(OmsOrder omsOrder,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(omsOrderService.selectPage(omsOrder,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-23
    * @param: omsOrder
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.oms.order.model.po.OmsOrder>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<OmsOrder>> selectList(OmsOrder omsOrder,
                                                HttpServletRequest req) {
        return BaseResult.ok(omsOrderService.selectList(omsOrder));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-23
    * @param: omsOrder
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody InputOrderForm form ){
        if(!"pass".equals(form.getOrderForm().check()) && form.getCmd().equals("submit")){
            return BaseResult.error(444,form.getOrderForm().check());
        }
        return omsOrderService.saveOmsOrder(form);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-23
//     * @param: omsOrder
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody OmsOrder omsOrder ){
//        omsOrderService.updateById(omsOrder);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id){
//        omsOrderService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        omsOrderService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.oms.order.model.po.OmsOrder>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<OmsOrder> queryById(@RequestParam(name="id",required=true) int id) {
        OmsOrder omsOrder = omsOrderService.getById(id);
        return BaseResult.ok(omsOrder);
    }

    @ApiOperation("根据id修改订单列表数据")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @PostMapping(value = "/updateById")
    public BaseResult updateById(@RequestBody OmsOrderForm omsOrderForm) {

        return omsOrderService.updateOmsOrderById(omsOrderForm);
    }




    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-23
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出订单管理——订单主表")
    @PostMapping(path = "/exportOmsOrder")
    public void exportOmsOrder(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "订单编号",
                "客户参考号",
                "订单日期",
                "业务类型",
                "客户ID",
                "客户编号",
                "客户名称",
                "客户下单人",
                "客户邮箱",
                "接单主体",
                "接单部门",
                "接单业务ID",
                "接单业务员名称",
                "协议ID",
                "协议编号",
                "结算方式",
                "订单状态(用0，1，2表示状态，定义一个枚举)",
                "审核级别",
                "当前级别",
                "审核状态",
                "流程实例",
                "提交审核人",
                "提交时间",
                "最后审核人",
                "最后审核时间",
                "业务要求",
                "备注",
                "组织机构ID",
                "多租户ID",
                "创建人名称",
                "创建时间",
                "最后修改人名称",
                "最后修改时间",
                "删除标志",
                "删除人",
                "删除时间"
            );
            List<LinkedHashMap<String, Object>> dataList = omsOrderService.queryOmsOrderForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "订单管理——订单主表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }

    /**
     * 审核
     */
    @ApiOperation("审核")
    @PostMapping(value = "/check")
    public BaseResult check(@RequestBody CheckForm checkForm){

        return omsOrderService.check(checkForm);
    }


    /**
     * 反核
     */
    @ApiOperation("反核")
    @PostMapping(value = "/unCheck")
    public BaseResult unCheck(@RequestBody CheckForm checkForm){

        return omsOrderService.unCheck(checkForm);
    }


}
