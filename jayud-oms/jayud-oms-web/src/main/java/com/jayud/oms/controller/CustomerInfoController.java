package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.model.bo.*;
import com.jayud.model.enums.CustomerInfoStatusEnum;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.AddCustomerAccountRelListVO;
import com.jayud.model.vo.CustAccountVO;
import com.jayud.model.vo.CustomerInfoVO;
import com.jayud.model.vo.InitComboxVO;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.service.ICustomerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户管理 前端控制器
 */
@RestController
@RequestMapping("/customerInfo")
@Api(tags = "客户管理")
public class CustomerInfoController {

    @Autowired
    private ICustomerInfoService customerInfoService;

    @Autowired
    private OauthClient oauthClient;

    @ApiOperation(value = "查询客户列表")
    @PostMapping(value = "/findCustomerInfoByPage")
    public CommonResult<CommonPageResult<CustomerInfoVO>> findCustomerInfoByPage(@RequestBody QueryCustomerInfoForm form) {
        IPage<CustomerInfoVO> pageList = customerInfoService.findCustomerInfoByPage(form);
        CommonPageResult<CustomerInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查看客户详情和编辑时数据回显,id=客户ID")
    @PostMapping(value = "/getCustomerInfoById")
    public CommonResult<CustomerInfoVO> getCustomerInfoById(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        return CommonResult.success(customerInfoService.getCustomerInfoById(Long.valueOf(id)));
    }

    @ApiOperation(value = "新增编辑客户")
    @PostMapping(value = "/saveOrUpdateCustomerInfo")
    public CommonResult saveOrUpdateCustomerInfo(@RequestBody AddCustomerInfoForm form) {
        CustomerInfo customerInfo = null;
        customerInfo = ConvertUtil.convert(form,CustomerInfo.class);
        if(form.getId() != null){
            customerInfo.setUpdatedUser(getLoginUser());
            customerInfo.setUpdatedTime(DateUtils.getNowTime());
        }else {
            customerInfo.setCreatedUser(getLoginUser());
        }
        customerInfoService.saveOrUpdate(customerInfo);
        return CommonResult.success();
    }

    @ApiOperation(value = "删除客户信息")
    @PostMapping(value = "/delCustomerInfo")
    public CommonResult delCustomerInfo(@RequestBody List<Integer> ids) {
        List<CustomerInfo> customerInfos = new ArrayList<>();
        for (Integer id : ids) {
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setId(id);
            customerInfo.setUpdatedTime(DateUtils.getNowTime());
            customerInfo.setUpdatedUser(getLoginUser());
            customerInfo.setStatus("0");
        }
        customerInfoService.saveOrUpdateBatch(customerInfos);
        return CommonResult.success();
    }

    @ApiOperation(value = "审核客户信息")
    @PostMapping(value = "/auditCustomerInfo")
    public CommonResult auditCustomerInfo(@RequestBody AuditCustomerInfoForm form) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(form.getId());
        customerInfo.setUpdatedTime(DateUtils.getNowTime());
        customerInfo.setUpdatedUser(getLoginUser());
        if("0".equals(form.getAuditStatus())){//审核拒绝
            customerInfo.setAuditStatus(CustomerInfoStatusEnum.AUDIT_FAIL.getCode());
            customerInfo.setAuditComment(form.getAuditComment());
        }else if("1".equals(form.getAuditStatus())){//审核状态
            if("kf".equals(form.getRoleFlag())){
                customerInfo.setAuditStatus(CustomerInfoStatusEnum.CW_WAIT_AUDIT.getCode());
            }else if("cw".equals(form.getRoleFlag())){
                customerInfo.setAuditStatus(CustomerInfoStatusEnum.ZJB_WAIT_AUDIT.getCode());
            }else if("zjb".equals(form.getRoleFlag())){
                customerInfo.setAuditStatus(CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
            }
        }
        customerInfoService.updateById(customerInfo);
        return CommonResult.success();
    }

    @ApiOperation(value = "客户账号管理-新增时数据回显")
    @PostMapping(value = "/findAddCustomerAccount")
    public CommonResult<AddCustomerAccountRelListVO> findAddCustomerAccount() {
        AddCustomerAccountRelListVO accountRelListVO = new AddCustomerAccountRelListVO();
        accountRelListVO.setDepartCharges((List<Map<Long,String>>)oauthClient.findCustAccount());//获取客户类型相同公司下的账户
        List<CustomerInfo> customerInfos = customerInfoService.findCustomerInfoByCondition(null);
        List<Map<Integer,String>> strList = new ArrayList<>();
        for (CustomerInfo cus : customerInfos) {
            Map<Integer,String> strMap = new HashMap<>();
            strMap.put(cus.getId(),cus.getName());
            strList.add(strMap);
        }
        accountRelListVO.setCustomerInfos(strList);//获取客户信息
        accountRelListVO.setRoles((List<Map<Long,String>>)oauthClient.findRole());
        return CommonResult.success(accountRelListVO);
    }

    @ApiOperation(value = "客户账号管理-修改时数据回显")
    @PostMapping(value = "/getCustomerAccountInfo")
    public CommonResult<CustAccountVO> getCustomerAccountInfo(Long id) {
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        CustAccountVO custAccountVO = customerInfoService.getCustAccountByCondition(param);
        return CommonResult.success(custAccountVO);
    }

    @ApiOperation(value = "客户账号管理-删除")
    @PostMapping(value = "/delCustomerAccountInfo")
    public CommonResult delCustomerAccountInfo(Long id) {
        oauthClient.delCustAccount(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "客户账号管理-修改/编辑")
    @PostMapping(value = "/saveOrUpdateCusAccountInfo")
    public CommonResult saveOrUpdateCusAccountInfo(AddCusAccountForm form) {
        oauthClient.saveOrUpdateCustAccount(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "客户账号管理-分页获取数据")
    @PostMapping(value = "/findCusAccountByPage")
    public CommonResult findCusAccountByPage(QueryCusAccountForm form) {
        customerInfoService.findCustAccountByPage(form);
        return CommonResult.success();
    }

    /**
     * 所有下拉框的初始化
     */
    @ApiOperation(value = "客户列表-新增-接单部门")
    @PostMapping(value = "/initDepartment")
    public CommonResult<List<InitComboxVO>> initDepartment() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findDepartment().getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "客户列表-新增-接单客服")
    @PostMapping(value = "/initKfs")
    public CommonResult<List<InitComboxVO>> initKfs() {
        List<InitComboxVO> initComboxVOS = null;// (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.CUSTOMER_SERVICE.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "客户列表-新增-业务员")
    @PostMapping(value = "/initYws")
    public CommonResult<List<InitComboxVO>> initYws() {
        List<InitComboxVO> initComboxVOS = null;//(List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.BUSINESS_MANAGER.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        ApiResult apiResult = oauthClient.getLoginUser();
        return apiResult.getData().toString();
    }

}

