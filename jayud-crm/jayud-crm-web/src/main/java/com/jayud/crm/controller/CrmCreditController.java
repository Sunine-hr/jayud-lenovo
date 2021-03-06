package com.jayud.crm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.bo.AddCrmCreditForm;
import com.jayud.crm.model.po.CrmCreditDepart;
import com.jayud.crm.model.po.CrmCustomer;
import com.jayud.crm.model.vo.CrmCreditVO;
import com.jayud.crm.service.ICrmCreditDepartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCreditService;
import com.jayud.crm.model.po.CrmCredit;

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
 * 基本档案_额度_额度总量(crm_credit) 控制类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Api(tags = "基本档案_额度_额度总量(crm_credit)")
@RestController
@RequestMapping("/crmCredit")
public class CrmCreditController {


    @Autowired
    public ICrmCreditService crmCreditService;
    @Autowired
    public ICrmCreditDepartService crmCreditDepartService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-03
     * @param: crmCredit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmCredit>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CrmCreditVO>> selectPage(CrmCredit crmCredit,
                                                             @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                             HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(crmCreditService.selectPage(crmCredit, currentPage, pageSize, req)));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-03
     * @param: crmCredit
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmCredit>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCreditVO>> selectList(CrmCredit crmCredit,
                                                    HttpServletRequest req) {
        return BaseResult.ok(crmCreditService.selectList(crmCredit));
    }


    /**
     * @description 新增/编辑
     * @author jayud
     * @date 2022-03-03
     * @param: crmCredit
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增/编辑")
    @PostMapping("/saveOrUpdate")
    public BaseResult saveOrUpdate(@Valid @RequestBody AddCrmCreditForm form) {

        if (form.getId() != null) {
            List<CrmCreditVO> tmps = this.crmCreditService.selectList(new CrmCredit().setCreditId(form.getCreditId()).setIsDeleted(false).setTenantCode(CurrentUserUtil.getUserTenantCode()));
            CrmCredit crmCredit = this.crmCreditService.getById(form.getId());
            if (form.getCreditMoney().compareTo(crmCredit.getCreditGrantedMoney()) < 0) {
                return BaseResult.error(SysTips.INSUFFICIENT_REMAINING_AMOUNT);
            }
            if (!CollectionUtil.isEmpty(tmps)) {
                CrmCreditVO crmCreditVO = tmps.get(0);
                if (!crmCreditVO.getId().equals(form.getId())) {
                    return BaseResult.error(SysTips.TYPE_ALREADY_EXISTS);
                }
            }

        }
        crmCreditService.saveOrUpdate(form);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        CrmCredit crmCredit = this.crmCreditService.getById(id);
        //是否存在已分配额度
        List<CrmCreditDepart> creditDeparts = this.crmCreditDepartService.selectList(new CrmCreditDepart().setCreditId(crmCredit.getCreditId()).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        if (!CollectionUtil.isEmpty(creditDeparts)) {
            return BaseResult.error(SysTips.CREDIT_DELETE_ERROR);
        }
        crmCreditService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCredit>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCreditVO> queryById(@RequestParam(name = "id", required = true) int id) {
        CrmCredit crmCredit = crmCreditService.getById(id);
        CrmCreditVO convert = ConvertUtil.convert(crmCredit, CrmCreditVO.class);
        List<CrmCreditDepart> creditDeparts = this.crmCreditDepartService.selectList(new CrmCreditDepart().setCreditId(crmCredit.getCreditId()).setTenantCode(CurrentUserUtil.getUserTenantCode()).setIsDeleted(false));
        convert.setIsCredit(!CollectionUtil.isEmpty(creditDeparts));
        return BaseResult.ok(convert);
    }


    /**
     * @description 根据查询条件导出收货单
     * @author jayud
     * @date 2022-03-03
     * @param: response  响应对象
     * @param: queryReceiptForm  参数queryReceiptForm
     * @param: req
     * @return: void
     **/
    @ApiOperation("根据查询条件导出基本档案_额度_额度总量(crm_credit)")
    @PostMapping(path = "/exportCrmCredit")
    public void exportCrmCredit(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "自动ID",
                    "额度类型ID",
                    "额度类型",
                    "额度类型值(1:税费额度,2:期票额度,3:贷款额度)",
                    "授信总金额",
                    "已授信额度",
                    "租户编码",
                    "备注",
                    "是否删除，0未删除，1已删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCreditService.queryCrmCreditForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_额度_额度总量(crm_credit)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
