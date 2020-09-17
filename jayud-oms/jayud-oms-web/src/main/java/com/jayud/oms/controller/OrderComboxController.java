package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.enums.RoleKeyEnum;
import com.jayud.oms.model.po.ContractInfo;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.PortInfo;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.service.IContractInfoService;
import com.jayud.oms.service.IPortInfoService;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.model.vo.InitComboxVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
        initComboxVOS = (List<InitComboxVO>)oauthClient.findDepartment();
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
    public CommonResult<Map<String,Object>> initUnit(Map<String,Object> param) {
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





}

