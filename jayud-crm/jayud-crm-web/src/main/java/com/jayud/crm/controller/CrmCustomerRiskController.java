package com.jayud.crm.controller;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.bo.CrmCustomerRiskForm;
import com.jayud.crm.model.bo.DeleteForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.bo.CrmCodeFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerRiskService;
import com.jayud.crm.model.po.CrmCustomerRisk;

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
 * 基本档案_客户_风险客户（crm_customer_risk） 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_客户_风险客户（crm_customer_risk）")
@RestController
@RequestMapping("/crmCustomerRisk")
public class CrmCustomerRiskController {



    @Autowired
    public ICrmCustomerRiskService crmCustomerRiskService;

    @Autowired
    private SysDictClient sysDictClient;
    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerRisk
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerRisk>>
     **/
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerRisk>> selectPage(@RequestBody CrmCustomerRiskForm crmCustomerRiskForm,
                                                         HttpServletRequest req) {
        return BaseResult.ok(crmCustomerRiskService.selectPage(crmCustomerRiskForm,crmCustomerRiskForm.getCurrentPage(),crmCustomerRiskForm.getPageSize(),req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerRisk
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerRisk>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerRisk>> selectList(CrmCustomerRisk crmCustomerRisk,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerRiskService.selectList(crmCustomerRisk));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerRisk
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerRiskForm crmCustomerRiskForm ){
        return  crmCustomerRiskService.saveOrUpdateCrmCustomerRisk(crmCustomerRiskForm);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: crmCustomerRisk
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmCustomerRisk crmCustomerRisk ){
//        crmCustomerRiskService.updateById(crmCustomerRisk);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }



//    /**
//     * @description 物理删除
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id){
//        crmCustomerRiskService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm ids){
        crmCustomerRiskService.logicDel(ids.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerRisk>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerRisk> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerRisk crmCustomerRisk = crmCustomerRiskService.getById(id);
        return BaseResult.ok(crmCustomerRisk);
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
    @ApiOperation("根据查询条件导出基本档案_客户_风险客户（crm_customer_risk）")
    @PostMapping(path = "/exportCrmCustomerRisk")
    public void exportCrmCustomerRisk(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "客户ID",
                "客户名称",
                "风险类型",
                "经营范围",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间",
                "统一信用代码"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerRiskService.queryCrmCustomerRiskForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_风险客户（crm_customer_risk）", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }
    /**
     * @description 获取客户信息字典项目
     * @author  ciro
     * @date   2022/3/2 11:07
     * @param:
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.bo.CrmCodeFrom>
     **/
    @ApiOperation("获取风控管理类型字典下拉")
    @GetMapping(path = "/getCrmCustomerRiskCode")
    public BaseResult  getCrmCode(){
        BaseResult<List<SysDictItem>> custNormalStatus= sysDictClient.selectItemByDictCode(CrmDictCode.CRM_CUSTOMER_RISK_TYPE);
        return BaseResult.ok(custNormalStatus.getResult());
    }



}
