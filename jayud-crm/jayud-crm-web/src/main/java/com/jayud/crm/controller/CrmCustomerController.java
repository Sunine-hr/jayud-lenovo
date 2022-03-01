package com.jayud.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerService;
import com.jayud.crm.model.po.CrmCustomer;

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
 * 基本档案_客户_基本信息(crm_customer) 控制类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Api(tags = "基本档案_客户_基本信息(crm_customer)")
@RestController
@RequestMapping("/crmCustomer")
public class CrmCustomerController {



    @Autowired
    public ICrmCustomerService crmCustomerService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: crmCustomer
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomer>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomer>> selectPage(CrmCustomer crmCustomer,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCustomerService.selectPage(crmCustomer,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-01
    * @param: crmCustomer
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomer>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomer>> selectList(CrmCustomer crmCustomer,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerService.selectList(crmCustomer));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-01
    * @param: crmCustomer
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomer crmCustomer ){
        crmCustomerService.save(crmCustomer);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-01
     * @param: crmCustomer
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCustomer crmCustomer ){
        crmCustomerService.updateById(crmCustomer);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        crmCustomerService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        crmCustomerService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomer>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomer> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomer crmCustomer = crmCustomerService.getById(id);
        return BaseResult.ok(crmCustomer);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-01
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出基本档案_客户_基本信息(crm_customer)")
    @PostMapping(path = "/exportCrmCustomer")
    public void exportCrmCustomer(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键ID",
                "(母公司ID)",
                "公司名称",
                "统一信用代码",
                "企业跟进状态(潜在，意向，合作，暂不合作）",
                "是否普通用户",
                "是否公海客户",
                "是否供应商",
                "简称",
                "英文名称",
                "企业类型(融资客户，账期客户，现结客户）",
                "客户等级(A,AA,AAA等）",
                "行业类型",
                "国家",
                "省",
                "城市",
                "县、区",
                "区域(华南，华北，西南等)",
                "注册时间",
                "注册地址",
                "注册地址英文",
                "注册电话",
                "企来类型(有限责任公司,上市公司，国有企业等)",
                "登记注册号",
                "组织机构代码",
                "有效期截止",
                "法定代表人姓名",
                "实际控制人",
                "是否一般纳税人",
                "注册资本（万元）",
                "实缴资本（万元）",
                "注册币别",
                "经营范围",
                "注册机构",
                "海关代码",
                "商检代码",
                "客户来源",
                "目前合作方",
                "第一单日期",
                "业务ID",
                "业务员名称",
                "商务ID",
                "商务名称",
                "在线系统ID",
                "在线系统编号",
                "是否同步",
                "审核级别",
                "当前级别",
                "审核状态",
                "曾用名",
                "公司编码",
                "供应商编码",
                "业务类型",
                "服务类型",
                "帐期",
                "对账方式",
                "结算方式",
                "所属行业",
                "网址地址",
                "企业信用",
                "进口信用",
                "海关信用",
                "租户编码",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerService.queryCrmCustomerForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_基本信息(crm_customer)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
