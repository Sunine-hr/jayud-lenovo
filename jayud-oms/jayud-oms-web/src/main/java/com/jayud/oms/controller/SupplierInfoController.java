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
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.BeanUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.enums.*;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.CustomerInfoVO;
import com.jayud.oms.model.vo.EnumVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.model.vo.SupplierInfoVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.ISupplierInfoService;
import com.jayud.oms.service.ISupplierRelaLegalService;
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
    private ISupplierRelaLegalService supplierRelaLegalService;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    private FileClient fileClient;

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
        List<Long> longs = supplierRelaLegalService.getList(supplierInfo.getId());
        SupplierInfoVO supplierInfoVO = ConvertUtil.convert(supplierInfo, SupplierInfoVO.class);
        supplierInfoVO.packageProductClassifyId(supplierInfo.getProductClassifyIds());
        supplierInfoVO.setLegalEntityIds(longs);
        supplierInfoVO.assembleAccessories(fileClient.getBaseUrl().getData());
        //审核意见
        AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfoVO.getId(), AuditTypeDescEnum.ONE.getTable());
        supplierInfoVO.setAuditComment(auditInfo.getAuditComment());


        return CommonResult.success(supplierInfoVO);
    }


    @ApiOperation(value = "新增编辑供应商")
    @PostMapping(value = "/saveOrUpdateSupplierInfo")
    public CommonResult saveOrUpdateSupplierInfo(@Valid @RequestBody AddSupplierInfoForm form) {
        //校验客户代码的唯一性
        if (!com.alibaba.nacos.client.utils.StringUtils.isEmpty(form.getSupplierCode())) {
            List<SupplierInfo> supplierInfoVOS = supplierInfoService.existSupplierInfo(form.getSupplierCode());
            if ((form.getId() == null && supplierInfoVOS != null && supplierInfoVOS.size() > 0)
                    || (form.getId() != null && supplierInfoVOS != null && supplierInfoVOS.size() > 1)) {
                return CommonResult.error(400, "该供应商代码已存在");
            }
        }
        form.checkAddr();
        //校验客户名称是否唯一性
        if (this.supplierInfoService.exitName(form.getId(), form.getSupplierChName())) {
            return CommonResult.error(400, "该供应商名称已存在");
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

    @ApiOperation(value = "二期优化3:新增和编辑时校验供应商名称是否存在,name=供应商名称 id=供应商ID")
    @PostMapping(value = "/existSupplierName")
    public CommonResult existSupplierName(@RequestBody Map<String, Object> param) {
        String supplierName = MapUtil.getStr(param, "name");
        String idStr = (MapUtil.getStr(param, "id"));
        if (StringUtil.isNullOrEmpty(supplierName)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryWrapper<SupplierInfo> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SupplierInfo::getSupplierChName, supplierName);
        List<SupplierInfo> supplierInfos = supplierInfoService.list(queryWrapper);
        if ((StringUtil.isNullOrEmpty(idStr) && supplierInfos != null && supplierInfos.size() > 0) || ((!StringUtil.isNullOrEmpty(idStr)) && supplierInfos != null && supplierInfos.size() > 1)) {
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

        //获取供应商信息
        SupplierInfo supplierInfo1 = supplierInfoService.getById(form.getId());

        //获取最新供应商审核信息
        AuditInfo tmp = this.auditInfoService.getAuditInfoLatestByExtId(form.getId(), AuditTypeDescEnum.ONE.getTable());
        AuditInfo auditInfo = new AuditInfo().setId(tmp.getId()).setAuditComment(form.getAuditComment());

        //根据上一个状态进行状态扭转
        if (AuditStatusEnum.FAIL.getCode().equals(form.getAuditOperation())) {
            //审核拒绝，不继续往下审核
            auditInfo.setAuditStatus(FAIL.getCode());
        } else if (AuditStatusEnum.CW_WAIT.getCode().equals(tmp.getAuditStatus())) {

            //供应商代码此处必填项
            if (StringUtil.isNullOrEmpty(form.getSupplierCode())) {
                return CommonResult.error(400, "供应商代码未填写");
            }
            if (form.getSupplierCode() != supplierInfo1.getSupplierCode()) {
                if (this.supplierInfoService.exitCode(form.getId(), form.getSupplierCode())) {
                    return CommonResult.error(400, "供应商代码已存在");
                }

                //更新供应商代码
                SupplierInfo supplierInfo = new SupplierInfo();
                supplierInfo.setId(form.getId());
                supplierInfo.setSupplierCode(form.getSupplierCode());
                boolean b = supplierInfoService.saveOrUpdate(supplierInfo);
                if (!b) {
                    return CommonResult.error(400, "供应商代码修改失败");
                }
            }

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

    @ApiOperation(value = "编辑及审核客户代码是否可填 id = 客户ID")
    @PostMapping(value = "/isFillSupplierCode")
    public CommonResult<Boolean> isFillSupplierCode(@RequestBody Map<String, Object> param) {
        String supplierInfoIdStr = MapUtil.getStr(param, "id");
        if (StringUtil.isNullOrEmpty(supplierInfoIdStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ext_id", Long.parseLong(supplierInfoIdStr));
        queryWrapper.eq("ext_desc", AuditTypeDescEnum.ONE.getTable());
        queryWrapper.eq("audit_status", CustomerInfoStatusEnum.AUDIT_SUCCESS.getCode());
        List<AuditInfo> auditInfos = auditInfoService.list(queryWrapper);
        if (auditInfos != null && auditInfos.size() > 0) {
            return CommonResult.success(false);
        }
        return CommonResult.success(true);
    }


    @ApiOperation(value = "供应商账号管理-下拉框合并返回")
    @PostMapping(value = "/findComboxs2")
    public CommonResult<Map<String, Object>> findComboxs2() {
        Map<String, Object> resultMap = new HashMap<>();
        //角色
        resultMap.put("roles", oauthClient.findRole().getData());
        //所属公司
        resultMap.put("companys", initCompany());
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


    @Value("${address.supplierAddr}")
    private String filePath;

    @ApiOperation(value = "下载供应商模板")
    @GetMapping(value = "/downloadExcel")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        //获取输入流，原始模板位置
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //假如以中文名下载的话，设置下载文件名称
        String filename = "供应商模板.xlsx";
        //转码，免得文件名中文乱码s
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

    @ApiOperation(value = "导入供应商信息")
    @PostMapping(value = "/uploadExcel")
    public CommonResult ajaxUploadExcel(MultipartFile file, HttpServletResponse response, @RequestParam("userName") String userName) {

        String commentHTML = null;
        try {
            commentHTML = supplierInfoService.importCustomerInfoExcel(response, file, userName);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (com.alibaba.nacos.client.utils.StringUtils.isNotBlank(commentHTML)) {
            return CommonResult.success(commentHTML);
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL, "导入失败");
        }

    }

    @ApiOperation(value = "下载错误信息")
    @GetMapping(value = "/downloadErrorExcel")
    public void downloadErrorExcel(HttpServletResponse response, @RequestParam("userName") String userName) {
        try {
            supplierInfoService.insExcel(response, userName);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @ApiOperation(value = "判断是否有错误信息")
    @PostMapping(value = "/checkMes")
    public CommonResult checkMes(@RequestBody Map<String, Object> param) {
        String userName = MapUtil.getStr(param, "loginUserName");
        boolean result = supplierInfoService.checkMes(userName);
        return CommonResult.success(result);
    }

    /**
     * 轨迹管理，实时定位，查询供应商及其车辆tree
     */
    @ApiOperation("查询供应商及其车辆tree")
    @PostMapping(value = "/getSupplierVehicleTree")
    public CommonResult getSupplierVehicleTree(){
        List<Map<String, Object>> supplierVehicleTree = supplierInfoService.getSupplierVehicleTree();
        return CommonResult.success(supplierVehicleTree);
    }

    /**
     * 轨迹管理，轨迹回放，按车辆，查询供应商list
     */
    @ApiOperation("查询供应商list")
    @PostMapping(value = "/getList")
    public CommonResult getList(){
        List<Map<String, Object>> list = supplierInfoService.getList();
        return CommonResult.success(list);
    }

    /**
     * 轨迹管理，轨迹回放，按车辆，获取单据类型
     */
    @ApiOperation("获取单据类型")
    @PostMapping(value = "/getOrderSignEnum")
    public CommonResult getOrderSignEnum(){
        List<Map<String, Object>> list = new ArrayList<>();
        //ZGYS("zgys", "order_transport", "中港运输"),
        Map<String, Object> zgys = new HashMap<>();
        zgys.put("code", SubOrderSignEnum.ZGYS.getSignTwo());
        zgys.put("text", SubOrderSignEnum.ZGYS.getDesc());
        list.add(zgys);
        //NL("nl", "order_inland_transport", "内陆运输"),
        Map<String, Object> nl = new HashMap<>();
        nl.put("code", SubOrderSignEnum.NL.getSignTwo());
        nl.put("text", SubOrderSignEnum.NL.getDesc());
        list.add(nl);
        return CommonResult.success(list);
    }

    /**
     * 轨迹管理，轨迹回放，按单据，查询订单tree
     */
    @ApiOperation("查询订单tree")
    @PostMapping(value = "/getOrderTree")
    public CommonResult getOrderTree(@RequestBody Map<String, Object> param){
        String orderTypeCode = MapUtil.getStr(param, "orderTypeCode");//单据类型
        String pickUpTimeStart = MapUtil.getStr(param, "pickUpTimeStart");//提货时间Start
        String pickUpTimeEnd = MapUtil.getStr(param, "pickUpTimeEnd");//提货时间End
        String orderNo = MapUtil.getStr(param, "orderNo");//订单号
        List<Map<String, Object>> orderTree = supplierInfoService.getOrderTree(orderTypeCode, pickUpTimeStart, pickUpTimeEnd, orderNo);
        return CommonResult.success(orderTree);
    }


}

