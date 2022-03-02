package com.jayud.crm.controller;

import com.jayud.common.CommonResult;
import com.jayud.crm.model.bo.AddCrmContractQuotationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmContractQuotationService;
import com.jayud.crm.model.po.CrmContractQuotation;

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
 * 合同报价 控制类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Api(tags = "合同报价")
@RestController
@RequestMapping("/crmContractQuotation")
public class CrmContractQuotationController {


    @Autowired
    public ICrmContractQuotationService crmContractQuotationService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-01
     * @param: crmContractQuotation
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmContractQuotation>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmContractQuotation>> selectPage(CrmContractQuotation crmContractQuotation,
                                                              @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                              HttpServletRequest req) {
        return BaseResult.ok(crmContractQuotationService.selectPage(crmContractQuotation, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-01
     * @param: crmContractQuotation
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmContractQuotation>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmContractQuotation>> selectList(CrmContractQuotation crmContractQuotation,
                                                             HttpServletRequest req) {
        return BaseResult.ok(crmContractQuotationService.selectList(crmContractQuotation));
    }


    @ApiOperation("新增/编辑合同报价")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody AddCrmContractQuotationForm form) {
        form.checkParam();
        if (form.getId() != null) {
            CrmContractQuotation contractQuotation = this.crmContractQuotationService.getById(form.getId());
            if (contractQuotation.getFLevel() != null && contractQuotation.getFStep() != 0
                    && !contractQuotation.getFStep().equals(contractQuotation.getFLevel())) {
                return CommonResult.error(400, SysTips.UNDER_REVIEW_NOT_OPT);
            }
        }
        form.checkCostDuplicate();
        return CommonResult.success(this.crmContractQuotationService.saveOrUpdate(form));
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-03-01
     * @param: crmContractQuotation
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmContractQuotation crmContractQuotation) {
        crmContractQuotationService.save(crmContractQuotation);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author jayud
     * @date 2022-03-01
     * @param: crmContractQuotation
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmContractQuotation crmContractQuotation) {
        crmContractQuotationService.updateById(crmContractQuotation);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id) {
        crmContractQuotationService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        crmContractQuotationService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmContractQuotation>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmContractQuotation> queryById(@RequestParam(name = "id", required = true) int id) {
        CrmContractQuotation crmContractQuotation = crmContractQuotationService.getById(id);
        return BaseResult.ok(crmContractQuotation);
    }


    /**
     * @description 根据查询条件导出收货单
     * @author jayud
     * @date 2022-03-01
     * @param: response  响应对象
     * @param: queryReceiptForm  参数queryReceiptForm
     * @param: req
     * @return: void
     **/
    @ApiOperation("根据查询条件导出合同报价")
    @PostMapping(path = "/exportCrmContractQuotation")
    public void exportCrmContractQuotation(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "报价编号(合同报价编号)",
                    "客户/供应商id",
                    "客户/供应商code",
                    "客户/供应商名称",
                    "报价名称",
                    "有效起始时间",
                    "有效结束时间",
                    "状态（0禁用 1启用）",
                    "合同对象(1:客户,2:供应商)",
                    "法人主体名称id",
                    "法人主体",
                    "销售员id",
                    "销售员名称",
                    "审核级别",
                    "当前级别",
                    "审核状态",
                    "租户编码",
                    "备注",
                    "是否删除，0未删除，1已删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmContractQuotationService.queryCrmContractQuotationForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "合同报价", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
