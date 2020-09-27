package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.enums.RoleKeyEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.CurrencyInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/combox")
@Api(tags = "初始化下拉框接口")
public class OrderComboxController {

    @Autowired
    ICustomerInfoService customerInfoService;

    @Autowired
    OauthClient oauthClient;

    @Autowired
    IContractInfoService contractInfoService;

    @Autowired
    IPortInfoService portInfoService;

    @Autowired
    IProductClassifyService productClassifyService;

    @Autowired
    ICostInfoService costInfoService;

    @Autowired
    ICurrencyInfoService currencyInfoService;

    @Autowired
    ICostTypeService costTypeService;

    @ApiOperation(value = "纯报关-客户,业务员,合同,业务所属部门,通关口岸")
    @PostMapping(value = "/initCombox1")
    public CommonResult<Map<String,Object>> initCombox1() {
        Map<String,Object> resultMap = new HashMap<>();
        //客户
        Map<String,Object> param = new HashMap<>();
        param.put("audit_status", CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
        List<CustomerInfo> customerInfoList = customerInfoService.findCustomerInfoByCondition(param);
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getIdCode());
            comboxStrVO.setName(customerInfo.getName()+" ("+customerInfo.getIdCode()+")");
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put("customers",comboxStrVOS);

        //业务员
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.BUSINESS_MANAGER.getCode()).getData();
        resultMap.put("yws",initComboxVOS);

        //合同
        List<ContractInfo> contractInfos = contractInfoService.findContractByCondition(new HashMap<>());
        comboxStrVOS = new ArrayList<>();
        for (ContractInfo contractInfo : contractInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(contractInfo.getContractNo());
            comboxStrVO.setName(contractInfo.getContractNo()+" ("+contractInfo.getName()+")");
            //计算合同的剩余天数
            Date endDate = contractInfo.getEndDate();
            Date nowDate = DateUtils.stringToDate(DateUtils.format(new Date()),DateUtils.DATE_PATTERN);
            if(endDate == null || nowDate.getTime() > endDate.getTime()){//过期合同不显示
                continue;
            }
            comboxStrVO.setNote(String.valueOf(DateUtils.diffDay(nowDate,endDate)));
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put("contracts",comboxStrVOS);

        //业务所属部门
        initComboxVOS = (List<InitComboxVO>) oauthClient.findDepartment().getData();
        resultMap.put("departments",initComboxVOS);

        //通关口岸
        List<PortInfo> proInfos = portInfoService.findPortInfoByCondition(new HashMap<>());
        comboxStrVOS = new ArrayList<>();
        for (PortInfo portInfo : proInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(portInfo.getIdCode());
            comboxStrVO.setName(portInfo.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put("proInfos",comboxStrVOS);
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "纯报关-结算单位,idCode=客户CODE,必填")
    @PostMapping(value = "/initUnit")
    public CommonResult<Map<String,Object>> initUnit(@RequestBody Map<String,Object> param) {
        String idCode = MapUtil.getStr(param,"idCode");
        if(idCode != null && "".equals(idCode)){
            return CommonResult.error(400,"参数不合法");
        }
        Map<String,Object> resultMap = new HashMap<>();
        param = new HashMap<>();
        param.put("id_code", idCode);
        List<CustomerInfo > customerInfoList = customerInfoService.findCustomerInfoByCondition(param);
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getUnitCode());
            comboxStrVO.setName(customerInfo.getUnitAccount());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put("units",comboxStrVOS);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "纯报关-通关口岸")
    @PostMapping(value = "/initPort")
    public CommonResult<List<InitComboxStrVO>> initPort() {
        List<PortInfo> proInfos = portInfoService.findPortInfoByCondition(new HashMap<>());
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (PortInfo portInfo : proInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(portInfo.getIdCode());
            comboxStrVO.setName(portInfo.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "纯报关-作业类型")
    @PostMapping(value = "/initBizType")
    public CommonResult<List<InitComboxStrVO>> initBizType() {
        List<ProductClassify> productClassifies = productClassifyService.findProductClassify(new HashMap<>());
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (ProductClassify productClassify : productClassifies) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(productClassify.getIdCode());
            comboxStrVO.setName(productClassify.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "录入费用:应收/付项目/币种 ")
    @PostMapping(value = "/initCost")
    public CommonResult initCost() {
        Map<String,Object> param = new HashMap<>();
        List<CostInfo> costInfos = costInfoService.findCostInfo();//费用项目
        List<InitComboxStrVO> paymentCombox = new ArrayList<>();
        List<InitComboxStrVO> receivableCombox = new ArrayList<>();
        for (CostInfo costInfo : costInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(costInfo.getIdCode());
            comboxStrVO.setName(costInfo.getName());
            if(costInfo.getTypes() == 1){
                receivableCombox.add(comboxStrVO);
            }else if(costInfo.getTypes() == 2){
                paymentCombox.add(comboxStrVO);
            }
        }
        param.put("paymentCost",paymentCombox);
        param.put("receivableCost",receivableCombox);

        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo();
        for (CurrencyInfoVO currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(currencyInfo.getExchangeRate());
            initComboxStrVOS.add(comboxStrVO);
        }
        param.put("currency",initComboxStrVOS);

        //费用类型
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status","1");
        List<CostType> costTypes = costTypeService.list(queryWrapper);
        for (CostType costType : costTypes) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costType.getCodeName());
            initComboxVO.setId(costType.getId());
            costTypeComboxs.add(initComboxVO);
        }
        param.put("costTypes",costTypeComboxs);
        return CommonResult.success(param);
    }

    @ApiOperation(value = "操作员")
    @PostMapping(value = "/initOptUser")
    public CommonResult initOptUser() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.OPERATOR.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }





}

