package com.jayud.crm.controller;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.bo.CrmCustomerFollowForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.vo.CrmCustomerFollowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerFollowService;
import com.jayud.crm.model.po.CrmCustomerFollow;

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
 * 基本档案_客户_跟进记录(crm_customer_follow) 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_客户_跟进记录(crm_customer_follow)")
@RestController
@RequestMapping("/crmCustomerFollow")
public class CrmCustomerFollowController {



    @Autowired
    public ICrmCustomerFollowService crmCustomerFollowService;

    @Autowired
    private SysDictClient sysDictClient;
    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerFollow
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerFollow>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCustomerFollowVO>> selectPage(CrmCustomerFollow crmCustomerFollow,
                                                             @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                             HttpServletRequest req) {
        return BaseResult.ok(crmCustomerFollowService.selectPage(crmCustomerFollow,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerFollow
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCustomerFollow>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerFollow>> selectList(CrmCustomerFollow crmCustomerFollow,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCustomerFollowService.selectList(crmCustomerFollow));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCustomerFollow
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCustomerFollowForm crmCustomerFollowForm ){

        return crmCustomerFollowService.saveOrUpdateCrmCustomerFollow(crmCustomerFollowForm);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: crmCustomerFollow
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmCustomerFollow crmCustomerFollow ){
//        crmCustomerFollowService.updateById(crmCustomerFollow);
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
//        crmCustomerFollowService.phyDelById(id);
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
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        crmCustomerFollowService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerFollow>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerFollow> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCustomerFollow crmCustomerFollow = crmCustomerFollowService.getById(id);
//        crmCustomerFollowService.findCrmCustomerFollowById(id);
        return BaseResult.ok(crmCustomerFollow);
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
    @ApiOperation("根据查询条件导出基本档案_客户_跟进记录(crm_customer_follow)")
    @PostMapping(path = "/exportCrmCustomerFollow")
    public void exportCrmCustomerFollow(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "",
                "客户ID",
                "联系人",
                "联系人id",
                "记录类型",
                "记录内容",
                "租户编码",
                "文件上传url",
                "联系时间",
                "备注",
                "是否删除，0未删除，1已删除",
                "创建人",
                "创建时间",
                "更新人",
                "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerFollowService.queryCrmCustomerFollowForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_客户_跟进记录(crm_customer_follow)", response);
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
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.form.CrmCodeFrom>
     **/
    @ApiOperation("获取跟进记录方式字典下拉")
    @GetMapping(path = "/getCrmCustomerRiskCode")
    public BaseResult  getCrmCode(){
        BaseResult<List<SysDictItem>> custNormalStatus= sysDictClient.selectItemByDictCode(CrmDictCode.CRM_CUSTOMER_FOLLOW_TYPE);
        return BaseResult.ok(custNormalStatus.getResult());
    }

}
