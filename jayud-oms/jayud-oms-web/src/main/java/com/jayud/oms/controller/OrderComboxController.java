package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.*;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FreightAirClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.enums.*;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/combox")
@Api(tags = "初始化下拉框接口")
@Slf4j
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

    @Autowired
    private FreightAirClient freightAirClient;
    @Autowired
    private ICustomerRelaLegalService relaLegalService;
    @Autowired
    private IServiceTypeService serviceTypeService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IVehicleInfoService vehicleInfoService;
    @Autowired
    private IDriverInfoService driverInfoService;


    @ApiOperation(value = "创建订单-客户,业务员,合同,业务所属部门,通关口岸")
    @PostMapping(value = "/initCombox1")
    public CommonResult<Map<String, Object>> initCombox1(@RequestBody Map<String, Object> param) {
        String loginUserName = MapUtil.getStr(param, "loginUserName");
        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(loginUserName);
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Map<String, Object> resultMap = new HashMap<>();
        //客户
        List<CustomerInfo> allCustomerInfoList = customerInfoService.getCustomerInfoByCondition(legalIds);
        List<CustomerInfo> customerInfoList = allCustomerInfoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CustomerInfo::getName))), ArrayList::new));
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(customerInfo.getIdCode());
            if (CreditStatusEnum.ABNORMAL.getCode().equals(customerInfo.getNationalCredit())
                    || CreditStatusEnum.ABNORMAL.getCode().equals(customerInfo.getCustomsCredit())
                    || CustomsCreditRatingEnum.THREE.getCode().equals(customerInfo.getCustomsCreditRating())) {
                comboxStrVO.setNote("该用户存在风险");
            }

            comboxStrVO.setName(customerInfo.getName() + " (" + customerInfo.getIdCode() + ")");
            comboxStrVO.setId(customerInfo.getId());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.CUSTOMERS, comboxStrVOS);

        //业务员
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.BUSINESS_MANAGER.getCode()).getData();
        resultMap.put(CommonConstant.YWS, initComboxVOS);

        //合同
        List<ContractInfo> contractInfos = contractInfoService.findContractByCondition(new HashMap<>());
        comboxStrVOS = new ArrayList<>();
        for (ContractInfo contractInfo : contractInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(contractInfo.getContractNo());
            comboxStrVO.setName(contractInfo.getContractNo() + " (" + contractInfo.getName() + ")");
            //计算合同的剩余天数
            Date endDate = contractInfo.getEndDate();
            Date nowDate = DateUtils.stringToDate(DateUtils.format(new Date()), DateUtils.DATE_PATTERN);
            if (endDate == null || nowDate.getTime() > endDate.getTime()) {//过期合同不显示
                continue;
            }
            comboxStrVO.setNote(String.valueOf(DateUtils.diffDay(nowDate, endDate)));
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.CONTRACTS, comboxStrVOS);

        //业务所属部门
        initComboxVOS = oauthClient.findDepartment().getData();
        resultMap.put(CommonConstant.DEPARTMENTS, initComboxVOS);

        //通关口岸
        List<PortInfo> proInfos = portInfoService.findPortInfoByCondition(new HashMap<>());
        comboxStrVOS = new ArrayList<>();
        for (PortInfo portInfo : proInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(portInfo.getIdCode());
            comboxStrVO.setName(portInfo.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.PROTINFOS, comboxStrVOS);

        //业务类型
        List<ProductBiz> productBizs = productBizService.findProductBiz();
        comboxStrVOS = new ArrayList<>();
        for (ProductBiz productBiz : productBizs) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(productBiz.getIdCode());
            comboxStrVO.setName(productBiz.getName());
            comboxStrVOS.add(comboxStrVO);
        }
        resultMap.put(CommonConstant.BIZCODES, comboxStrVOS);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "二期优化3现有接口:创建订单-客户联动业务员和结算单位,idCode=客户CODE,必填")
    @PostMapping(value = "/initUnit")
    public CommonResult<Map<String, Object>> initUnit(@RequestBody Map<String, Object> param) {
        String idCode = MapUtil.getStr(param, "idCode");
        if (StringUtil.isNullOrEmpty(idCode)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //根据客户CODE获取客户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id_code", idCode);
        CustomerInfo customer = customerInfoService.getOne(queryWrapper);
        Map<String, Object> resultMap = new HashMap<>();
        List<CustomerInfoVO> allCustomerInfoList = customerInfoService.relateUnitList(customer.getId());
        List<CustomerInfoVO> customerInfoList = allCustomerInfoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CustomerInfoVO::getName))), ArrayList::new));
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();

        //默认关联自己
        InitComboxStrVO customerComboxVO = new InitComboxStrVO();
        customerComboxVO.setCode(customer.getIdCode());
        customerComboxVO.setName(customer.getName());
        comboxStrVOS.add(customerComboxVO);

        for (CustomerInfoVO customerInfo : customerInfoList) {
            customerComboxVO = new InitComboxStrVO();
            customerComboxVO.setCode(customerInfo.getIdCode());
            customerComboxVO.setName(customerInfo.getName());
            comboxStrVOS.add(customerComboxVO);
        }
        //去重重复客户code
        comboxStrVOS = comboxStrVOS.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(InitComboxStrVO::getCode))), ArrayList::new));


        //如果没有结算单位,客户本身作为结算单位
//        if (comboxStrVOS.size() == 0) {
//            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
//            comboxStrVO.setCode(customer.getIdCode());
//            comboxStrVO.setName(customer.getName());
//            comboxStrVOS.add(comboxStrVO);
//        }
        resultMap.put("units", comboxStrVOS);

        List<SupplierInfo> supplierInfos = supplierInfoService.getApprovedSupplier(
                BeanUtils.convertToFieldName(true,
                        SupplierInfo::getId, SupplierInfo::getSupplierChName, SupplierInfo::getSupplierCode));
        List<InitComboxStrVO> supplierStrVOS = new ArrayList<>();
        for (SupplierInfo supplierInfo : supplierInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(supplierInfo.getSupplierCode());
            comboxStrVO.setName(supplierInfo.getSupplierChName());
            supplierStrVOS.add(comboxStrVO);
        }
        resultMap.put("supplierInfos", supplierStrVOS);//下拉供应商

        //根据客户获取业务员
        //设置业务员默认值
        resultMap.put("ywId", customer.getYwId());
//        List<InitComboxVO> yws = new ArrayList<>();
//        List<Long> ids = new ArrayList<>();
//        ids.add(customer.getYwId());
//        if (ids.size() > 0) {
//            List<SystemUserVO> userVOS = oauthClient.getUsersByIds(ids).getData();
//            for (SystemUserVO systemUserVO : userVOS) {
//                InitComboxVO comboxVO = new InitComboxVO();
//                comboxVO.setId(systemUserVO.getId());
//                comboxVO.setName(systemUserVO.getUserName());
//                yws.add(comboxVO);
//            }
//        }
        //获取所有业务员下拉框
        List<SystemUserVO> systemUserVOS = oauthClient.getSystemUserList().getData();
        resultMap.put("yws", systemUserVOS);

        //根据客户获取业务员部门
        resultMap.put("departmentId", customer.getDepartmentId());

        //查询法人主体
        List<LegalEntityVO> legalEntityVOS = relaLegalService.findLegalByCustomerId(customer.getId(),
                LegalEntityAuditStatusEnum.SUCCESS.getCode());
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (LegalEntityVO legalEntityVO : legalEntityVOS) {
            InitComboxVO comboxVO = new InitComboxVO();
            comboxVO.setId(legalEntityVO.getId());
            comboxVO.setName(legalEntityVO.getLegalName());
            initComboxVOS.add(comboxVO);
        }
        resultMap.put("legalEntitys", initComboxVOS);

        return CommonResult.success(resultMap);
    }

//    @ApiOperation(value = "获取选中费用对象")
//    @PostMapping(value = "/getSelectedFeeObj")
//    public CommonResult getFeeObj(@RequestBody QueryFeeObjForm form) {
//        //获取选中结算单位对象
//        Map<String, Object> map = new HashMap<>();
//        map.put("subUnitCode", form.getSubUnitCode());
//
//        //获取选中中港供应商
//
//
//        return null;
//    }


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
        Map<String, Object> param = new HashMap<>();
        param.put("f_id", 0);
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
    public CommonResult initCost(@RequestBody Map<String, Object> param) {
        String createdTimeStr = MapUtil.getStr(param, "createdTimeStr");
        if (StringUtil.isNullOrEmpty(createdTimeStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Map<String, Object> result = new HashMap<>();
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
        result.put("paymentCost", paymentCombox);
        result.put("receivableCost", receivableCombox);

        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfoVO> currencyInfos = currencyInfoService.findCurrencyInfo(createdTimeStr);
        for (CurrencyInfoVO currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(String.valueOf(currencyInfo.getExchangeRate()));
            initComboxStrVOS.add(comboxStrVO);
        }
        result.put("currency", initComboxStrVOS);
        return CommonResult.success(result);
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
        queryWrapper.eq(SqlConstant.STATUS, 1);
        queryWrapper.eq(SqlConstant.AUDIT_STATUS, "2");
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
    public CommonResult<List<InitComboxVO>> initCostType(@RequestBody Map<String, Object> param) {
        String idCode = MapUtil.getStr(param, CommonConstant.ID_CODE);
        QueryWrapper queryCostInfo = new QueryWrapper();
        queryCostInfo.eq(SqlConstant.ID_CODE, idCode);
        CostInfo costInfo = costInfoService.getOne(queryCostInfo);
        if (costInfo == null || StringUtil.isNullOrEmpty(costInfo.getCids())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        String[] cids = costInfo.getCids().split(CommonConstant.COMMA);
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, CommonConstant.VALUE_1);
        queryWrapper.in(SqlConstant.ID, cids);
        List<CostType> costTypes = costTypeService.list(queryWrapper);
        for (CostType costType : costTypes) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costType.getCodeName());
            initComboxVO.setId(costType.getId());
            costTypeComboxs.add(initComboxVO);
        }
        return CommonResult.success(costTypeComboxs);
    }


    @ApiOperation(value = "根据费用名称code下拉作业环节,idCode=费用名称的隐藏值")
    @PostMapping(value = "/initCostTypeByCostInfoCode")
    public CommonResult<Map<String, List<InitComboxVO>>> initCostTypeByCostInfoCode(@RequestBody Map<String, Object> param) {
        Map<String, List<InitComboxVO>> map = this.costInfoService.initCostTypeByCostInfoCode();
        return CommonResult.success(map);
    }


    @ApiOperation(value = "费用类型,bizCode=业务类型CODE")
    @PostMapping(value = "/initCostGenre")
    public CommonResult<List<InitComboxVO>> initCostGenre(@RequestBody Map<String, Object> param) {
        String code = MapUtil.getStr(param, CommonConstant.BIZ_CODE);
        List<ProductBiz> productBizs = new ArrayList<>();
        if (!StringUtils.isEmpty(code)) {
            QueryWrapper queryProductBiz = new QueryWrapper();
            queryProductBiz.eq(SqlConstant.ID_CODE, code);
            productBizs = productBizService.list(queryProductBiz);
        } else {
            productBizs = productBizService.getEnableProductBiz();
        }
        if (productBizs.size() == 0) {
            return CommonResult.success();
        }

//        if (productBiz == null || StringUtil.isNullOrEmpty(productBiz.getCostGenreIds())) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//        }
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, StatusEnum.ENABLE.getCode());
        List<CostGenre> costGenres = costGenreService.list(queryWrapper);
        for (CostGenre costGenre : costGenres) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costGenre.getName());
            initComboxVO.setId(costGenre.getId());
            initComboxVO.setTaxRate(costGenre.getTaxRate());
            if (productBizs.size() == 1) {
                ProductBiz productBiz = productBizs.get(0);
                if (costGenre.getId().equals(productBiz.getCostGenreDefault())) {
                    initComboxVO.setIsDefault(true);
                } else {
                    initComboxVO.setIsDefault(false);
                }
            }

            costTypeComboxs.add(initComboxVO);
        }

        return CommonResult.success(costTypeComboxs);
    }


    @ApiModelProperty(value = "空运 下拉选项-飞机港口")
    @RequestMapping(value = "/initAirPort")
    public CommonResult initAirPort() {
        ApiResult result = freightAirClient.getAirPort();
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("获取飞机港口下拉数据失败");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success(result.getData());
    }


    @ApiOperation(value = "二期优化3:新增客户列表 初始化审核通过法人主体")
    @PostMapping(value = "/initLegalEntity")
    public CommonResult<List<InitComboxVO>> initLegalEntity() {
        List<InitComboxVO> initComboxVOS = oauthClient.findLegalEntity().getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "服务单 下拉选项-服务类型")
    @PostMapping(value = "/initServiceType")
    public CommonResult<List<ServiceTypeVO>> getEnableParentProductClassify() {
        List<ServiceType> list = this.serviceTypeService.getEnableParentServiceType(StatusEnum.ENABLE.getCode());
        List<ServiceTypeVO> result = new ArrayList<>();
        list.forEach(tmp -> result.add(ConvertUtil.convert(tmp, ServiceTypeVO.class)));
        return CommonResult.success(result);
    }

    @ApiOperation(value = "客户 下拉选项-审核通过客户")
    @PostMapping(value = "/initApprovedCustomer")
    public CommonResult<List<InitComboxStrVO>> initApprovedCustomer() {
        return CommonResult.success(this.customerInfoService.initApprovedCustomer());
    }


    @ApiOperation(value = "录用费用页面-下拉选单位")
    @PostMapping(value = "/initCostUnit")
    public CommonResult<List<InitComboxStrVO>> initCostUnit() {
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (UnitEnum unitEnum : UnitEnum.values()) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(unitEnum.getCode());
            comboxStrVO.setName(unitEnum.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "拖车订单-下拉框")
    @PostMapping(value = "/initTrailerUnit")
    public CommonResult initTrailerUnit() {
        //获取下拉港口
        List<Dict> port = dictService.getByDictTypeCode("Port");
        List<InitComboxStrVO> ports = new ArrayList<>();
        for (Dict dict : port) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(dict.getValue());
            initComboxStrVO.setCode(dict.getCode());
            ports.add(initComboxStrVO);
        }
        //获取下拉车型
        List<VehicleSizeInfoVO> vehicleSizeInfoVOS = vehicleInfoService.findVehicleSize();
        List<InitComboxVO> cabinetSizes = new ArrayList<>();
        for (VehicleSizeInfoVO obj : vehicleSizeInfoVOS) {
            if (VehicleTypeEnum.CABINET_CAR.getCode().equals(obj.getVehicleType())) {
                InitComboxVO initComboxVO = new InitComboxVO();
                initComboxVO.setId(obj.getId());
                initComboxVO.setName(obj.getVehicleSize());
                cabinetSizes.add(initComboxVO);
            }
        }
        //获取操作主体下拉列表
        List<InitComboxVO> legals = oauthClient.findLegalEntity().getData();

        //获取结算单位下拉框
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");
        queryWrapper.eq("audit_status", 10);
        List<CustomerInfo> customerInfos = customerInfoService.list(queryWrapper);
        List<InitComboxStrVO> initComboxVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfos) {
            InitComboxStrVO initComboxVO = new InitComboxStrVO();
            initComboxVO.setCode(customerInfo.getIdCode());
            initComboxVO.setName(customerInfo.getName());
            initComboxVOS.add(initComboxVO);
        }
        Map map = new HashMap();
        map.put("ports", ports);
        map.put("cabinetSizes", cabinetSizes);
        map.put("legalEntitys", legals);
        map.put("customerInfos", initComboxVOS);
        return CommonResult.success(map);
    }


    @ApiOperation(value = "下拉币种")
    @RequestMapping(value = "/initCurrencyInfo")
    public CommonResult<List<InitComboxStrVO>> initCurrencyInfo() {
        return CommonResult.success(this.currencyInfoService.initCurrencyInfo());
    }


    @ApiOperation(value = "信用评价-下拉框")
    @PostMapping(value = "/initCreditEvaluation")
    public CommonResult<Map<String, Object>> initCreditEvaluation() {

        Map<String, Object> map = new HashMap<>();
        map.put("credit", CreditStatusEnum.getDropDownList());
        map.put("creditRating", CustomsCreditRatingEnum.getDropDownList());

        return CommonResult.success(map);
    }

    @ApiOperation(value = "海关调查问卷状态-下拉框")
    @PostMapping(value = "/initCustomsQuestionnaireStatus")
    public CommonResult<List<com.jayud.common.entity.InitComboxVO>> initCustomsQuestionnaireStatus() {
        return CommonResult.success(CustomsQuestionnaireStatusEnum.getDropDownList());
    }

    @ApiOperation(value = "仓储订单-下拉框")
    @PostMapping(value = "/initStorageUnit")
    public CommonResult initStorageUnit() {
        //获取操作主体下拉列表
        List<InitComboxVO> legals = oauthClient.findLegalEntity().getData();
        List<InitComboxVO> departments = oauthClient.findDepartment().getData();
        Map map = new HashMap();
        map.put("legalEntitys", legals);
        map.put("departments", departments);
        map.put("defaultDepartments", "仓储部");
        return CommonResult.success(map);
    }


    @ApiOperation(value = "搜索客户信息-下拉框")
    @PostMapping(value = "/searchCustomersInfo")
    public CommonResult<com.alibaba.fastjson.JSONArray> searchCustomersInfo() {
        List<CustomerInfo> list = this.customerInfoService.list();

        com.alibaba.fastjson.JSONArray array = JSON.parseArray(JSON.toJSONString(list, SerializerFeature.WriteMapNullValue));
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            jsonObject.put("value", jsonObject.get("name"));
        }
        return CommonResult.success(array);
    }

    @ApiOperation(value = "根据传入类型获取默认值(创建订单页面)")
    @PostMapping(value = "/initCreateOrderDefaultValue")
    public CommonResult<Map<String, Object>> initCreateOrderDefaultValue(@RequestBody Map<String, Object> map) {
        String type = MapUtil.getStr(map, "type");
        if (StringUtils.isEmpty(type)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        SubOrderSignEnum subType = SubOrderSignEnum.getEnum(type);
        Map<String, Object> response = new HashMap<>();
        switch (subType) {
            case BG:
                Object data = this.oauthClient.getLegalEntityByCode("802").getData();
                cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject(data);
                response.put("bgLegalEntityId", jsonObject.getInt("id"));
                response.put("bgLegalEntityName", jsonObject.getStr("legalName"));
                break;
        }


        return CommonResult.success(response);
    }

    @ApiOperation(value = "下拉业务类型")
    @PostMapping(value = "/initBizService")
    public CommonResult<List<InitComboxStrVO>> initBizService() {
        //业务类型
        List<ProductBiz> productBizs = productBizService.findProductBiz();
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (ProductBiz productBiz : productBizs) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(productBiz.getIdCode());
            comboxStrVO.setName(productBiz.getName());
            comboxStrVOS.add(comboxStrVO);
        }

        return CommonResult.success(comboxStrVOS);
    }


    @ApiOperation(value = "下拉所属部门")
    @PostMapping(value = "/initDepartment")
    public CommonResult<List<InitComboxVO>> initDepartment(Map<String, Object> map) {
        //业务所属部门
        List<InitComboxVO> initComboxVOS = oauthClient.findDepartment().getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "默认部门")
    @PostMapping(value = "/getDefaultDepartment")
    public CommonResult getDefaultDepartment(@RequestBody Map<String, Object> map) {
        //业务所属部门
        String type = MapUtil.getStr(map, "type");
        SubOrderSignEnum orderSignEnum = SubOrderSignEnum.getEnum(type);
        String department = null;
        switch (orderSignEnum) {
            case BG:
                department = "报关部";
                break;
            case CCF:
            case CCE:
            case CCI:
                department = "仓储部";
                break;
        }
        Object departmentId = oauthClient.getDeptIdByDeptName(department).getData();
        if (departmentId == null) {
            return CommonResult.error(400, "不存在操作部门");
        }
        Map<String, Long> response = new HashMap<>();
        response.put("departmentId", Long.valueOf(departmentId.toString()));
        return CommonResult.success(response);
    }


    @ApiOperation(value = "下拉客户结算单位业务类型")
    @PostMapping(value = "/initCustomerUnitBisType")
    public CommonResult<List<com.jayud.common.entity.InitComboxStrVO>> initCustomerUnitBisType(Map<String, Object> map) {
        return CommonResult.success(SubOrderSignEnum.initBusinessType());
    }

    @ApiOperation(value = "下拉全部费用类别")
    @PostMapping(value = "/initAllCostType")
    public CommonResult<List<InitComboxStrVO>> initAllCostType(@RequestBody Map<String, Object> param) {
        List<CostType> enableCostType = this.costTypeService.getEnableCostType();
        List<InitComboxStrVO> list = new ArrayList<>();
        for (CostType costType : enableCostType) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setId(costType.getId());
            initComboxStrVO.setName(costType.getCodeName());
            initComboxStrVO.setCode(costType.getCode());
            list.add(initComboxStrVO);
        }
        return CommonResult.success(list);
    }

    /**
     * 查询有效司机
     */
    @ApiOperation("查询有效司机")
    @PostMapping(value = "/initEffectiveDrivers")
    public CommonResult<List<InitComboxStrVO>> initEffectiveDrivers() {
        List<InitComboxStrVO> list = this.driverInfoService.initEffectiveDriver();
        return CommonResult.success(list);
    }

    /**
     * 根据司机id查询所有绑定车辆
     *
     * @return
     */
    @ApiOperation("根据司机id查询所有绑定车辆")
    @PostMapping(value = "/initVehicleInfoByDriverId")
    public CommonResult<List<InitComboxStrVO>> initVehicleInfoByDriverId(@RequestBody Map<String, Object> map) {
        Long driverId = MapUtil.getLong(map, "driverId");
        if (driverId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<VehicleInfo> vehicleInfos = vehicleInfoService.getVehicleInfoByDriverId(driverId, StatusEnum.ENABLE.getCode());
        List<InitComboxStrVO> list = new ArrayList<>();
        vehicleInfos.forEach(e -> {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(e.getPlateNumber());
            initComboxStrVO.setId(e.getId());
            list.add(initComboxStrVO);
        });
        return CommonResult.success(list);
    }


    /**
     * 下拉车牌
     *
     * @return
     */
    @ApiOperation("下拉车牌")
    @PostMapping(value = "/initVehicleInfo")
    public CommonResult<List<InitComboxStrVO>> initVehicleInfo() {
        return CommonResult.success(this.vehicleInfoService.initVehicleInfo(com.jayud.common.enums.StatusEnum.ENABLE.getCode()));
    }

    /**
     * 下拉详情业务类型
     *
     * @return
     */
    @ApiOperation("下拉详情业务类型")
    @PostMapping(value = "/initDetailsBusinessType")
    public CommonResult<List<Map<String, Object>>> initDetailsBusinessType(@RequestBody Map<String, Object> params) {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (BusinessTypeEnum enumType : BusinessTypeEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> objs = new ArrayList<>();
            Map<String, Object> obj = new HashMap<>();
            switch (enumType) {
                case KY:
                    break;
                case ZGYS:
                    InputOrderTransportVO transportVO = MapUtil.get(params, "orderTransportForm", InputOrderTransportVO.class);
                    if (transportVO == null) continue;
                    map.put("name", enumType.getDesc());
                    obj.put("id", transportVO.getId());
                    obj.put("type", enumType.getCode());
                    obj.put("orderNo", transportVO.getOrderNo());
                    objs.add(obj);
                    map.put("data", objs);
                    break;
                case BG:
                    InputOrderCustomsVO orderCustomsForm = MapUtil.get(params, "orderCustomsForm", InputOrderCustomsVO.class);
                    if (orderCustomsForm == null) continue;
                    map.put("name", enumType.getDesc());
                    orderCustomsForm.getSubOrders().forEach(e -> {
                        obj.put("id", e.getSubOrderId());
                        obj.put("type", enumType.getCode());
                        obj.put("orderNo", e.getOrderNo());
                        objs.add(obj);
                    });
                    map.put("data", objs);
                    break;
                case NL:
                    InputOrderInlandTPVO inputOrderInlandTPVO = MapUtil.get(params, "orderInlandTransportForm", InputOrderInlandTPVO.class);
                    if (inputOrderInlandTPVO == null) continue;
                    map.put("name", enumType.getDesc());
                    obj.put("id", inputOrderInlandTPVO.getId());
                    obj.put("type", enumType.getCode());
                    obj.put("orderNo", inputOrderInlandTPVO.getOrderNo());
                    objs.add(obj);
                    map.put("data", objs);
                    break;
                case RK:
                    InputStorageInputOrderVO rk = MapUtil.get(params, "storageInputOrderForm", InputStorageInputOrderVO.class);
                    if (rk == null) continue;
                    map.put("name", enumType.getDesc());
                    obj.put("id", rk.getId());
                    obj.put("type", enumType.getCode());
                    obj.put("orderNo", rk.getOrderNo());
                    objs.add(obj);
                    map.put("data", objs);
                    break;
                case CK:
                    InputStorageOutOrderVO ck = MapUtil.get(params, "storageOutOrderForm", InputStorageOutOrderVO.class);
                    if (ck == null) continue;
                    obj.put("id", ck.getId());
                    obj.put("type", enumType.getCode());
                    obj.put("orderNo", ck.getOrderNo());
                    objs.add(obj);
                    map.put("data", objs);
                    break;
                case TC:
                    JSONArray trailerOrderFormArray = new JSONObject(params).getJSONArray("trailerOrderForm");
                    if (CollectionUtils.isEmpty(trailerOrderFormArray)) continue;
                    List<InputTrailerOrderVO> trailerOrderForms = trailerOrderFormArray.toJavaList(InputTrailerOrderVO.class);
                    map.put("name", enumType.getDesc());
                    trailerOrderForms.forEach(e -> {
                        obj.put("id", e.getId());
                        obj.put("type", enumType.getCode());
                        obj.put("orderNo", e.getOrderNo());
                        objs.add(obj);
                    });
                    map.put("data", objs);
                    break;
                case IO:
                    InputStorageFastOrderVO io = MapUtil.get(params, "storageFastOrderForm", InputStorageFastOrderVO.class);
                    if (io == null) continue;
                    map.put("name", enumType.getDesc());
                    obj.put("id", io.getId());
                    obj.put("type", enumType.getCode());
                    obj.put("orderNo", io.getOrderNo());
                    objs.add(obj);
                    map.put("data", objs);
                    break;
            }
            if (map.size() > 0) {
                maps.add(map);
            }

        }
        return CommonResult.success(maps);
    }

    /**
     * 下拉合同客户
     *
     * @return
     */
    @ApiOperation("下拉合同客户")
    @PostMapping(value = "/initContractCustomer")
    public CommonResult<Set<InitComboxStrVO>> initContractCustomer() {
        List<ContractInfo> list = this.contractInfoService.getByCondition(new ContractInfo().setType("0").setStatus(com.jayud.common.enums.StatusEnum.ENABLE.getCode() + ""));
        Set<InitComboxStrVO> initComboxStrVOS = new HashSet<>();
        for (ContractInfo contractInfo : list) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(contractInfo.getName());
            initComboxStrVO.setId(contractInfo.getBindId());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return CommonResult.success(initComboxStrVOS);
    }

    /**
     * 下拉合同编号
     *
     * @return
     */
    @ApiOperation("下拉合同编号")
    @PostMapping(value = "/initContractByCustomerId")
    public CommonResult<List<InitComboxStrVO>> initContractByCustomerId(@RequestBody Map<String, Object> map) {
        Long customerId = MapUtil.getLong(map, "customerId");
        if (customerId == null) {
            return CommonResult.error(400, "请选择客户");
        }
        List<ContractInfo> list = this.contractInfoService.getByCondition(new ContractInfo().setType("0").setStatus(com.jayud.common.enums.StatusEnum.ENABLE.getCode() + "")
                .setBindId(customerId));
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (ContractInfo contractInfo : list) {
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(contractInfo.getContractNo());
            initComboxStrVO.setCode(contractInfo.getContractNo());
            initComboxStrVOS.add(initComboxStrVO);
        }
        return CommonResult.success(initComboxStrVOS);
    }

    /**
     * 下拉所有车型
     *
     * @return
     */
    @ApiOperation("下拉所有车型")
    @PostMapping(value = "/initVehicleSize")
    public CommonResult<List<com.jayud.common.entity.InitComboxStrVO>> initVehicleSize(@RequestBody Map<String, Object> map) {
        //获取下拉车型
        List<VehicleSizeInfoVO> vehicleSizeInfoVOS = vehicleInfoService.findVehicleSize();
        List<com.jayud.common.entity.InitComboxStrVO> cabinetSizes = new ArrayList<>();
        for (VehicleSizeInfoVO obj : vehicleSizeInfoVOS) {
            com.jayud.common.entity.InitComboxStrVO initComboxVO = new com.jayud.common.entity.InitComboxStrVO();
            initComboxVO.setId(obj.getId());
            initComboxVO.setName(obj.getVehicleSize());
            cabinetSizes.add(initComboxVO);
        }
        return CommonResult.success(cabinetSizes);
    }


    /**
     * 下拉客户对象
     *
     * @return
     */
    @ApiOperation("下拉客户对象")
    @PostMapping(value = "/initClientObj")
    public CommonResult<List<InitComboxStrVO>> initClientObj(@RequestBody Map<String, Object> map) {

        Integer type = MapUtil.getInt(map, "type");
        List<InitComboxStrVO> initComboxVOS = new ArrayList<>();
        switch (type) {
            case 1: //客户
                //获取结算单位下拉框
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("status", "1");
                queryWrapper.eq("audit_status", 10);
                List<CustomerInfo> customerInfos = customerInfoService.list(queryWrapper);
                for (CustomerInfo customerInfo : customerInfos) {
                    InitComboxStrVO initComboxVO = new InitComboxStrVO();
                    initComboxVO.setCode(customerInfo.getIdCode());
                    initComboxVO.setName(customerInfo.getName());
                    initComboxVOS.add(initComboxVO);
                }
                break;
            case 2: //供应商
                List<SupplierInfo> supplierInfos = supplierInfoService.getApprovedSupplier(
                        BeanUtils.convertToFieldName(true,
                                SupplierInfo::getId, SupplierInfo::getSupplierChName, SupplierInfo::getSupplierCode));
                for (SupplierInfo supplierInfo : supplierInfos) {
                    InitComboxStrVO comboxStrVO = new InitComboxStrVO();
                    comboxStrVO.setCode(supplierInfo.getSupplierCode());
                    comboxStrVO.setName(supplierInfo.getSupplierChName());
                    initComboxVOS.add(comboxStrVO);
                }
                break;
        }
        return CommonResult.success(initComboxVOS);
    }

}

