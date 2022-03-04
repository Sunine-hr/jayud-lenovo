package com.jayud.crm.controller;

import com.jayud.crm.model.bo.AddCrmCreditDepartForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCreditDepartService;
import com.jayud.crm.model.po.CrmCreditDepart;

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
 * 基本档案_额度_部门额度授信管理(crm_credit_depart) 控制类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Api(tags = "基本档案_额度_部门额度授信管理(crm_credit_depart)")
@RestController
@RequestMapping("/crmCreditDepart")
public class CrmCreditDepartController {


    @Autowired
    public ICrmCreditDepartService crmCreditDepartService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-03
     * @param: crmCreditDepart
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmCreditDepart>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCreditDepart>> selectPage(CrmCreditDepart crmCreditDepart,
                                                         @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        return BaseResult.ok(crmCreditDepartService.selectPage(crmCreditDepart, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-03
     * @param: crmCreditDepart
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmCreditDepart>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCreditDepart>> selectList(CrmCreditDepart crmCreditDepart,
                                                        HttpServletRequest req) {
        return BaseResult.ok(crmCreditDepartService.selectList(crmCreditDepart));
    }


    /**
     * @description 新增/编辑
     **/
    @ApiOperation("新增/编辑")
    @PostMapping("/saveOrUpdate")
    public BaseResult saveOrUpdate(@Valid @RequestBody AddCrmCreditDepartForm form) {
        crmCreditDepartService.saveOrUpdate(form);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author jayud
     * @date 2022-03-03
     * @param: crmCreditDepart
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCreditDepart crmCreditDepart) {
        crmCreditDepartService.updateById(crmCreditDepart);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id) {
        crmCreditDepartService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
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
        crmCreditDepartService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCreditDepart>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCreditDepart> queryById(@RequestParam(name = "id", required = true) int id) {
        CrmCreditDepart crmCreditDepart = crmCreditDepartService.getById(id);
        return BaseResult.ok(crmCreditDepart);
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
    @ApiOperation("根据查询条件导出基本档案_额度_部门额度授信管理(crm_credit_depart)")
    @PostMapping(path = "/exportCrmCreditDepart")
    public void exportCrmCreditDepart(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "自动ID",
                    "额度类型ID",
                    "额度类型名称",
                    "法人主体id",
                    "法人主体名称",
                    "额度类型值(1:税费额度,2:期票额度,3:贷款额度)",
                    "授信金额",
                    "租户编码",
                    "备注",
                    "是否删除，0未删除，1已删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCreditDepartService.queryCrmCreditDepartForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_额度_部门额度授信管理(crm_credit_depart)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
