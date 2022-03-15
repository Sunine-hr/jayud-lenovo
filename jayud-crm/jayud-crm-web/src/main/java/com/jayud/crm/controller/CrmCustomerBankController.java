package com.jayud.crm.controller;

import com.jayud.crm.model.bo.DeleteForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerBankService;
import com.jayud.crm.model.po.CrmCustomerBank;

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
 * 基本档案_客户_银行账户(crm_customer_bank) 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_客户_银行账户(crm_customer_bank)")
@RestController
@RequestMapping("/crmCustomerBank")
public class CrmCustomerBankController {



    @Autowired
    public ICrmCustomerBankService crmCustomerBankService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerBank
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerBank>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerBank>> selectPage(CrmCustomerBank crmCustomerBank,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCustomerBankService.selectPage(crmCustomerBank,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerBank
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerBank>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerBank>> selectList(CrmCustomerBank crmCustomerBank,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerBankService.selectList(crmCustomerBank));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerBank
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerBank crmCustomerBank ){
        return crmCustomerBankService.saveBank(crmCustomerBank);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerBank
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCustomerBank crmCustomerBank ){
        return crmCustomerBankService.saveBank(crmCustomerBank);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        crmCustomerBankService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    @ApiOperation("逻辑删除")
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm form){
        crmCustomerBankService.logicDelByIds(form.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    @ApiOperation("根据id批量逻辑删除")
    @PostMapping("/logicDelByIds")
    public BaseResult logicDelByIds(@RequestBody DeleteForm form){
        crmCustomerBankService.logicDelByIds(form.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerBank>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerBank> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerBank crmCustomerBank = crmCustomerBankService.getById(id);
        return BaseResult.ok(crmCustomerBank);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-02
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出基本档案_客户_银行账户(crm_customer_bank)")
    @PostMapping(path = "/exportCrmCustomerBank")
    public void exportCrmCustomerBank(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "客户ID",
                "银行名称",
                "银行地址",
                "银行代码",
                "账户名称",
                "银行账号",
                "开户行",
                "账户币别",
                "国际联行号",
                "是否默认账户",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerBankService.queryCrmCustomerBankForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_银行账户(crm_customer_bank)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
