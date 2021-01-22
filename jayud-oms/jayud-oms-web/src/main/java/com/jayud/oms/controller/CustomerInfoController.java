package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import com.jayud.oms.model.enums.RoleKeyEnum;
import com.jayud.oms.model.enums.UserTypeEnum;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.CustAccountVO;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.ICustomerInfoService;
import com.jayud.oms.service.ICustomerRelaLegalService;
import com.jayud.oms.service.ICustomerRelaUnitService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
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

    @Autowired
    private IAuditInfoService auditInfoService;

    @Autowired
    private ICustomerRelaUnitService customerRelaUnitService;

    @Autowired
    private ICustomerRelaLegalService customerRelaLegalService;


    @ApiOperation(value = "查询客户列表")
    @PostMapping(value = "/findCustomerInfoByPage")
    public CommonResult<CommonPageResult<CustomerInfoVO>> findCustomerInfoByPage(@RequestBody QueryCustomerInfoForm form) {
        IPage<CustomerInfoVO> pageList = customerInfoService.findCustomerInfoByPage(form);
        CommonPageResult<CustomerInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "分页查询客户基本信息列表")
    @PostMapping(value = "/findCustomerBasicsInfoByPage")
    public CommonResult<CommonPageResult<CustomerInfoVO>> findCustomerBasicsInfoByPage(@RequestBody QueryCustomerInfoForm form) {
        IPage<CustomerInfoVO> pageList = customerInfoService.findCustomerBasicsInfoByPage(form);
        CommonPageResult<CustomerInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查看客户详情和编辑时数据回显,id=客户ID")
    @PostMapping(value = "/getCustomerInfoById")
    public CommonResult<CustomerInfoVO> getCustomerInfoById(@RequestBody Map<String, Object> param) {
        String id = MapUtil.getStr(param, "id");
        if (StringUtil.isNullOrEmpty(id)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        return CommonResult.success(customerInfoService.getCustomerInfoById(Long.valueOf(id)));
    }

    @ApiOperation(value = "新增编辑客户")
    @PostMapping(value = "/saveOrUpdateCustomerInfo")
    public CommonResult saveOrUpdateCustomerInfo(@RequestBody @Valid AddCustomerInfoForm form) {
        //校验客户代码的唯一性
        if (!StringUtils.isEmpty(form.getIdCode())) {
            List<CustomerInfoVO> oldCustomerInfos = customerInfoService.existCustomerInfo(form.getIdCode());
            if ((form.getId() == null && oldCustomerInfos != null && oldCustomerInfos.size() > 0)
                    || (form.getId() != null && oldCustomerInfos != null && oldCustomerInfos.size() > 1)) {
                return CommonResult.error(ResultEnum.CUSTOMER_CODE_EXIST);
            }
        }


        CustomerInfo customerInfo = ConvertUtil.convert(form, CustomerInfo.class);
//        //空字符串设置null
//        form.setIdCode(StringUtils.isEmpty(form.getIdCode()) ? null : form.getIdCode());
//        if (form.getId() != null) {
//            customerInfo.setUpdatedUser(form.getLoginUserName());
//            customerInfo.setUpdatedTime(DateUtils.getNowTime());
//        } else {
//            customerInfo.setCreatedUser(form.getLoginUserName());
//        }
        this.customerInfoService.saveOrUpdateCustomerInfo(form,customerInfo);

        return CommonResult.success();
    }

    @ApiOperation(value = "二期优化3:新增和编辑时校验客户名称是否存在,name=客户姓名 id=客户ID")
    @PostMapping(value = "/existCustomerName")
    public CommonResult existCustomerName(@RequestBody Map<String, Object> param) {
        String customerName = MapUtil.getStr(param, "name");
        String id = MapUtil.getStr(param, "id");
        if (StringUtil.isNullOrEmpty(customerName)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name", customerName);
        List<CustomerInfo> customerInfos = customerInfoService.list(queryWrapper);
        if ((StringUtil.isNullOrEmpty(id) && customerInfos != null && customerInfos.size() > 0) || ((!StringUtil.isNullOrEmpty(id)) && customerInfos != null && customerInfos.size() > 1)) {
            return CommonResult.error(ResultEnum.CUSTOMER_NAME_EXIST);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "二期优化3:删除客户信息(禁用)")
    @PostMapping(value = "/delCustomerInfo")
    public CommonResult delCustomerInfo(@RequestBody Map<String, Object> param) {
        String idStr = MapUtil.getStr(param, "id");
        String tempStatus = MapUtil.getStr(param, "status");
        if (StringUtil.isNullOrEmpty(idStr) || StringUtil.isNullOrEmpty(tempStatus)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        String status;
        Boolean isStatus = Boolean.valueOf(tempStatus);
        if (isStatus) {
            status = "1";
        } else {
            status = "0";
        }
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(Long.parseLong(idStr));
        customerInfo.setUpdatedTime(DateUtils.getNowTime());
        customerInfo.setUpdatedUser(UserOperator.getToken());
        customerInfo.setStatus(status);
        Boolean result = customerInfoService.saveOrUpdate(customerInfo);
        if (result) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.OPR_FAIL);
    }

    @ApiOperation(value = "二期优化3:已关联客户(结算单位)列表,id = 客户ID")
    @PostMapping(value = "/relateUnitList")
    public CommonResult<List<CustomerInfoVO>> relateUnitList(@RequestBody Map<String, Object> param) {
        String idStr = MapUtil.getStr(param, "id");
        if (StringUtil.isNullOrEmpty(idStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<CustomerInfoVO> customerInfoVOS = customerInfoService.relateUnitList(Long.parseLong(idStr));
        return CommonResult.success(customerInfoVOS);
    }

    @ApiOperation(value = "二期优化3:删除已关联客户(结算单位)列表,customerInfoId=客户ID,unitId=关联客户ID")
    @PostMapping(value = "/delRelateUnitList")
    public CommonResult delRelateUnitList(@RequestBody Map<String, Object> param) {
        String customerInfoIdStr = MapUtil.getStr(param, "customerInfoId");
        String unitIdStr = MapUtil.getStr(param, "unitId");
        if (StringUtil.isNullOrEmpty(customerInfoIdStr) || StringUtil.isNullOrEmpty(unitIdStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryWrapper removeWrapper = new QueryWrapper();
        removeWrapper.eq("customer_info_id", Long.parseLong(customerInfoIdStr));
        removeWrapper.eq("unit_id", Long.parseLong(unitIdStr));
        customerRelaUnitService.remove(removeWrapper);
        return CommonResult.success();
    }

    @ApiOperation(value = "二期优化3:关联客户(结算单位)列表")
    @PostMapping(value = "/findRelateUnitList")
    public CommonResult<List<CustomerInfoVO>> findRelateUnitList(@RequestBody QueryRelUnitInfoListForm form) {
        List<CustomerInfoVO> customerInfoVOS = customerInfoService.findRelateUnitList(form);
        return CommonResult.success(customerInfoVOS);
    }


    @ApiOperation(value = "二期优化3:确认关联客户(结算单位)")
    @PostMapping(value = "/confirmRelateUnit")
    public CommonResult<List<CustomerInfoVO>> confirmRelateUnit(@RequestBody ConfirmRelateUnitForm form) {
        Boolean result = customerRelaUnitService.confirmRelateUnit(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "审核客户信息")
    @PostMapping(value = "/auditCustomerInfo")
    public CommonResult auditCustomerInfo(@RequestBody AuditCustomerInfoForm form) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(form.getId());
        customerInfo.setUpdatedTime(DateUtils.getNowTime());
        customerInfo.setUpdatedUser(UserOperator.getToken());
        CustomerInfoVO customerInfoVO = customerInfoService.getCustomerInfoById(form.getId());
        String auditStatus = String.valueOf(customerInfoVO.getAuditStatus());
        if (auditStatus == null) {
            return CommonResult.error(400, "不属于审核状态流程");
        }
        if (form.getAuditStatus() == null || "".equals(form.getAuditStatus())) {
            return CommonResult.error(400, "参数不合法");
        }
        //记录操作信息
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setExtDesc(SqlConstant.CUSTOMER_INFO);
        auditInfo.setExtId(form.getId());
        auditInfo.setCreatedUser(UserOperator.getToken());
        auditInfo.setAuditComment(form.getAuditComment());
        if ("0".equals(form.getAuditStatus())) {//审核拒绝
            customerInfo.setAuditStatus(CustomerInfoStatusEnum.AUDIT_FAIL.getCode());
            customerInfo.setAuditComment(form.getAuditComment());
            auditInfo.setAuditStatus(CustomerInfoStatusEnum.AUDIT_FAIL.getCode());
            auditInfo.setAuditTypeDesc(CustomerInfoStatusEnum.AUDIT_FAIL.getDesc());
        } else if ("1".equals(form.getAuditStatus())) {//审核状态
            if (CustomerInfoStatusEnum.KF_WAIT_AUDIT.getCode().equals(auditStatus)) {//客服审核流程
                customerInfo.setAuditStatus(CustomerInfoStatusEnum.CW_WAIT_AUDIT.getCode());
                auditInfo.setAuditStatus(CustomerInfoStatusEnum.CW_WAIT_AUDIT.getCode());
                auditInfo.setAuditTypeDesc(CustomerInfoStatusEnum.CW_WAIT_AUDIT.getDesc());
            } else if (CustomerInfoStatusEnum.CW_WAIT_AUDIT.getCode().equals(auditStatus)) {//财务审核流程
                //客户代码此处必填项
                if (StringUtil.isNullOrEmpty(form.getIdCode())) {
                    return CommonResult.error(ResultEnum.OPR_FAIL);
                }
                //TODO 判断是否存在客户代码
                if (this.customerInfoService.exitCode(customerInfo.getId(), form.getIdCode())) {
                    return CommonResult.error(400, "客户代码已存在");
                }
                customerInfo.setIdCode(form.getIdCode());
                customerInfo.setAuditStatus(CustomerInfoStatusEnum.ZJB_WAIT_AUDIT.getCode());
                auditInfo.setAuditStatus(CustomerInfoStatusEnum.ZJB_WAIT_AUDIT.getCode());
                auditInfo.setAuditTypeDesc(CustomerInfoStatusEnum.ZJB_WAIT_AUDIT.getDesc());
            } else if (CustomerInfoStatusEnum.ZJB_WAIT_AUDIT.getCode().equals(auditStatus)) {//总经办审核
                customerInfo.setAuditStatus(CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
                auditInfo.setAuditStatus(CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
                auditInfo.setAuditTypeDesc(CustomerInfoStatusEnum.AUDIT_SUCCESS.getDesc());
            }
        }
        customerInfoService.updateById(customerInfo);
        auditInfoService.save(auditInfo);
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑及审核客户代码是否可填 id = 客户ID")
    @PostMapping(value = "/isFillCustomerCode")
    public CommonResult<Boolean> isFillCustomerCode(@RequestBody Map<String, Object> param) {
        String customerInfoIdStr = MapUtil.getStr(param, "id");
        if (StringUtil.isNullOrEmpty(customerInfoIdStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ext_id", Long.parseLong(customerInfoIdStr));
        queryWrapper.eq("ext_desc", SqlConstant.CUSTOMER_INFO);
        queryWrapper.eq("audit_status", CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
        List<AuditInfo> auditInfos = auditInfoService.list(queryWrapper);
        if (auditInfos != null && auditInfos.size() > 0) {
            return CommonResult.success(false);
        }
        return CommonResult.success(true);
    }

    @ApiOperation(value = "客户账号管理-修改时数据回显,id=客户账号ID")
    @PostMapping(value = "/getCustomerAccountInfo")
    public CommonResult<CustAccountVO> getCustomerAccountInfo(@RequestBody Map<String, Object> param) {
        String id = MapUtil.getStr(param, "id");
        param = new HashMap<>();
        param.put("id", id);
        CustAccountVO custAccountVO = customerInfoService.getCustAccountByCondition(param);
        return CommonResult.success(custAccountVO);
    }

    @ApiOperation(value = "客户账号管理-删除，id=客户账号ID")
    @PostMapping(value = "/delCustomerAccountInfo")
    public CommonResult delCustomerAccountInfo(@RequestBody Map<String, Object> param) {
        String id = MapUtil.getStr(param, "id");
        oauthClient.delCustAccount(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "客户账号管理-修改/编辑")
    @PostMapping(value = "/saveOrUpdateCusAccountInfo")
    public CommonResult saveOrUpdateCusAccountInfo(@RequestBody AddCusAccountForm form) {
        form.setUserType(UserTypeEnum.customer.getCode());
        ApiResult result = oauthClient.saveOrUpdateCustAccount(form);
        if (HttpStatus.SC_OK == result.getCode()) {
            return CommonResult.success();
        } else {
            return CommonResult.error(result.getCode(), result.getMsg());
        }
    }

    @ApiOperation(value = "客户账号管理-分页获取数据")
    @PostMapping(value = "/findCusAccountByPage")
    public CommonResult<CommonPageResult<CustAccountVO>> findCusAccountByPage(@RequestBody QueryCusAccountForm form) {
        IPage<CustAccountVO> pageList = customerInfoService.findCustAccountByPage(form);
        CommonPageResult<CustAccountVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "客户列表新增-下拉框合并返回")
    @PostMapping(value = "/findComboxs1")
    public CommonResult<Map<String, Object>> findComboxs1() {
        Map<String, Object> resultMap = new HashMap<>();
        //接单部门
        resultMap.put("jiedanDepart", initDepartment());
        //接单客服
        resultMap.put("jiedanKF", initKfs());
        //业务员
        resultMap.put("yws", initYws());
        return CommonResult.success(resultMap);

    }

    @ApiOperation(value = "客户账号管理-下拉框合并返回")
    @PostMapping(value = "/findComboxs2")
    public CommonResult<Map<String, Object>> findComboxs2() {
        Map<String, Object> resultMap = new HashMap<>();
        //角色
        resultMap.put("roles", initRole());
        //所属公司
        resultMap.put("companys", initCompany());
        //所属上级
        resultMap.put("departCharges", initDepartCharge());
        return CommonResult.success(resultMap);

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
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.CUSTOMER_SERVICE.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "客户列表-新增-业务员")
    @PostMapping(value = "/initYws")
    public CommonResult<List<InitComboxVO>> initYws() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findUserByKey(RoleKeyEnum.BUSINESS_MANAGER.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "供应商账号-新增-角色")
    @PostMapping(value = "/initRole")
    public CommonResult<List<InitComboxVO>> initRole() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findRole().getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "供应商账号-新增-所属公司")
    @PostMapping(value = "/initCompany")
    public CommonResult<List<InitComboxVO>> initCompany() {
        List<CustomerInfo> customerInfos = customerInfoService.findCustomerInfoByCondition(new HashMap<>());
        List<InitComboxVO> initComboxVOS = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfos) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(customerInfo.getId());
            initComboxVO.setName(customerInfo.getName());
            initComboxVOS.add(initComboxVO);
        }
        return CommonResult.success(initComboxVOS);
    }


    @ApiOperation(value = "供应商账号-新增-所属上级")
    @PostMapping(value = "/initDepartCharge")
    public CommonResult<List<InitComboxVO>> initDepartCharge() {
        List<InitComboxVO> initComboxVOS = (List<InitComboxVO>) oauthClient.findCustAccount().getData();
        return CommonResult.success(initComboxVOS);
    }

    @Value("${address.productAddr}")
    private String filePath;

    @ApiOperation(value = "下载客户模板")
    @GetMapping(value = "/downloadExcel")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        //获取输入流，原始模板位置
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //假如以中文名下载的话，设置下载文件名称
        String filename = "客户模板.xls";
        //转码，免得文件名中文乱码
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }

    @ApiOperation(value = "导入客户信息")
    @PostMapping(value = "/uploadExcel")
    public CommonResult ajaxUploadExcel(MultipartFile file, HttpServletResponse response, @RequestParam("userName") String userName) {

        System.out.println("userName:=======" + userName);
        String commentHTML = null;
        try {
            commentHTML = customerInfoService.importCustomerInfoExcel(response, file, userName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (StringUtils.isNotBlank(commentHTML)) {
            return CommonResult.success(commentHTML);
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL, "导入失败");
        }
    }

    @ApiOperation(value = "下载错误信息")
    @GetMapping(value = "/downloadErrorExcel")
    public void downloadErrorExcel(HttpServletResponse response, @RequestParam("userName") String userName) {
        try {
            customerInfoService.insExcel(response, userName);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @ApiOperation(value = "判断是否有错误信息")
    @PostMapping(value = "/checkMes")
    public CommonResult checkMes(@RequestParam("userName") String userName) {
        boolean result = customerInfoService.checkMes(userName);
        return CommonResult.success(result);
    }


}

