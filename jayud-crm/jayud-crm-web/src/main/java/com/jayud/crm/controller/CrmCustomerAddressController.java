package com.jayud.crm.controller;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.bo.CrmCustomerAddressForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.form.CrmCodeFollowForm;
import com.jayud.crm.model.po.CrmCustomerRelations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerAddressService;
import com.jayud.crm.model.po.CrmCustomerAddress;

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
 * 基本档案_客户_地址 控制类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Api(tags = "基本档案_客户_地址")
@RestController
@RequestMapping("/crmCustomerAddress")
public class CrmCustomerAddressController {



    @Autowired
    public ICrmCustomerAddressService crmCustomerAddressService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private SysDictClient sysDictClient;

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAddress
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerAddress>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerAddress>> selectPage(CrmCustomerAddress crmCustomerAddress,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCustomerAddressService.selectPage(crmCustomerAddress,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerAddress
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerAddress>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerAddress>> selectList(CrmCustomerAddress crmCustomerAddress,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerAddressService.selectList(crmCustomerAddress));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-03
    * @param: crmCustomerAddress
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerAddressForm crmCustomerAddressForm ){
        ;
        return crmCustomerAddressService.saveOrUpdateCrmCustomerAddress(crmCustomerAddressForm);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAddress
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCustomerAddress crmCustomerAddress ){
        crmCustomerAddressService.updateById(crmCustomerAddress);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
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
        crmCustomerAddressService.phyDelById(id);
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
        crmCustomerAddressService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerAddress>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerAddress> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerAddress crmCustomerAddress = crmCustomerAddressService.getById(id);
        return BaseResult.ok(crmCustomerAddress);
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
    @ApiOperation("根据查询条件导出基本档案_客户_地址")
    @PostMapping(path = "/exportCrmCustomerAddress")
    public void exportCrmCustomerAddress(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "客户ID",
                "地址类型",
                "国家",
                "省",
                "市",
                "区",
                "详细地址",
                "区域(市区，关外等)",
                "联系人",
                "联系电话",
                "手机",
                "邮箱",
                "收货公司",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerAddressService.queryCrmCustomerAddressForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_地址", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }
    @ApiOperation("根据条件省市级联")
    @GetMapping(value="/saveTroubleCascade")
    public BaseResult saveTroubleCascade(@RequestParam(name="level",required=false) Integer level,@RequestParam(name="parentCode",required=false) Long parentCode){
        BaseResult baseResult = authClient.selectListSysAreaFeign(level,parentCode);
        return BaseResult.ok(baseResult);
    }



    /**
     * @description 获取客户信息字典项目
     * @author  ciro
     * @date   2022/3/2 11:07
     * @param:
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.form.CrmCodeFrom>
     **/
    @ApiOperation("获取客户地址类型字典下拉")
    @GetMapping(path = "/getCrmCustomerAddressCode")
    public BaseResult  getCrmCode(){
        BaseResult<List<SysDictItem>> custNormalStatus= sysDictClient.selectItemByDictCode(CrmDictCode.CRM_CUSTOMER_ADDRESS_TYPE);
        return BaseResult.ok(custNormalStatus);
    }

}
