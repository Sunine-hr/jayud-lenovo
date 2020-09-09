package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.model.bo.AddCustomerInfoForm;
import com.jayud.model.bo.QueryContractInfoForm;
import com.jayud.model.enums.CustomerInfoStatusEnum;
import com.jayud.model.po.ContractInfo;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.vo.AddContractInfoRelListVO;
import com.jayud.model.vo.ContractInfoVO;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.service.IContractInfoService;
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
 * 合同管理 前端控制器
 */
@RestController
@RequestMapping("/contractInfo")
@Api(tags = "客户管理-合同管理")
public class ContractInfoController {

    @Autowired
    private IContractInfoService contractInfoService;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private ICustomerInfoService customerInfoService;

    @ApiOperation(value = "查询合同列表")
    @PostMapping(value = "/findContractInfoByPage")
    public CommonResult<CommonPageResult<ContractInfoVO>> findCustomerInfoByPage(@RequestBody QueryContractInfoForm form) {
        IPage<ContractInfoVO> pageList = contractInfoService.findContractInfoByPage(form);
        CommonPageResult<ContractInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑时数据回显")
    @PostMapping(value = "/getContractInfoById")
    public CommonResult<ContractInfoVO> getContractInfoById(@RequestBody QueryContractInfoForm form) {
        return CommonResult.success(contractInfoService.getContractInfoById(form));
    }

    @ApiOperation(value = "新增编辑合同")
    @PostMapping(value = "/saveOrUpdateContractInfo")
    public CommonResult saveOrUpdateCustomerInfo(@RequestBody AddCustomerInfoForm form) {
        ContractInfo contractInfo = ConvertUtil.convert(form,ContractInfo.class);
        if(form.getId() != null){
            contractInfo.setUpdatedUser(getLoginUser());
            contractInfo.setUpdatedTime(DateUtils.getNowTime());
        }else {
            contractInfo.setCreatedUser(getLoginUser());
        }
        contractInfoService.saveOrUpdate(contractInfo);
        return CommonResult.success();
    }

    @ApiOperation(value = "新增合同时初始化下拉框信息")
    @PostMapping(value = "/getInfoBySave")
    public CommonResult<AddContractInfoRelListVO> getInfoBySave() {
        AddContractInfoRelListVO addContractInfoRelListVO = new AddContractInfoRelListVO();
        Map<String,String> param = new HashMap<String,String>();
        param.put("audit_status", CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());//有效的，审核通过的客户名称
        param.put("status", "1");
        List<CustomerInfo> customerInfos = customerInfoService.findCustomerInfoByCondition(param);
        List<Map<String,String>> mapList1 = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfos) {
            param = new HashMap<String,String>();
            param.put("id",String.valueOf(customerInfo.getId()));
            param.put("name",customerInfo.getName());
            mapList1.add(param);
        }
        addContractInfoRelListVO.setCustomerInfos(mapList1);
        addContractInfoRelListVO.setLegalEntitys((List<Map<String,String>>)oauthClient.findLegalEntity().getData());//获取法人主体
        return CommonResult.success(addContractInfoRelListVO);
    }

    @ApiOperation(value = "删除合同信息")
    @PostMapping(value = "/delContractInfo")
    public CommonResult delContractInfo(@RequestBody List<Integer> ids) {
        List<ContractInfo> contractInfos = new ArrayList<>();
        for (Integer id : ids) {
            ContractInfo contractInfo = new ContractInfo();
            contractInfo.setId(id);
            contractInfo.setUpdatedTime(DateUtils.getNowTime());
            contractInfo.setUpdatedUser(getLoginUser());
            contractInfo.setStatus("0");
        }
        contractInfoService.saveOrUpdateBatch(contractInfos);
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

