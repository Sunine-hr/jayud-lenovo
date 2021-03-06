package com.jayud.crm.controller;

import com.jayud.crm.model.bo.CrmCustomerTaxForm;
import com.jayud.crm.model.bo.DeleteForm;
import com.jayud.crm.model.vo.CrmCustomerTaxVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerTaxService;
import com.jayud.crm.model.po.CrmCustomerTax;

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
 * 开票资料 控制类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Api(tags = "开票资料")
@RestController
@RequestMapping("/crmCustomerTax")
public class CrmCustomerTaxController {



    @Autowired
    public ICrmCustomerTaxService crmCustomerTaxService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerTax
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerTax>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerTaxVO>> selectPage(CrmCustomerTax crmCustomerTax,
                                                          @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                          HttpServletRequest req) {
        return BaseResult.ok(crmCustomerTaxService.selectPage(crmCustomerTax,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerTax
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerTax>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerTaxVO>> selectList(CrmCustomerTax crmCustomerTax,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerTaxService.selectList(crmCustomerTax));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerTax
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerTaxForm crmCustomerTaxForm ){
        crmCustomerTaxService.saveOrUpdateCrmCustomerTax(crmCustomerTaxForm);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-03
//     * @param: crmCustomerTax
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmCustomerTax crmCustomerTax ){
//        crmCustomerTaxService.updateById(crmCustomerTax);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }



//    /**
//     * @description 物理删除
//     * @author  jayud
//     * @date   2022-03-03
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id){
//        crmCustomerTaxService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm ids){
        crmCustomerTaxService.logicDel(ids.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerTax>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerTax> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerTax crmCustomerTax = crmCustomerTaxService.getById(id);
        return BaseResult.ok(crmCustomerTax);
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
    @ApiOperation("根据查询条件导出开票资料")
    @PostMapping(path = "/exportCrmCustomerTax")
    public void exportCrmCustomerTax(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "客户ID",
                "税号",
                "名称",
                "地址电话",
                "银行及账号",
                "是否生效账户 ：0 生效 1 不生效",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerTaxService.queryCrmCustomerTaxForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "开票资料", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
