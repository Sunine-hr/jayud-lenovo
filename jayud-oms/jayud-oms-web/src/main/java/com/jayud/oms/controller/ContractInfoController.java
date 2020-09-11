package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.model.bo.AddContractInfoForm;
import com.jayud.model.bo.DeleteForm;
import com.jayud.model.bo.QueryContractInfoForm;
import com.jayud.model.enums.CustomerInfoStatusEnum;
import com.jayud.model.po.ContractInfo;
import com.jayud.model.po.CustomerInfo;
import com.jayud.model.po.ProductBiz;
import com.jayud.model.vo.ContractInfoVO;
import com.jayud.model.vo.InitComboxVO;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.service.IContractInfoService;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.IProductBizService;
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

    @Autowired
    private IProductBizService productBizService;

    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation(value = "查询合同列表")
    @PostMapping(value = "/findContractInfoByPage")
    public CommonResult<CommonPageResult<ContractInfoVO>> findCustomerInfoByPage(@RequestBody QueryContractInfoForm form) {
        IPage<ContractInfoVO> pageList = contractInfoService.findContractInfoByPage(form);
        CommonPageResult<ContractInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑时数据回显,id=合同ID")
    @PostMapping(value = "/getContractInfoById")
    public CommonResult<ContractInfoVO> getContractInfoById(@RequestBody Map<String,Object> param) {
        String id = MapUtil.getStr(param,"id");
        return CommonResult.success(contractInfoService.getContractInfoById(Long.parseLong(id)));
    }

    @ApiOperation(value = "新增编辑合同")
    @PostMapping(value = "/saveOrUpdateContractInfo")
    public CommonResult saveOrUpdateCustomerInfo(@RequestBody AddContractInfoForm form) {
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

    @ApiOperation(value = "删除合同信息")
    @PostMapping(value = "/delContractInfo")
    public CommonResult delContractInfo(@RequestBody DeleteForm form) {
        List<ContractInfo> contractInfos = new ArrayList<>();
        for (Long id : form.getIds()) {
            ContractInfo contractInfo = new ContractInfo();
            contractInfo.setId(id);
            contractInfo.setUpdatedTime(DateUtils.getNowTime());
            contractInfo.setUpdatedUser(getLoginUser());
            contractInfo.setStatus("0");
            contractInfos.add(contractInfo);
        }
        contractInfoService.saveOrUpdateBatch(contractInfos);
        return CommonResult.success();
    }
    @ApiOperation(value = "合同管理-下拉框合并返回")
    @PostMapping(value = "/findComboxs3")
    public CommonResult<Map<String,Object>> findComboxs3(){
        Map<String,Object> resultMap = new HashMap<>();
        //客户名称
        resultMap.put("customers",initCustomer());
        //业务类型
        resultMap.put("productBiz",initProductBiz());
        //法人主体
        resultMap.put("legalEntity",initLegalEntity());
        return CommonResult.success(resultMap);

    }

    /**
     * 初始化下拉框
     */
    @ApiOperation(value = "合同管理-下拉框-客户名称")
    @PostMapping(value = "/initCustomer")
    public CommonResult<List<InitComboxVO>> initCustomer() {
        Map<String,String> param = new HashMap<String,String>();
        param.put("audit_status", CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());//有效的，审核通过的客户名称
        param.put("status", "1");
        List<CustomerInfo> customerInfos = customerInfoService.findCustomerInfoByCondition(param);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(customerInfo.getId());
            initComboxVO.setName(customerInfo.getName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "合同管理-下拉框-法人主体")
    @PostMapping(value = "/initLegalEntity")
    public CommonResult<List<InitComboxVO>> initLegalEntity() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>)oauthClient.findLegalEntity().getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "合同管理-下拉框-业务类型")
    @PostMapping(value = "/initProductBiz")
    public CommonResult<List<InitComboxVO>> initProductBiz() {
        List<ProductBiz> productBizs = productBizService.findProductBiz();
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (ProductBiz productBiz : productBizs) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(productBiz.getId());
            initComboxVO.setName(productBiz.getName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }


    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        String loginUser = redisUtils.get("loginUser");
        return loginUser;
    }

}

