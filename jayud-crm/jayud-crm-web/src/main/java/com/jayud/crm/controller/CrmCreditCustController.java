package com.jayud.crm.controller;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.model.bo.AddCrmCreditCustForm;
import com.jayud.crm.model.po.CrmContractQuotation;
import com.jayud.crm.model.po.CrmCreditDepart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCreditCustService;
import com.jayud.crm.model.po.CrmCreditCust;

import java.math.BigDecimal;
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
 * 基本档案_额度_额度授信管理(crm_credit_cust) 控制类
 *
 * @author jayud
 * @since 2022-03-04
 */
@Slf4j
@Api(tags = "基本档案_额度_额度授信管理(crm_credit_cust)")
@RestController
@RequestMapping("/crmCreditCust")
public class CrmCreditCustController {


    @Autowired
    public ICrmCreditCustService crmCreditCustService;

    @Autowired
    private AuthClient authClient;

    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-04
     * @param: crmCreditCust
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmCreditCust>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CrmCreditCust>> selectPage(CrmCreditCust crmCreditCust,
                                                               @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        if (crmCreditCust.getCustName() != null) {
            crmCreditCust.setCustName(crmCreditCust.getCustName().trim());
        }
        return BaseResult.ok(PaginationBuilder.buildPageResult(crmCreditCustService.selectPage(crmCreditCust, currentPage, pageSize, req)));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-04
     * @param: crmCreditCust
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmCreditCust>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCreditCust>> selectList(CrmCreditCust crmCreditCust,
                                                      HttpServletRequest req) {
        return BaseResult.ok(crmCreditCustService.selectList(crmCreditCust));
    }


    /**
     * @description 新增/编辑
     **/
    @ApiOperation("新增/编辑")
    @PostMapping("/saveOrUpdate")
    public BaseResult saveOrUpdate(@Valid @RequestBody AddCrmCreditCustForm form) {

        List<CrmCreditCust> list = this.crmCreditCustService.selectList(new CrmCreditCust().setCustId(form.getCustId()).setCreditId(form.getCreditId()).setDepartId(form.getDepartId()).setIsDeleted(false).setTenantCode(CurrentUserUtil.getUserTenantCode()));
        BigDecimal existingAmount = new BigDecimal(0);
        if (list.size() > 0) {
            CrmCreditCust tmp = list.get(0);
            existingAmount = tmp.getCreditAmt();
            if (!tmp.getId().equals(form.getId())) {
                return BaseResult.error(SysTips.LEGAL_CUST_CREDIT);
            }
        }
        if (form.getCreditAmt().compareTo(new BigDecimal(0)) < 0) {
            return BaseResult.error(SysTips.CREDIT_AMOUNT_ERROR);
        }
        //计算剩余额度
        BigDecimal remainingQuota = this.crmCreditCustService.calculationRemainingCreditLine(form.getDepartId(), form.getCreditId(), CurrentUserUtil.getUserTenantCode());
        remainingQuota = remainingQuota.add(existingAmount);
        if (remainingQuota.compareTo(form.getCreditAmt()) < 0) {
            return BaseResult.error(SysTips.INSUFFICIENT_REMAINING_AMOUNT);
        }
        crmCreditCustService.saveOrUpdate(form);

        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * 剩余授信额度计算
     */
    @ApiOperation("剩余授信额度计算")
    @GetMapping("/calculationRemainingCreditLine")
    public BaseResult calculationRemainingCreditLine(@RequestParam("departId") Long departId, @RequestParam("creditId") String creditId) {
        //计算剩余额度
        BigDecimal remainingQuota = this.crmCreditCustService.calculationRemainingCreditLine(departId, creditId, CurrentUserUtil.getUserTenantCode());
        return BaseResult.ok(remainingQuota);
    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-04
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        CrmCreditCust byId = crmCreditCustService.getById(id);
        authClient.addSysLogFeign(" 删除了信用额度", byId.getCustId());
        crmCreditCustService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-04
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCreditCust>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCreditCust> queryById(@RequestParam(name = "id", required = true) int id) {
        CrmCreditCust crmCreditCust = crmCreditCustService.getById(id);
        return BaseResult.ok(crmCreditCust);
    }


    /**
     * @description 根据查询条件导出收货单
     * @author jayud
     * @date 2022-03-04
     * @param: response  响应对象
     * @param: queryReceiptForm  参数queryReceiptForm
     * @param: req
     * @return: void
     **/
    @ApiOperation("根据查询条件导出基本档案_额度_额度授信管理(crm_credit_cust)")
    @PostMapping(path = "/exportCrmCreditCust")
    public void exportCrmCreditCust(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "自动ID",
                    "额度类型ID",
                    "额度类型名称",
                    "额度类型值(1:税费额度,2:期票额度,3:贷款额度)",
                    "法人主体id",
                    "法人主体名称",
                    "客户ID",
                    "客户名称",
                    "申请金额",
                    "开始日期",
                    "结束日期",
                    "申请日期",
                    "额度模式",
                    "额度模式值(1:临时，2:标准)",
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
            List<LinkedHashMap<String, Object>> dataList = crmCreditCustService.queryCrmCreditCustForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_额度_额度授信管理(crm_credit_cust)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
