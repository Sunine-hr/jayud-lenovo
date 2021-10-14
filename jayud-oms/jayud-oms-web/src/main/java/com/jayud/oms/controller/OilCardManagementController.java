package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.model.bo.AddOilCardManagementForm;
import com.jayud.oms.model.bo.QueryOilCardManagementForm;
import com.jayud.oms.model.enums.OilCardRechargeTypeEnum;
import com.jayud.oms.model.enums.OilCardStatusEnum;
import com.jayud.oms.model.enums.OilCardTypeEnum;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.model.po.OilCardTrackingInfo;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.OilCardManagementVO;
import com.jayud.oms.model.vo.OilCardTrackingInfoVO;
import com.jayud.oms.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 油卡管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
@RestController
@Api(tags = "油卡管理")
@RequestMapping("/oilCardManagement")
public class OilCardManagementController {

    @Autowired
    private IOilCardManagementService oilCardManagementService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IOilCardTrackingInfoService oilCardTrackingInfoService;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Autowired
    private IVehicleInfoService vehicleInfoService;

    @ApiOperation(value = "新增/编辑油卡信息")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddOilCardManagementForm form) {
//        if (form.getId() != null) {
//            OilCardManagement tmp = this.oilCardManagementService.getById(form.getId());
//            if (!StatusEnum.ENABLE.getCode().equals(tmp.getStatus())) {
//                return CommonResult.error(400, "禁用数据无法操作");
//            }
//        }

        if (form.getDriverId() != null) {
            form.setOilStatus(1);
        } else {
            form.setOilStatus(2);
        }

        this.oilCardManagementService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "下拉油卡类型")
    @PostMapping("/initOilCardType")
    public CommonResult<List<InitComboxStrVO>> initOilCardType() {
//        List<Dict> oilCardTypes = dictService.getByDictTypeCode("oilCardType");
//        List<InitComboxStrVO> list=new ArrayList<>();
//        for (Dict oilCardType : oilCardTypes) {
//            InitComboxStrVO initComboxStrVO=new InitComboxStrVO();
//            initComboxStrVO.setCode(oilCardType.getCode());
//            initComboxStrVO.setName(oilCardType.getValue());
//            list.add(initComboxStrVO);
//        }
        return CommonResult.success(OilCardTypeEnum.initType());
    }

    @ApiOperation(value = "下拉油卡状态")
    @PostMapping("/initOilCardStatus")
    public CommonResult<List<InitComboxStrVO>> initOilCardStatus() {
        return CommonResult.success(OilCardStatusEnum.initStatus());
    }

    @ApiOperation(value = "下拉油卡充值类型")
    @PostMapping("/initOilCardRechargeType")
    public CommonResult<List<InitComboxStrVO>> initOilCardRechargeType() {
        return CommonResult.success(OilCardRechargeTypeEnum.initType());
    }

    @ApiOperation(value = "下拉油卡信息选项")
    @PostMapping("/initOilCardInfo")
    public CommonResult<Map<String, List<InitComboxStrVO>>> initOilCardInfo() {
        Map<String, List<InitComboxStrVO>> map = new HashMap<>();
        map.put("oilCardType", OilCardTypeEnum.initType());
        map.put("oilCardStatus", OilCardStatusEnum.initStatus());
        map.put("rechargeType", OilCardRechargeTypeEnum.initType());
        return CommonResult.success(map);
    }

    @ApiOperation(value = "分页查询油卡信息")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<OilCardManagementVO>> findByPage(@RequestBody QueryOilCardManagementForm form) {
        form.setStatus(Arrays.asList(StatusEnum.ENABLE.getCode(), StatusEnum.DISABLE.getCode()));
        IPage<OilCardManagementVO> iPage = this.oilCardManagementService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "充值操作")
    @PostMapping("/rechargeOpt")
    public CommonResult rechargeOpt(@RequestBody OilCardManagementVO oilCardManagement) {
        BigDecimal rechargeAmount = oilCardManagement.getRechargeAmount();
        LocalDateTime rechargeDate = oilCardManagement.getRechargeDate();
        if (rechargeAmount == null || rechargeDate == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OilCardManagement oldTmp = this.oilCardManagementService.getById(oilCardManagement.getId());
        if (!StatusEnum.ENABLE.getCode().equals(oldTmp.getStatus())) {
            return CommonResult.error(400, "禁用数据无法操作");
        }

        this.oilCardManagementService.updateById(new OilCardManagement()
                .setId(oilCardManagement.getId()).setRechargeDate(oilCardManagement.getRechargeDate())
                .setRechargeAmount(oilCardManagement.getRechargeAmount())
                .setBalance(BigDecimalUtil.add(oldTmp.getBalance(), oilCardManagement.getRechargeAmount())));
        //记录跟踪信息
        StringBuilder sb = new StringBuilder();
        sb.append("充值日期:").append(DateUtils.LocalDateTime2Str(oilCardManagement.getRechargeDate(), DateUtils.DATE_PATTERN))
                .append(";")
                .append("充值金额:").append(oilCardManagement.getRechargeAmount()).append(";")
                .append("当前余额:").append(BigDecimalUtil.add(oldTmp.getBalance(), oilCardManagement.getRechargeAmount()));

        this.oilCardTrackingInfoService.save(new OilCardTrackingInfo()
                .setOilCardId(oldTmp.getId()).setContent(sb.toString()).setType("系统")
                .setCreateUser(UserOperator.getToken()).setCreateTime(LocalDateTime.now()));
        return CommonResult.success();
    }

    @ApiOperation(value = "消费操作")
    @PostMapping("/consumptionOpt")
    public CommonResult consumptionOpt(@RequestBody OilCardManagementVO oilCardManagement) {
        BigDecimal consumptionAmount = oilCardManagement.getConsumptionAmount();
        LocalDateTime consumptionDate = oilCardManagement.getConsumptionDate();
        if (consumptionAmount == null || consumptionDate == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OilCardManagement oldTmp = this.oilCardManagementService.getById(oilCardManagement.getId());
        BigDecimal amount = BigDecimalUtil.subtract(oldTmp.getBalance(), consumptionAmount);
        if (consumptionAmount.compareTo(new BigDecimal(0)) < 0) {
            return CommonResult.error(400, "当前余额不够");
        }

        if (!StatusEnum.ENABLE.getCode().equals(oldTmp.getStatus())) {
            return CommonResult.error(400, "禁用数据无法操作");
        }
        this.oilCardManagementService.updateById(new OilCardManagement()
                .setId(oilCardManagement.getId()).setConsumptionDate(consumptionDate).setConsumptionAmount(consumptionAmount)
                .setBalance(amount).setConsumptionAmount(BigDecimalUtil.add(oldTmp.getConsumptionAmount(), oilCardManagement.getConsumptionAmount())));
        //记录跟踪信息
        StringBuilder sb = new StringBuilder();
        sb.append("消费日期:").append(DateUtils.LocalDateTime2Str(oilCardManagement.getConsumptionDate(), DateUtils.DATE_PATTERN))
                .append(";")
                .append("消费金额:").append(oilCardManagement.getConsumptionAmount()).append(";")
                .append("当前余额:").append(amount);

        this.oilCardTrackingInfoService.save(new OilCardTrackingInfo()
                .setOilCardId(oldTmp.getId()).setContent(sb.toString()).setType("系统")
                .setCreateUser(UserOperator.getToken()).setCreateTime(LocalDateTime.now()));
        return CommonResult.success();
    }

    @ApiOperation(value = "领用操作")
    @PostMapping("/collectingOpt")
    public CommonResult collectingOpt(@RequestBody OilCardManagementVO oilCardManagement) {
        Long driverId = oilCardManagement.getDriverId();
        Long vehicleId = oilCardManagement.getVehicleId();
        LocalDateTime consumingDate = oilCardManagement.getConsumingDate();
        if (driverId == null || vehicleId == null || consumingDate == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OilCardManagement oldTmp = this.oilCardManagementService.getById(oilCardManagement.getId());
        if (!StatusEnum.ENABLE.getCode().equals(oldTmp.getStatus())) {
            return CommonResult.error(400, "禁用数据无法操作");
        }

        this.oilCardManagementService.updateById(new OilCardManagement()
                .setId(oilCardManagement.getId()).setConsumingDate(consumingDate)
                .setDriverId(driverId).setVehicleId(vehicleId).setOilStatus(1));

        //记录跟踪信息
        StringBuilder sb = new StringBuilder();
        sb.append("领用日期:").append(DateUtils.LocalDateTime2Str(consumingDate, DateUtils.DATE_PATTERN)).append(";")
                .append("领用人:").append(this.driverInfoService.getById(driverId).getName()).append(";")
                .append("领用人车辆:").append(this.vehicleInfoService.getById(vehicleId).getPlateNumber());

        this.oilCardTrackingInfoService.save(new OilCardTrackingInfo()
                .setOilCardId(oldTmp.getId()).setContent(sb.toString()).setType("系统")
                .setCreateUser(UserOperator.getToken()).setCreateTime(LocalDateTime.now()));
        return CommonResult.success();
    }

    @ApiOperation(value = "归还日期")
    @PostMapping("/returnOpt")
    public CommonResult returnOpt(@RequestBody OilCardManagementVO oilCardManagement) {
        LocalDateTime returnDate = oilCardManagement.getReturnDate();
        if (returnDate == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OilCardManagement oldTmp = this.oilCardManagementService.getById(oilCardManagement.getId());
        if (!StatusEnum.ENABLE.getCode().equals(oldTmp.getStatus())) {
            return CommonResult.error(400, "禁用数据无法操作");
        }
        this.oilCardManagementService.updateById(new OilCardManagement()
                .setId(oilCardManagement.getId()).setReturnDate(returnDate).setOilStatus(2));
        //记录跟踪信息
        StringBuilder sb = new StringBuilder();
        sb.append("归还日期:").append(DateUtils.LocalDateTime2Str(returnDate, DateUtils.DATE_PATTERN));

        this.oilCardTrackingInfoService.save(new OilCardTrackingInfo()
                .setOilCardId(oldTmp.getId()).setContent(sb.toString()).setType("系统")
                .setCreateUser(UserOperator.getToken()).setCreateTime(LocalDateTime.now()));
        return CommonResult.success();
    }


    @ApiOperation(value = "修改密码")
    @PostMapping("/updatePwd")
    public CommonResult updatePwd(@RequestBody OilCardManagementVO oilCardManagement) {
        String newOilPwd = oilCardManagement.getNewOilPwd();
        if (newOilPwd == null || oilCardManagement.getId() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OilCardManagement oldTmp = this.oilCardManagementService.getById(oilCardManagement.getId());
        if (!StatusEnum.ENABLE.getCode().equals(oldTmp.getStatus())) {
            return CommonResult.error(400, "禁用数据无法操作");
        }
        this.oilCardManagementService.updateById(new OilCardManagement()
                .setId(oilCardManagement.getId()).setOilPwd(newOilPwd));
        //记录跟踪信息
//        StringBuilder sb = new StringBuilder();
//        sb.append("修改密码:").append(oilPwd);
//
//        this.oilCardTrackingInfoService.save(new OilCardTrackingInfo()
//                .setOilCardId(oldTmp.getId()).setContent(sb.toString())
//                .setCreateUser(UserOperator.getToken()).setCreateTime(LocalDateTime.now()));
        return CommonResult.success();
    }


    @ApiOperation(value = "查询跟踪信息")
    @PostMapping("/getTrackingInfo")
    public CommonResult<List<OilCardTrackingInfoVO>> getTrackingInfo(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<OilCardTrackingInfo> list = this.oilCardTrackingInfoService.getByCondition(new OilCardTrackingInfo().setOilCardId(id));
        List<OilCardTrackingInfoVO> tmps = ConvertUtil.convertList(list, OilCardTrackingInfoVO.class);
        tmps.stream().sorted(Comparator.comparing(OilCardTrackingInfoVO::getId).reversed());
        return CommonResult.success(tmps);
    }

    @ApiOperation(value = "查询油卡详情")
    @PostMapping("/getById")
    public CommonResult getById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OilCardManagement oilCardManagement = this.oilCardManagementService.getById(id);
        return CommonResult.success(oilCardManagement);
    }

    @ApiOperation(value = "删除油卡管理信息")
    @PostMapping("/delete")
    public CommonResult delete(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.oilCardManagementService.updateById(new OilCardManagement().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }

    @ApiOperation(value = "启用/禁用")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Long id = Long.parseLong(map.get("id"));
        if (this.oilCardManagementService.enableOrDisable(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }
}

