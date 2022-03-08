package com.jayud.crm.controller;

import com.jayud.crm.model.bo.CrmCustomerRelationsForm;
import com.jayud.crm.model.bo.DeleteForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerRelationsService;
import com.jayud.crm.model.po.CrmCustomerRelations;

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
 * 基本档案_客户_联系人(crm_customer_relations) 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_客户_联系人(crm_customer_relations)")
@RestController
@RequestMapping("/crmCustomerRelations")
public class CrmCustomerRelationsController {



    @Autowired
    public ICrmCustomerRelationsService crmCustomerRelationsService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerRelations
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerRelations>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerRelations>> selectPage(CrmCustomerRelations crmCustomerRelations,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCustomerRelationsService.selectPage(crmCustomerRelations,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerRelations
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerRelations>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerRelations>> selectList(CrmCustomerRelations crmCustomerRelations,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerRelationsService.selectList(crmCustomerRelations));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerRelations
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@RequestBody CrmCustomerRelationsForm crmCustomerRelationsForm ){
        crmCustomerRelationsForm.checkParam();
        return crmCustomerRelationsService.saveOrUpdateCrmCustomerRelations(crmCustomerRelationsForm);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: crmCustomerRelations
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmCustomerRelations crmCustomerRelations ){
//        crmCustomerRelationsService.updateById(crmCustomerRelations);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }
//


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
//        crmCustomerRelationsService.phyDelById(id);
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
        crmCustomerRelationsService.logicDel(ids.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerRelations>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerRelations> queryById(@RequestParam(name="id",required=true) Long id) {
        CrmCustomerRelations crmCustomerRelations = crmCustomerRelationsService.getById(id);
        return BaseResult.ok(crmCustomerRelations);
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
    @ApiOperation("根据查询条件导出基本档案_客户_联系人(crm_customer_relations)")
    @PostMapping(path = "/exportCrmCustomerRelations")
    public void exportCrmCustomerRelations(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "客户ID",
                "联系人类型",
                "是否默认联系人",
                "姓名",
                "证件类型",
                "证件号码",
                "电话",
                "手机",
                "邮箱",
                "地址",
                "生日",
                "岗位名称",
                "持股比例",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerRelationsService.queryCrmCustomerRelationsForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_联系人(crm_customer_relations)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
