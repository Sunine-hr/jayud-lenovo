package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.model.bo.AddCustomerInfoForm;
import com.jayud.model.bo.AuditCustomerInfoForm;
import com.jayud.model.bo.QueryCustomerInfoForm;
import com.jayud.model.enums.CustomerInfoStatusEnum;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.AddCustomerInfoRelListVO;
import com.jayud.model.vo.CustomerInfoVO;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.service.ICustomerInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户管理 前端控制器
 */
@RestController
@RequestMapping("/customerInfo")
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


    @ApiOperation(value = "查看客户详情，编辑时数据回显")
    @PostMapping(value = "/getCustomerInfoById")
    public CommonResult<CustomerInfoVO> getCustomerInfoById(@RequestBody QueryCustomerInfoForm form) {
        return CommonResult.success(customerInfoService.getCustomerInfoById(form));
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

    @ApiOperation(value = "新增客户时初始化下拉框信息")
    @PostMapping(value = "/getInfoBySave")
    public CommonResult<AddCustomerInfoRelListVO> getInfoBySave() {
        AddCustomerInfoRelListVO addCustomerInfoRelListVO = new AddCustomerInfoRelListVO();
        List<Map<Long,String>> departments = (List<Map<Long,String>>)oauthClient.findDepartment().getData();
        addCustomerInfoRelListVO.setDepartments(departments);
        List<Map<Long,String>> kfs = (List<Map<Long,String>>)oauthClient.findUserByRoleId(1L);//客服
        List<Map<Long,String>> yws = (List<Map<Long,String>>)oauthClient.findUserByRoleId(2L);//业务
        addCustomerInfoRelListVO.setKfs(kfs);
        addCustomerInfoRelListVO.setYws(yws);
        return CommonResult.success(addCustomerInfoRelListVO);
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

    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        ApiResult apiResult = oauthClient.getLoginUser();
        return apiResult.getData().toString();
    }

}

