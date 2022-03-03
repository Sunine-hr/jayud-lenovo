package com.jayud.crm.controller;

import com.jayud.crm.model.bo.CrmCustomerFeaturesForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerFeaturesService;
import com.jayud.crm.model.po.CrmCustomerFeatures;

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
 * 基本档案_客户_业务特征 控制类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Api(tags = "基本档案_客户_业务特征")
@RestController
@RequestMapping("/crmCustomerFeatures")
public class CrmCustomerFeaturesController {



    @Autowired
    public ICrmCustomerFeaturesService crmCustomerFeaturesService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerFeatures
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerFeatures>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerFeatures>> selectPage(CrmCustomerFeatures crmCustomerFeatures,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCustomerFeaturesService.selectPage(crmCustomerFeatures,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerFeatures
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerFeatures>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerFeatures>> selectList(CrmCustomerFeatures crmCustomerFeatures,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerFeaturesService.selectList(crmCustomerFeatures));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerFeatures
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增编辑")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerFeaturesForm crmCustomerFeaturesForm ){
        crmCustomerFeaturesService.saveOrUpdateCrmCustomerFeatures(crmCustomerFeaturesForm);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-03
//     * @param: crmCustomerFeatures
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmCustomerFeatures crmCustomerFeatures ){
//        crmCustomerFeaturesService.updateById(crmCustomerFeatures);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        crmCustomerFeaturesService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        crmCustomerFeaturesService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerFeatures>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerFeatures> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerFeatures crmCustomerFeatures = crmCustomerFeaturesService.getById(id);
        return BaseResult.ok(crmCustomerFeatures);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-03
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出基本档案_客户_业务特征")
    @PostMapping(path = "/exportCrmCustomerFeatures")
    public void exportCrmCustomerFeatures(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "",
                "客户ID",
                "特征类型",
                "特征内容",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerFeaturesService.queryCrmCustomerFeaturesForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_业务特征", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
