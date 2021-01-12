package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.AuditStatusEnum;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.enums.SettlementTypeEnum;
import com.jayud.oms.model.enums.UserTypeEnum;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.EnumVO;
import com.jayud.oms.model.vo.SupplierInfoVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.ISupplierInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

import static com.jayud.oms.model.enums.AuditStatusEnum.*;

/**
 * <p>
 * 供应商信息 前端控制器
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@RestController
@RequestMapping("/supplierInfo")
@Api(tags = "供应商接口")
@Slf4j
public class SupplierInfoController {

    @Autowired
    private ISupplierInfoService supplierInfoService;
    @Autowired
    private IAuditInfoService auditInfoService;

    @Autowired
    OauthClient oauthClient;

    @ApiOperation(value = "分页查询供应商信息列表")
    @PostMapping(value = "/findSupplierInfoByPage")
    public CommonResult<CommonPageResult<SupplierInfoVO>> findSupplierInfoByPage(@RequestBody QuerySupplierInfoForm form) {
        IPage<SupplierInfoVO> pageList = supplierInfoService.findSupplierInfoByPage(form);
        List<Long> ids = pageList.getRecords().stream().filter(tmp -> tmp.getBuyerId() != null).map(SupplierInfoVO::getBuyerId).collect(Collectors.toList());
        if (ids.size() > 0) {
            //根据id集合查询采购员
            ApiResult result = oauthClient.getUsersByIds(ids);

            JSONArray jsonArray = JSON.parseArray(JSONObject.toJSONString(result.getData()));
            Map<Long, String> map = new HashMap<>();
            for (Object obj : jsonArray) {
                JSONObject json = JSONObject.parseObject(obj.toString());
                map.put(json.getLong("id"), json.getString("userName"));
            }

            pageList.getRecords().forEach(tmp -> tmp.setBuyer(map.get(tmp.getBuyerId())));
        }
        CommonPageResult<SupplierInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "分页查询供应商审核信息(财务审核/总经办审核)")
    @PostMapping(value = "/findAuditSupplierInfoByPage")
    public CommonResult<CommonPageResult<SupplierInfoVO>> findAuditSupplierInfoByPage(@RequestBody QueryAuditSupplierInfoForm form) {
        IPage<SupplierInfoVO> pageList = supplierInfoService.findAuditSupplierInfoByPage(form);
        List<Long> ids = pageList.getRecords().stream().filter(tmp -> tmp.getBuyerId() != null).map(SupplierInfoVO::getBuyerId).collect(Collectors.toList());
        if (ids.size() > 0) {
            //根据id集合查询采购员
            ApiResult result = oauthClient.getUsersByIds(ids);

            JSONArray jsonArray = JSON.parseArray(JSONObject.toJSONString(result.getData()));
            Map<Long, String> map = new HashMap<>();
            for (Object obj : jsonArray) {
                JSONObject json = JSONObject.parseObject(obj.toString());
                map.put(json.getLong("id"), json.getString("userName"));
            }

            pageList.getRecords().forEach(tmp -> tmp.setBuyer(map.get(tmp.getBuyerId())));
        }
        CommonPageResult<SupplierInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "根据主键获取供应商信息,id是供应商id")
    @PostMapping(value = "/getSupplierInfoById")
    public CommonResult getSupplierInfoById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        SupplierInfo supplierInfo = this.supplierInfoService.getById(id);
        SupplierInfoVO supplierInfoVO = ConvertUtil.convert(supplierInfo, SupplierInfoVO.class);
        supplierInfoVO.packageProductClassifyId(supplierInfo.getProductClassifyIds());
        //审核意见
        AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfoVO.getId(), AuditTypeDescEnum.ONE.getTable());
        supplierInfoVO.setAuditComment(auditInfo.getAuditComment());

        return CommonResult.success(supplierInfoVO);
    }


    @ApiOperation(value = "新增编辑供应商")
    @PostMapping(value = "/saveOrUpdateSupplierInfo")
    public CommonResult saveOrUpdateSupplierInfo(@Valid @RequestBody AddSupplierInfoForm form) {
        SupplierInfo supplierInfo = new SupplierInfo()
                .setId(form.getId())
                .setSupplierCode(form.getSupplierCode()).setSupplierChName(form.getSupplierChName());
        if (this.supplierInfoService.checkUnique(supplierInfo)) {
            return CommonResult.error(400, "名称或代码已经存在");
        }

        //检查审核状态是否是审核通过和审核不通过，才能进行编辑
        if (form.getId() != null) {
            AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(form.getId(), AuditTypeDescEnum.ONE.getTable());
            if (!auditInfo.getAuditStatus().equals(SUCCESS.getCode())
                    && !auditInfo.getAuditStatus().equals(FAIL.getCode())) {
                return CommonResult.error(ResultEnum.CANNOT_MODIFY_IN_AUDIT);
            }
        }
        if (this.supplierInfoService.saveOrUpdateSupplierInfo(form)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "二期优化3:新增和编辑时校验供应商名称是否存在,name=供应商名称")
    @PostMapping(value = "/existSupplierName")
    public CommonResult existSupplierName(@RequestBody Map<String,Object> param) {
        String supplierName = MapUtil.getStr(param, "name");
        String idStr = (MapUtil.getStr(param,"id"));
        if(StringUtil.isNullOrEmpty(supplierName) || StringUtil.isNullOrEmpty(idStr)){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Long id = Long.valueOf(idStr);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("supplier_ch_name",supplierName);
        List<SupplierInfo> supplierInfos = supplierInfoService.list(queryWrapper);
        if((id == null && supplierInfos != null && supplierInfos.size() > 0) || (id != null && supplierInfos != null && supplierInfos.size() > 1)){
            return CommonResult.error(ResultEnum.SUPPLIER_NAME_EXIST);
        }
        return CommonResult.success();
    }

    @ApiOperation("查询结算类型枚举")
    @PostMapping(value = "/getSettlementTypeEnum")
    public CommonResult<List<EnumVO>> getSettlementTypeEnum() {

        List<EnumVO> list = new ArrayList<>();
        for (SettlementTypeEnum value : SettlementTypeEnum.values()) {
            list.add(new EnumVO(value.getCode(), value.getDesc()));
        }
        return CommonResult.success(list);
    }

    @ApiOperation("供应商审核(财务审核/总经办审核)")
    @PostMapping(value = "/auditSupplier")
    public CommonResult auditSupplier(@Valid @RequestBody AddAuditSupplierInfoForm form) {

        if (!SUCCESS.getCode().equals(form.getAuditOperation())
                && !FAIL.getCode().equals(form.getAuditOperation())) {
            log.warn("供应商审核操作类型不存在");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //获取最新供应商审核信息
        AuditInfo tmp = this.auditInfoService.getAuditInfoLatestByExtId(form.getId(), AuditTypeDescEnum.ONE.getTable());
        AuditInfo auditInfo = new AuditInfo().setId(tmp.getId()).setAuditComment(form.getAuditComment());

        //根据上一个状态进行状态扭转
        if (AuditStatusEnum.FAIL.getCode().equals(form.getAuditOperation())) {
            //审核拒绝，不继续往下审核
            auditInfo.setAuditStatus(FAIL.getCode());
        } else if (AuditStatusEnum.CW_WAIT.getCode().equals(tmp.getAuditStatus())) {
            //财务审核
            auditInfo.setAuditStatus(ZJB_WAIT.getCode());
        } else {
            //总经办审核
            auditInfo.setAuditStatus(form.getAuditOperation());
        }

        boolean isTrue = this.auditInfoService.saveOrUpdateAuditInfo(auditInfo);
        if (isTrue) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.OPR_FAIL);
    }

    @ApiOperation(value = "供应商账号管理-下拉框合并返回")
    @PostMapping(value = "/findComboxs2")
    public CommonResult<Map<String, Object>> findComboxs2() {
        Map<String, Object> resultMap = new HashMap<>();
        //角色
        resultMap.put("roles", oauthClient.findRole().getData());
        //所属公司
        resultMap.put("companys", oauthClient.getCompany().getData());
        //所属上级
        resultMap.put("departCharges", oauthClient.findCustAccount().getData());
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "供应商账号管理-分页查询")
    @PostMapping(value = "/findSupplierAccountByPage")
    public CommonResult findSupplierAccountByPage(@Valid @RequestBody QueryAccountForm form) {
        ApiResult result = this.oauthClient.findEachModuleAccountByPage(form);
        return CommonResult.success(result.getData());
    }


    @ApiOperation(value = "供应商账号管理-修改时数据回显,id=客户账号ID")
    @PostMapping(value = "/getSupplierAccountById")
    public CommonResult getSupplierAccountById(@RequestBody Map<String, Object> param) {
        Long id = MapUtil.getLong(param, "id");
        ApiResult result = oauthClient.getEachModuleAccountById(id);
        return CommonResult.success(result.getData());
    }

    @ApiOperation(value = "供应商账号管理-启用/禁用，id=客户账号ID")
    @PostMapping(value = "/enableOrDisableSupplierAccount")
    public CommonResult enableOrDisableSupplierAccount(@RequestBody Map<String, Object> param) {
        Long id = MapUtil.getLong(param, "id");
        ApiResult result = oauthClient.enableOrDisableSupplierAccount(id);
        if (HttpStatus.SC_OK == result.getCode()) {
            return CommonResult.success();
        } else {
            return CommonResult.error(result.getCode(), result.getMsg());
        }
    }

    @ApiOperation(value = "供应商账号管理-修改/编辑")
    @PostMapping(value = "/saveOrUpdateSupplierAccount")
    public CommonResult saveOrUpdateSupplierAccount(@RequestBody AddSupplierAccountForm form) {
        form.setUserType(UserTypeEnum.supplier.getCode());
        //TODO 实体参数departmentChargeId错了，不改动源代码情况，做了特殊处理，后续再更改
        AddCusAccountForm addCusAccountForm = ConvertUtil.convert(form, AddCusAccountForm.class);
        addCusAccountForm.setDepartmentChargeId(form.getSuperiorId());
        ApiResult result = oauthClient.saveOrUpdateCustAccount(addCusAccountForm);
        if (HttpStatus.SC_OK == result.getCode()) {
            return CommonResult.success();
        } else {
            return CommonResult.error(result.getCode(), result.getMsg());
        }
    }

    @ApiOperation(value = "供应商账号-所属公司")
    @PostMapping(value = "/initCompany")
    public CommonResult initCompany() {
        return CommonResult.success(oauthClient.getCompany().getData());
    }

    @Value("${address.supplierAddr}")
    private String filePath;

    @ApiOperation(value = "下载供应商模板")
    @GetMapping(value = "/downloadExcel")
    public void downloadExcel(HttpServletResponse response)throws IOException {
        //获取输入流，原始模板位置
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //假如以中文名下载的话，设置下载文件名称
        String filename = "供应商模板.xlsx";
        //转码，免得文件名中文乱码s
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename,"UTF-8"));
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }

    @ApiOperation(value = "导入供应商信息")
    @PostMapping(value = "/uploadExcel")
    public CommonResult ajaxUploadExcel(MultipartFile file, HttpServletResponse response){

        String commentHTML=null;
        try {
            commentHTML = supplierInfoService.importCustomerInfoExcel(response,file);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (com.alibaba.nacos.client.utils.StringUtils.isNotBlank(commentHTML)) {
            return CommonResult.success(commentHTML);
        }else {
            return CommonResult.error(ResultEnum.OPR_FAIL,"导入失败");
        }

    }

    @ApiOperation(value = "下载错误信息")
    @GetMapping(value = "/downloadErrorExcel")
    public void downloadErrorExcel( HttpServletResponse response)  {
        try {
            supplierInfoService.insExcel(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

