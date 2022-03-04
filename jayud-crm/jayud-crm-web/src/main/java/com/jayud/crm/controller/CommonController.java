package com.jayud.crm.controller;


import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.TreeNode;
import com.jayud.common.utils.TreeUtil;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.OmsClient;
import com.jayud.crm.feign.SysDictClient;
import com.jayud.crm.model.constant.CrmDictCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private SysDictClient sysDictClient;

    /**
     * 币种
     *
     * @return
     */
    @ApiOperation(value = "币种")
    @PostMapping(value = "/initCostUnit")
    public CommonResult<List<InitComboxStrVO>> initCurrencyInfo() {
        return CommonResult.success(omsClient.initCurrencyInfo().getData());
    }


    /**
     * @description 查询法人主体
     **/
    @ApiOperation("查询法人主体")
    @GetMapping("/getLegalEntity")
    public BaseResult<List<SysDepart>> getLegalEntity() {
        return authClient.getLegalEntity();
    }


    /**
     * @description 获取业务类型
     **/
    @ApiOperation("获取业务类型")
    @RequestMapping("/getBusinessType")
    public BaseResult<List<SysDictItem>> getBusinessType() {
        //客户管理-业务类型
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CUST_BUSINESS_TYPE);
        return custBusinessType;
    }

    @ApiOperation(value = "获取省市区树结构")
    @PostMapping(value = "/adrrTree")
    public CommonResult<List<TreeNode>> adrrTree() {
        return CommonResult.success(this.omsClient.adrrTree().getData());
    }

    /**
     * @description 获取额度类型
     **/
    @ApiOperation("获取额度类型")
    @RequestMapping("/getQuotaType")
    public BaseResult<List<SysDictItem>> getQuotaType() {
        BaseResult<List<SysDictItem>> custBusinessType = sysDictClient.selectItemByDictCode(CrmDictCode.CRM_CREDIT_TYPE);
        return custBusinessType;
    }

}

