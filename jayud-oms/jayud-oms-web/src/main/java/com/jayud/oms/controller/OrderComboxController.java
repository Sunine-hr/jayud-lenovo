package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.enums.RoleKeyEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.netty.util.internal.StringUtil;
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

    @Autowired
    IProductBizService productBizService;

    @Autowired
    IWarehouseInfoService warehouseInfoService;

    @Autowired
    ICostGenreService costGenreService;

    @Autowired
    ISupplierInfoService supplierInfoService;

    @ApiOperation(value = "创建订单-客户,业务员,合同,业务所属部门,通关口岸")
    @PostMapping(value = "/initCombox1")
    public CommonResult<Map<String,Object>> initCombox1() {
        Map<String,Object> resultMap = new HashMap<>();
        //客户
        Map<String,Object> param = new HashMap<>();
        param.put(SqlConstant.AUDIT_STATUS, CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
        List<CustomerInfo> customerInfoList = customerInfoService.findCustomerInfoByCondition(param);
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getIdCode());
            comboxStrVO.setName(customerInfo.getName()+" ("+customerInfo.getIdCode()+")");
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.CUSTOMERS,comboxStrVOS);

        //业务员
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.BUSINESS_MANAGER.getCode()).getData();
        resultMap.put(CommonConstant.YWS,initComboxVOS);

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
        resultMap.put(CommonConstant.CONTRACTS,comboxStrVOS);

        //业务所属部门
        initComboxVOS = (List<InitComboxVO>) oauthClient.findDepartment().getData();
        resultMap.put(CommonConstant.DEPARTMENTS,initComboxVOS);

        //通关口岸
        List<PortInfo> proInfos = portInfoService.findPortInfoByCondition(new HashMap<>());
        comboxStrVOS = new ArrayList<>();
        for (PortInfo portInfo : proInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(portInfo.getIdCode());
            comboxStrVO.setName(portInfo.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.PROTINFOS,comboxStrVOS);

        //业务类型
        List<ProductBiz> productBizs = productBizService.findProductBiz();
        comboxStrVOS = new ArrayList<>();
        for (ProductBiz productBiz : productBizs) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(productBiz.getIdCode());
            comboxStrVO.setName(productBiz.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.BIZCODES,comboxStrVOS);
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "创建订单-客户联动业务员和结算单位,idCode=客户CODE,必填")
    @PostMapping(value = "/initUnit")
    public CommonResult<Map<String,Object>> initUnit(@RequestBody Map<String,Object> param) {
        String idCode = MapUtil.getStr(param,"idCode");
        if(StringUtil.isNullOrEmpty(idCode)){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Map<String,Object> resultMap = new HashMap<>();
        List<CustomerInfoVO> customerInfoList = customerInfoService.findUnitInfoByCode(idCode);
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (CustomerInfoVO customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getIdCode());
            comboxStrVO.setName(customerInfo.getName());
            comboxStrVOS.add(comboxStrVO);
            if(customerInfo.getYwId() != null) {
                ids.add(customerInfo.getYwId());
            }
        }
        resultMap.put("units",comboxStrVOS);

        List<SupplierInfo> supplierInfos = supplierInfoService.getApprovedSupplier(
                BeanUtils.convertToFieldName(true,
                        SupplierInfo::getId, SupplierInfo::getSupplierChName,SupplierInfo::getSupplierCode));
        List<InitComboxStrVO> supplierStrVOS = new ArrayList<>();
        for (SupplierInfo supplierInfo : supplierInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(supplierInfo.getSupplierCode());
            comboxStrVO.setName(supplierInfo.getSupplierChName());
            supplierStrVOS.add(comboxStrVO);
        }
        resultMap.put("supplierInfos",supplierStrVOS);//下拉供应商

        List<InitComboxVO> yws = new ArrayList<>();
        if(ids.size() > 0) {
            List<SystemUserVO> userVOS = oauthClient.getUsersByIds(ids).getData();
            for (SystemUserVO systemUserVO : userVOS) {
                InitComboxVO comboxVO = new InitComboxVO();
                comboxVO.setId(systemUserVO.getId());
                comboxVO.setName(systemUserVO.getUserName());
                yws.add(comboxVO);
            }
        }
        resultMap.put("yws",yws);
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
        Map<String,Object> param = new HashMap<>();
        param.put("f_id",0);
        List<ProductClassify> productClassifies = productClassifyService.findProductClassify(param);
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
            receivableCombox.add(comboxStrVO);//后期没做应收应付项目的区分
            paymentCombox.add(comboxStrVO);
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
        return CommonResult.success(param);
    }

    @ApiOperation(value = "操作员")
    @PostMapping(value = "/initOptUser")
    public CommonResult initOptUser() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.OPERATOR.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "中转仓库")
    @PostMapping(value = "/initWarehouseInfo")
    public CommonResult initWarehouseInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS,1);
        List<WarehouseInfo> warehouseInfos = warehouseInfoService.list(queryWrapper);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (WarehouseInfo warehouseInfo : warehouseInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(warehouseInfo.getId());
            initComboxVO.setName(warehouseInfo.getWarehouseName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "费用类别,idCode=费用名称的隐藏值")
    @PostMapping(value = "/initCostType")
    public CommonResult<List<InitComboxVO>> initCostType(@RequestBody Map<String,Object> param) {
        String idCode = MapUtil.getStr(param,CommonConstant.ID_CODE);
        QueryWrapper queryCostInfo = new QueryWrapper();
        queryCostInfo.eq(SqlConstant.ID_CODE,idCode);
        CostInfo costInfo = costInfoService.getOne(queryCostInfo);
        if(costInfo == null || StringUtil.isNullOrEmpty(costInfo.getCids())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        String[] cids = costInfo.getCids().split(CommonConstant.COMMA);
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS,CommonConstant.VALUE_1);
        queryWrapper.in(SqlConstant.ID,cids);
        List<CostType> costTypes = costTypeService.list(queryWrapper);
        for (CostType costType : costTypes) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costType.getCodeName());
            initComboxVO.setId(costType.getId());
            costTypeComboxs.add(initComboxVO);
        }
        return CommonResult.success(costTypeComboxs);
    }

    @ApiOperation(value = "费用类型,bizCode=业务类型CODE")
    @PostMapping(value = "/initCostGenre")
    public CommonResult<List<InitComboxVO>> initCostGenre(@RequestBody Map<String,Object> param) {
        String code = MapUtil.getStr(param,CommonConstant.BIZ_CODE);
        QueryWrapper queryProductBiz = new QueryWrapper();
        queryProductBiz.eq(SqlConstant.ID_CODE,code);
        ProductBiz productBiz = productBizService.getOne(queryProductBiz);
        if(productBiz == null || StringUtil.isNullOrEmpty(productBiz.getCostGenreIds())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        String[] cids = productBiz.getCostGenreIds().split(CommonConstant.COMMA);
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS,CommonConstant.VALUE_1);
        queryWrapper.in(SqlConstant.ID,cids);
        List<CostGenre> costGenres = costGenreService.list(queryWrapper);
        for (CostGenre costGenre : costGenres) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costGenre.getName());
            initComboxVO.setId(costGenre.getId());
            if(productBiz.getCostGenreDefault() == costGenre.getId()){
                initComboxVO.setIsDefault(true);
            }else {
                initComboxVO.setIsDefault(false);
            }
            costTypeComboxs.add(initComboxVO);
        }
        return CommonResult.success(costTypeComboxs);
    }

    @ApiOperation(value = "二期优化:法人主体")
    @PostMapping(value = "/initLegalEntity")
    public CommonResult<List<InitComboxVO>> initLegalEntity() {
        return CommonResult.success(oauthClient.findLegalEntity().getData());
    }








}

