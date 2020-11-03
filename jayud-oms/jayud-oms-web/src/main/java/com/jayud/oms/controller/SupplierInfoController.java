package com.jayud.oms.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.AddAuditSupplierInfoForm;
import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.bo.QueryAuditSupplierInfoForm;
import com.jayud.oms.model.bo.QuerySupplierInfoForm;
import com.jayud.oms.model.enums.AuditStatusEnum;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.enums.SettlementTypeEnum;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.vo.EnumVO;
import com.jayud.oms.model.vo.SupplierInfoVO;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.ISupplierInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
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
@Api(value = "供应商接口")
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
        List<Long> ids = pageList.getRecords().stream().filter(Objects::nonNull).map(SupplierInfoVO::getBuyerId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ids)) {
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
        List<Long> ids = pageList.getRecords().stream().filter(Objects::nonNull).map(SupplierInfoVO::getBuyerId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ids)) {
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


    @ApiOperation(value = "新增编辑供应商")
    @PostMapping(value = "/saveOrUpdateSupplierInfo")
    public CommonResult saveOrUpdateSupplierInfo(@Valid @RequestBody AddSupplierInfoForm form) {
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

    @ApiModelProperty("查询结算类型枚举")
    @PostMapping(value = "/getSettlementTypeEnum")
    public CommonResult<List<EnumVO>> getSettlementTypeEnum() {

        List<EnumVO> list = new ArrayList<>();
        for (SettlementTypeEnum value : SettlementTypeEnum.values()) {
            list.add(new EnumVO(value.getCode(), value.getDesc()));
        }
        return CommonResult.success(list);
    }

    @ApiModelProperty("供应商审核(财务审核/总经办审核)")
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
        if (AuditStatusEnum.CW_WAIT.getCode().equals(tmp.getAuditStatus())) {
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
}

