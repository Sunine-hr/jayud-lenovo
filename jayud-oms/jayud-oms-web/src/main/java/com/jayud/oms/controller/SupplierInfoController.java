package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
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
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jayud.oms.model.enums.AuditStatusEnum.*;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author ?????????
 * @since 2020-10-29
 */
@RestController
@RequestMapping("/supplierInfo")
@Api(tags = "???????????????")
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

    @ApiOperation(value = "?????????????????????????????????")
    @PostMapping(value = "/findSupplierInfoByPage")
    public CommonResult<CommonPageResult<SupplierInfoVO>> findSupplierInfoByPage(@RequestBody QuerySupplierInfoForm form) {
        IPage<SupplierInfoVO> pageList = supplierInfoService.findSupplierInfoByPage(form);
        List<Long> ids = pageList.getRecords().stream().filter(tmp -> tmp.getBuyerId() != null).map(SupplierInfoVO::getBuyerId).collect(Collectors.toList());
        if (ids.size() > 0) {
            //??????id?????????????????????
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

    @ApiOperation(value = "?????????????????????????????????(????????????/???????????????)")
    @PostMapping(value = "/findAuditSupplierInfoByPage")
    public CommonResult<CommonPageResult<SupplierInfoVO>> findAuditSupplierInfoByPage(@RequestBody QueryAuditSupplierInfoForm form) {
        IPage<SupplierInfoVO> pageList = supplierInfoService.findAuditSupplierInfoByPage(form);
        List<Long> ids = pageList.getRecords().stream().filter(tmp -> tmp.getBuyerId() != null).map(SupplierInfoVO::getBuyerId).collect(Collectors.toList());
        if (ids.size() > 0) {
            //??????id?????????????????????
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

    @ApiOperation(value = "?????????????????????????????????,id????????????id")
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
        //????????????
        AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(supplierInfoVO.getId(), AuditTypeDescEnum.ONE.getTable());
        supplierInfoVO.setAuditComment(auditInfo.getAuditComment());


        return CommonResult.success(supplierInfoVO);
    }


    @ApiOperation(value = "?????????????????????")
    @PostMapping(value = "/saveOrUpdateSupplierInfo")
    public CommonResult saveOrUpdateSupplierInfo(@Valid @RequestBody AddSupplierInfoForm form) {
        //??????????????????????????????
        if (!com.alibaba.nacos.client.utils.StringUtils.isEmpty(form.getSupplierCode())) {
            List<SupplierInfo> supplierInfoVOS = supplierInfoService.existSupplierInfo(form.getSupplierCode());
            if ((form.getId() == null && supplierInfoVOS != null && supplierInfoVOS.size() > 0)
                    || (form.getId() != null && supplierInfoVOS != null && supplierInfoVOS.size() > 1)) {
                return CommonResult.error(400, "???????????????????????????");
            }
        }
        form.checkAddr();
        //?????????????????????????????????
        if (this.supplierInfoService.exitName(form.getId(), form.getSupplierChName())) {
            return CommonResult.error(400, "???????????????????????????");
        }

        //??????????????????????????????????????????????????????????????????????????????
        if (form.getId() != null) {
            AuditInfo auditInfo = this.auditInfoService.getAuditInfoLatestByExtId(form.getId(), AuditTypeDescEnum.ONE.getTable());
            if (!auditInfo.getAuditStatus().equals(SUCCESS.getCode())
                    && !auditInfo.getAuditStatus().equals(FAIL.getCode())) {
                return CommonResult.error(ResultEnum.CANNOT_MODIFY_IN_AUDIT);
            }
        }
        Long aLong = this.supplierInfoService.saveOrUpdateSupplierInfo(form);
        if (aLong!=null) {
            return CommonResult.success(aLong);
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "????????????3:???????????????????????????????????????????????????,name=??????????????? id=?????????ID")
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

    @ApiOperation("????????????????????????")
    @PostMapping(value = "/getSettlementTypeEnum")
    public CommonResult<List<EnumVO>> getSettlementTypeEnum() {

        List<EnumVO> list = new ArrayList<>();
        for (SettlementTypeEnum value : SettlementTypeEnum.values()) {
            list.add(new EnumVO(value.getCode(), value.getDesc()));
        }
        return CommonResult.success(list);
    }

    @ApiOperation("???????????????(????????????/???????????????)")
    @PostMapping(value = "/auditSupplier")
    public CommonResult auditSupplier(@Valid @RequestBody AddAuditSupplierInfoForm form) {

        if (!SUCCESS.getCode().equals(form.getAuditOperation())
                && !FAIL.getCode().equals(form.getAuditOperation())) {
            log.warn("????????????????????????????????????");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //?????????????????????
        SupplierInfo supplierInfo1 = supplierInfoService.getById(form.getId());

        //?????????????????????????????????
        AuditInfo tmp = this.auditInfoService.getAuditInfoLatestByExtId(form.getId(), AuditTypeDescEnum.ONE.getTable());
        AuditInfo auditInfo = new AuditInfo().setId(tmp.getId()).setAuditComment(form.getAuditComment());

        //???????????????????????????????????????
        if (AuditStatusEnum.FAIL.getCode().equals(form.getAuditOperation())) {
            //????????????????????????????????????
            auditInfo.setAuditStatus(FAIL.getCode());
        } else if (AuditStatusEnum.CW_WAIT.getCode().equals(tmp.getAuditStatus())) {

            //??????????????????????????????
            if (StringUtil.isNullOrEmpty(form.getSupplierCode())) {
                return CommonResult.error(400, "????????????????????????");
            }
            if (form.getSupplierCode() != supplierInfo1.getSupplierCode()) {
                if (this.supplierInfoService.exitCode(form.getId(), form.getSupplierCode())) {
                    return CommonResult.error(400, "????????????????????????");
                }

                //?????????????????????
                SupplierInfo supplierInfo = new SupplierInfo();
                supplierInfo.setId(form.getId());
                supplierInfo.setSupplierCode(form.getSupplierCode());
                boolean b = supplierInfoService.saveOrUpdate(supplierInfo);
                if (!b) {
                    return CommonResult.error(400, "???????????????????????????");
                }
            }

            //????????????
            auditInfo.setAuditStatus(ZJB_WAIT.getCode());
        } else {
            //???????????????
            auditInfo.setAuditStatus(form.getAuditOperation());
        }

        boolean isTrue = this.auditInfoService.saveOrUpdateAuditInfo(auditInfo);
        if (isTrue) {
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.OPR_FAIL);
    }

    @ApiOperation(value = "??????????????????????????????????????? id = ??????ID")
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


    @ApiOperation(value = "?????????????????????-?????????????????????")
    @PostMapping(value = "/findComboxs2")
    public CommonResult<Map<String, Object>> findComboxs2() {
        Map<String, Object> resultMap = new HashMap<>();
        //??????
        resultMap.put("roles", oauthClient.findRole().getData());
        //????????????
        resultMap.put("companys", initCompany());
        //????????????
        resultMap.put("departCharges", oauthClient.findCustAccount().getData());
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "?????????????????????-????????????")
    @PostMapping(value = "/findSupplierAccountByPage")
    public CommonResult findSupplierAccountByPage(@Valid @RequestBody QueryAccountForm form) {
        ApiResult result = this.oauthClient.findEachModuleAccountByPage(form);
        return CommonResult.success(result.getData());
    }


    @ApiOperation(value = "?????????????????????-?????????????????????,id=????????????ID")
    @PostMapping(value = "/getSupplierAccountById")
    public CommonResult getSupplierAccountById(@RequestBody Map<String, Object> param) {
        Long id = MapUtil.getLong(param, "id");
        ApiResult result = oauthClient.getEachModuleAccountById(id);
        return CommonResult.success(result.getData());
    }

    @ApiOperation(value = "?????????????????????-??????/?????????id=????????????ID")
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

    @ApiOperation(value = "?????????????????????-??????/??????")
    @PostMapping(value = "/saveOrUpdateSupplierAccount")
    public CommonResult saveOrUpdateSupplierAccount(@RequestBody AddSupplierAccountForm form) {
        form.setUserType(UserTypeEnum.supplier.getCode());
        //TODO ????????????departmentChargeId????????????????????????????????????????????????????????????????????????
        AddCusAccountForm addCusAccountForm = ConvertUtil.convert(form, AddCusAccountForm.class);
        addCusAccountForm.setDepartmentChargeId(form.getSuperiorId());
        ApiResult result = oauthClient.saveOrUpdateCustAccount(addCusAccountForm);
        if (HttpStatus.SC_OK == result.getCode()) {
            return CommonResult.success();
        } else {
            return CommonResult.error(result.getCode(), result.getMsg());
        }
    }

    @ApiOperation(value = "???????????????-????????????")
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

    @ApiOperation(value = "?????????????????????")
    @GetMapping(value = "/downloadExcel")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        //????????????????????????????????????
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //?????????????????????????????????????????????????????????
        String filename = "???????????????.xlsx";
        //????????????????????????????????????s
        //?????????????????????
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        //1.????????????ContentType?????????????????????????????????????????????????????????
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }

    @ApiOperation(value = "?????????????????????")
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
            return CommonResult.error(ResultEnum.OPR_FAIL, "????????????");
        }

    }

    @ApiOperation(value = "??????????????????")
    @GetMapping(value = "/downloadErrorExcel")
    public void downloadErrorExcel(HttpServletResponse response, @RequestParam("userName") String userName) {
        try {
            supplierInfoService.insExcel(response, userName);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @ApiOperation(value = "???????????????????????????")
    @PostMapping(value = "/checkMes")
    public CommonResult checkMes(@RequestBody Map<String, Object> param) {
        String userName = MapUtil.getStr(param, "loginUserName");
        boolean result = supplierInfoService.checkMes(userName);
        return CommonResult.success(result);
    }

    /**
     * ?????????????????????????????????????????????????????????tree
     */
    @ApiOperation("???????????????????????????tree")
    @PostMapping(value = "/getSupplierVehicleTree")
    public CommonResult getSupplierVehicleTree(@RequestBody Map<String, Object> param){
        List<Map<String, Object>> supplierVehicleTree = supplierInfoService.getSupplierVehicleTree(param);
        return CommonResult.success(supplierVehicleTree);
    }

    /**
     * ?????????????????????????????????????????????????????????list????????????????????????????????????
     */
    @ApiOperation("???????????????list")
    @PostMapping(value = "/getList")
    public CommonResult getList(){
        List<Map<String, Object>> list = supplierInfoService.getList();
        return CommonResult.success(list);
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @ApiOperation("??????????????????")
    @PostMapping(value = "/getOrderSignEnum")
    public CommonResult getOrderSignEnum(){
        List<Map<String, Object>> list = new ArrayList<>();
        //ZGYS("zgys", "order_transport", "????????????"),
        Map<String, Object> zgys = new HashMap<>();
        zgys.put("code", SubOrderSignEnum.ZGYS.getSignTwo());
        zgys.put("text", SubOrderSignEnum.ZGYS.getDesc());
        list.add(zgys);
        //NL("nl", "order_inland_transport", "????????????"),
        Map<String, Object> nl = new HashMap<>();
        nl.put("code", SubOrderSignEnum.NL.getSignTwo());
        nl.put("text", SubOrderSignEnum.NL.getDesc());
        list.add(nl);
        return CommonResult.success(list);
    }

    /**
     * ??????????????????????????????????????????????????????tree
     */
    @ApiOperation("????????????tree")
    @PostMapping(value = "/getOrderTree")
    public CommonResult getOrderTree(@RequestBody Map<String, Object> param){
        String orderTypeCode = MapUtil.getStr(param, "orderTypeCode");//????????????

        //???????????? ??????~??????
        String pickUpTimeStart = "";
        String pickUpTimeEnd = "";
        ArrayList times = MapUtil.get(param, "times", ArrayList.class);
        if(ObjectUtil.isNotEmpty(times)){
            Object a = times.get(0);
            Object b = times.get(1);
            pickUpTimeStart = a.toString();
            pickUpTimeEnd = b.toString();
        }
        String orderNo = MapUtil.getStr(param, "orderNo");//?????????
        List<Map<String, Object>> orderTree = supplierInfoService.getOrderTree(orderTypeCode, pickUpTimeStart, pickUpTimeEnd, orderNo);
        return CommonResult.success(orderTree);
    }


}

