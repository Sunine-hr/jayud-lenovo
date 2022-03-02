package com.jayud.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmContractQuotationDetailsService;
import com.jayud.crm.model.po.CrmContractQuotationDetails;

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
 * 合同报价详情 控制类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Api(tags = "合同报价详情")
@RestController
@RequestMapping("/crmContractQuotationDetails")
public class CrmContractQuotationDetailsController {



    @Autowired
    public ICrmContractQuotationDetailsService crmContractQuotationDetailsService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: crmContractQuotationDetails
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmContractQuotationDetails>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmContractQuotationDetails>> selectPage(CrmContractQuotationDetails crmContractQuotationDetails,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmContractQuotationDetailsService.selectPage(crmContractQuotationDetails,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-01
    * @param: crmContractQuotationDetails
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmContractQuotationDetails>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmContractQuotationDetails>> selectList(CrmContractQuotationDetails crmContractQuotationDetails,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmContractQuotationDetailsService.selectList(crmContractQuotationDetails));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-01
    * @param: crmContractQuotationDetails
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmContractQuotationDetails crmContractQuotationDetails ){
        crmContractQuotationDetailsService.save(crmContractQuotationDetails);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-01
     * @param: crmContractQuotationDetails
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmContractQuotationDetails crmContractQuotationDetails ){
        crmContractQuotationDetailsService.updateById(crmContractQuotationDetails);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        crmContractQuotationDetailsService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        crmContractQuotationDetailsService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmContractQuotationDetails>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmContractQuotationDetails> queryById(@RequestParam(name="id",required=true) int id) {
        CrmContractQuotationDetails crmContractQuotationDetails = crmContractQuotationDetailsService.getById(id);
        return BaseResult.ok(crmContractQuotationDetails);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-01
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出合同报价详情")
    @PostMapping(path = "/exportCrmContractQuotationDetails")
    public void exportCrmContractQuotationDetails(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键",
                "子订单类型",
                "合同报价id",
                "类型(1:整车 2:其他)",
                "起始地",
                "目的地",
                "车型(3T 5T 8T 10T 12T 20GP 40GP 45GP)",
                "费用名称code",
                "单价",
                "单位",
                "币种代码",
                "费用类别(作业环节)",
                "状态（0禁用 1启用）",
                "起始地id(多个逗号隔开)",
                "目的地id(多个逗号隔开)",
                "重量计费/kg",
                "件数计费/件",
                "板数计费/版",
                "最低计费",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmContractQuotationDetailsService.queryCrmContractQuotationDetailsForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "合同报价详情", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
