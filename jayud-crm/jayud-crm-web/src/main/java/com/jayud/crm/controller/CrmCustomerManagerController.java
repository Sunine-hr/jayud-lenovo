package com.jayud.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerManagerService;
import com.jayud.crm.model.po.CrmCustomerManager;

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
 * 基本档案_客户_客户维护人(crm_customer_manager) 控制类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Api(tags = "基本档案_客户_客户维护人(crm_customer_manager)")
@RestController
@RequestMapping("/crmCustomerManager")
public class CrmCustomerManagerController {



    @Autowired
    public ICrmCustomerManagerService crmCustomerManagerService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerManager
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerManager>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerManager>> selectPage(CrmCustomerManager crmCustomerManager,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCustomerManagerService.selectPage(crmCustomerManager,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerManager
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerManager>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerManager>> selectList(CrmCustomerManager crmCustomerManager,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerManagerService.selectList(crmCustomerManager));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerManager
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerManager crmCustomerManager ){
        return crmCustomerManagerService.saveManager(crmCustomerManager);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerManager
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCustomerManager crmCustomerManager ){
        return crmCustomerManagerService.saveManager(crmCustomerManager);
    }



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
        crmCustomerManagerService.phyDelById(id);
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
        crmCustomerManagerService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerManager>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerManager> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerManager crmCustomerManager = crmCustomerManagerService.getById(id);
        return BaseResult.ok(crmCustomerManager);
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
    @ApiOperation("根据查询条件导出基本档案_客户_客户维护人(crm_customer_manager)")
    @PostMapping(path = "/exportCrmCustomerManager")
    public void exportCrmCustomerManager(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "客户ID",
                "对接用户id",
                "对接用户名称",
                "对接用户角色编码",
                "对接用户角色名称",
                "归属时间",
                "业务类型编码",
                "业务类型名称",
                "是否负责人，0否，1是",
                "是否销售人员，0否，1是",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerManagerService.queryCrmCustomerManagerForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_客户维护人(crm_customer_manager)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
