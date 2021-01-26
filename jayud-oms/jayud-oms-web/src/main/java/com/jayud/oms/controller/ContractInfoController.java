package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.AddContractInfoForm;
import com.jayud.oms.model.bo.DeleteForm;
import com.jayud.oms.model.bo.QueryContractInfoForm;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.ContractInfoVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同管理 前端控制器
 */
@RestController
@RequestMapping("/contractInfo")
@Api(tags = "合同管理")
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
    private IProductClassifyService productClassifyService;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ISupplierInfoService supplierInfoService;

    @ApiOperation(value = "查询合同列表")
    @PostMapping(value = "/findContractInfoByPage")
    public CommonResult<CommonPageResult<ContractInfoVO>> findContractInfoByPage(
            @Valid @RequestBody QueryContractInfoForm form) {
        IPage<ContractInfoVO> pageList = contractInfoService.findContractInfoByPage(form);
        List<InitComboxVO> data = oauthClient.findLegalEntity().getData();
        //法人主体
        if (data != null) {
            Map<Long, String> map = new HashMap<>();
            for (InitComboxVO obj : data) {
                map.put(obj.getId(), obj.getName());
            }

            for (ContractInfoVO record : pageList.getRecords()) {
                record.setLegalEntityName(map.get(record.getLegalEntity()));
            }
        }

        CommonPageResult<ContractInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑时数据回显,id=合同ID")
    @PostMapping(value = "/getContractInfoById")
    public CommonResult<ContractInfoVO> getContractInfoById(@RequestBody Map<String, Object> param) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        String id = MapUtil.getStr(param, "id");
        ContractInfoVO contractInfoVO = contractInfoService.getContractInfoById(Long.parseLong(id));
        if (contractInfoVO != null && contractInfoVO.getBusinessType() != null) {
            String businessType = contractInfoVO.getBusinessType();
            contractInfoVO.setBusinessTypes(businessType);
        }

        //处理附件
        contractInfoVO.setFileViews(StringUtils.getFileViews(contractInfoVO.getContractUrl(), contractInfoVO.getContractName(), prePath));
        return CommonResult.success(contractInfoVO);
    }

    @ApiOperation(value = "新增编辑合同")
    @PostMapping(value = "/saveOrUpdateContractInfo")
    public CommonResult saveOrUpdateContractInfo(@RequestBody AddContractInfoForm form) {
        ContractInfo contractInfo = ConvertUtil.convert(form, ContractInfo.class);

        String name = "";
        //获取名称
        switch (form.getType()) {
            case "0":
                name = this.customerInfoService.getById(form.getBindId()).getName();
                break;
            case "1":
                name = this.supplierInfoService.getById(form.getBindId()).getSupplierChName();
                break;
        }

        List<Long> businessTypes = form.getBusinessTypes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < businessTypes.size(); i++) {
            sb.append(businessTypes.get(i)).append(",");
        }
        contractInfo.setBusinessType(sb.toString());
        Map<String, Object> param = new HashMap<>();
        param.put("status", 1);//有效
        param.put("contract_no", form.getContractNo());
        if (form.getId() != null) {
            ContractInfo oldContractInfo = contractInfoService.getById(form.getId());
            if (oldContractInfo != null && !oldContractInfo.getContractNo().equals(form.getContractNo())) {//若修改合同号了，则校重
                List<ContractInfo> contractInfos = contractInfoService.findContractByCondition(param);
                if (contractInfos != null && contractInfos.size() > 0) {
                    return CommonResult.error(400, "该合同已经存在,不能重复录入");
                }
            }
            contractInfo.setUpdatedUser(UserOperator.getToken());
            contractInfo.setUpdatedTime(DateUtils.getNowTime());
        } else {
            List<ContractInfo> contractInfos = contractInfoService.findContractByCondition(param);
            if (contractInfos != null && contractInfos.size() > 0) {
                return CommonResult.error(400, "该合同已经存在,不能重复录入");
            }
            contractInfo.setCreatedUser(UserOperator.getToken());
        }
        //处理附件
        contractInfo.setContractUrl(StringUtils.getFileStr(form.getFileViews()));
        contractInfo.setContractName(StringUtils.getFileNameStr(form.getFileViews()));
        contractInfo.setName(name);
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
            contractInfo.setUpdatedUser(UserOperator.getToken());
            contractInfo.setStatus("0");
            contractInfos.add(contractInfo);
        }
        contractInfoService.saveOrUpdateBatch(contractInfos);
        return CommonResult.success();
    }

    @ApiOperation(value = "合同管理-下拉框合并返回")
    @PostMapping(value = "/findComboxs3")
    public CommonResult<Map<String, Object>> findComboxs3(@RequestBody Map<String, Object> param) {
        String loginUserName = MapUtil.getStr(param, "loginUserName");
        Map<String, Object> resultMap = new HashMap<>();
        //客户名称
        resultMap.put("customers", initCustomer(loginUserName).getData());
        //供应商名称
        resultMap.put("suppliers", initSupplierInfo().getData());
        //服务类型
        resultMap.put("productClassify", initProductClassify().getData());
        //法人主体
        resultMap.put("legalEntity", initLegalEntity().getData());
        return CommonResult.success(resultMap);

    }

    /**
     * 初始化下拉框
     */
    @ApiOperation(value = "合同管理-下拉框-客户名称")
    @PostMapping(value = "/initCustomer")
    public CommonResult<List<InitComboxVO>> initCustomer(String loginUserName) {

        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(loginUserName);
        List<Long> legalIds = (List<Long>)legalEntityByLegalName.getData();
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("audit_status", CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());//有效的，审核通过的客户名称
//        param.put("status", "1");
        List<CustomerInfo> customerInfos = customerInfoService.getCustomerInfoByCondition(legalIds);
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
        List<InitComboxVO> initComboxVOS = oauthClient.findLegalEntity().getData();
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

    @ApiOperation(value = "合同管理-下拉框-供应商")
    @PostMapping(value = "/initSupplierInfo")
    public CommonResult<List<InitComboxVO>> initSupplierInfo() {
        List<SupplierInfo> supplierInfos = supplierInfoService.getApprovedSupplier(
                BeanUtils.convertToFieldName(true,
                        SupplierInfo::getId, SupplierInfo::getSupplierChName));
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (SupplierInfo supplierInfo : supplierInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(supplierInfo.getId());
            initComboxVO.setName(supplierInfo.getSupplierChName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "合同管理-下拉框-服务类型")
    @PostMapping(value = "/initProductClassify")
    public CommonResult<List<InitComboxVO>> initProductClassify() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.F_ID, CommonConstant.VALUE_0);
        queryWrapper.eq(SqlConstant.STATUS, CommonConstant.VALUE_1);
        List<ProductClassify> productClassifies = productClassifyService.list(queryWrapper);
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (ProductClassify productClassify : productClassifies) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(productClassify.getId());
            initComboxVO.setName(productClassify.getName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }


}

