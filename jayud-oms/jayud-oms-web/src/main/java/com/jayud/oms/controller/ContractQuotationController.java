package com.jayud.oms.controller;


import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.enums.TrackingInfoBisTypeEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddContractQuotationForm;
import com.jayud.oms.model.bo.QueryContractQuotationForm;
import com.jayud.oms.model.enums.ContractQuotationProStatusEnum;
import com.jayud.oms.model.enums.ContractQuotationSignEnum;
import com.jayud.oms.model.po.ContractQuotation;
import com.jayud.oms.model.po.TrackingInfo;
import com.jayud.oms.model.vo.ContractQuotationDetailsVO;
import com.jayud.oms.model.vo.ContractQuotationVO;
import com.jayud.oms.model.vo.TrackingInfoVO;
import com.jayud.oms.service.IContractQuotationService;
import com.jayud.oms.service.IOrderInfoService;
import com.jayud.oms.service.ITrackingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同报价 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@RestController
@Api(tags = "合同报价")
@RequestMapping("/contractQuotation")
public class ContractQuotationController {

    @Autowired
    private IContractQuotationService contractQuotationService;
    @Autowired
    private ITrackingInfoService trackingInfoService;
    @Autowired
    private IOrderInfoService orderInfoService;

    @ApiOperation("分页查询合同报价")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<ContractQuotationVO>> findByPage(@RequestBody QueryContractQuotationForm form) {
        IPage<ContractQuotationVO> iPage = this.contractQuotationService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }


    @ApiOperation("新增/编辑合同报价")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody AddContractQuotationForm form) {
        form.checkParam();
        form.assemblyContractNo();
//        if (this.contractQuotationService.exitName(form.getId(), form.getName())) {
//            return CommonResult.error(400, "该报价名称已存在");
//        }

        if (form.getId() != null) {
            ContractQuotation contractQuotation = this.contractQuotationService.getById(form.getId());
            if (!ContractQuotationProStatusEnum.ONE.getCode().equals(contractQuotation.getOptStatus())
                    && !ContractQuotationProStatusEnum.FIVE.getCode().equals(contractQuotation.getOptStatus())) {
                return CommonResult.error(400, "当前状态无法进行操作");
            }
        }
        form.checkCostDuplicate();
        this.contractQuotationService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation("自动生成编号")
    @PostMapping("/autoGenerateNum")
    public CommonResult<String> autoGenerateNum() {
        String num = this.contractQuotationService.autoGenerateNum();
        return CommonResult.success(num);
    }

    @ApiOperation("获取编辑详情")
    @PostMapping("/getEditInfoById")
    public CommonResult<ContractQuotationVO> getEditInfoById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        ContractQuotationVO tmp = this.contractQuotationService.getEditInfoById(id);
        return CommonResult.success(tmp);
    }

    @ApiOperation("删除合同报价信息")
    @PostMapping("/deleteById")
    public CommonResult<ContractQuotationVO> deleteById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.contractQuotationService.updateById(new ContractQuotation().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }


//    @ApiOperation("审核操作")
//    @PostMapping("/auditOpt")
//    public CommonResult<ContractQuotationVO> auditOpt(@RequestBody Map<String, Object> map) {
//        Long id = MapUtil.getLong(map, "id");
//        if (id == null) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
//        this.contractQuotationService.updateById(new ContractQuotation().setId(id)
//                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken()));
//        return CommonResult.success();
//    }

//    @ApiOperation("反审核操作")
//    @PostMapping("/antiAuditOpt")
//    public CommonResult<ContractQuotationVO> antiAuditOpt(@RequestBody Map<String, Object> map) {
//        Long id = MapUtil.getLong(map, "id");
//        if (id == null) {
//            return CommonResult.error(ResultEnum.PARAM_ERROR);
//        }
//        this.contractQuotationService.updateById(new ContractQuotation().setId(id)
//                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken()));
//        return CommonResult.success();
//    }


    /**
     * 获取跟踪信息
     *
     * @param map
     * @return
     */
    @ApiOperation("获取跟踪信息")
    @PostMapping("/getTrackingInfo")
    public CommonResult<List<TrackingInfoVO>> getTrackingInfo(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        String type = MapUtil.getStr(map, "type");
        if (id == null || StringUtils.isEmpty(type)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<TrackingInfo> list = this.trackingInfoService.getByCondition(new TrackingInfo().setType(type)
                .setBusinessId(id).setBusinessType(TrackingInfoBisTypeEnum.ONE.getCode()));
        list = list.stream().sorted(Comparator.comparing(TrackingInfo::getId).reversed()).collect(Collectors.toList());
        List<TrackingInfoVO> tmps = ConvertUtil.convertList(list, TrackingInfoVO.class);
        return CommonResult.success(tmps);
    }


    /**
     * 获取跟踪类型
     *
     * @return
     */
    @ApiOperation("获取跟踪类型")
    @PostMapping("/getTrackingType")
    public CommonResult<List<InitComboxStrVO>> getTrackingType() {
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (SubOrderSignEnum value : SubOrderSignEnum.values()) {
            switch (value) {
                case ZGYS:
                    InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
                    initComboxStrVO.setCode(value.getDesc());
                    initComboxStrVO.setName(value.getDesc());
                    initComboxStrVOS.add(initComboxStrVO);
            }
        }
        return CommonResult.success(initComboxStrVOS);
    }


    /**
     * 获取合同报价查询条件
     *
     * @return
     */
    @ApiOperation("获取合同报价查询条件")
    @PostMapping("/initQueryCondition")
    public CommonResult<Map<String, Object>> initQueryCondition() {
        Map<String, Object> map = new HashMap<>();
        map.put("optStatus", ContractQuotationProStatusEnum.initCombox());
        map.put("sign", ContractQuotationSignEnum.initCombox());
        return CommonResult.success(map);
    }

    /**
     * 审核操作
     *
     * @return
     */
    @ApiOperation("审核操作")
    @PostMapping("/auditOpt")
    public CommonResult auditOpt(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        ContractQuotation contractQuotation = this.contractQuotationService.getById(id);
        Integer optStatus = contractQuotation.getOptStatus();
        String reasonsFailure = MapUtil.getStr(map, "reasonsFailure");
        ContractQuotationProStatusEnum statusEnum = ContractQuotationProStatusEnum.getEnum(optStatus);
        if (statusEnum == null || statusEnum.getNextOpt() == null) {
            return CommonResult.error(400, "不存在操作流程");
        }
        this.contractQuotationService.auditOpt(id, reasonsFailure, statusEnum);
        return CommonResult.success(map);
    }

    /**
     * 审核不通过
     *
     * @return
     */
    @ApiOperation("审核不通过")
    @PostMapping("/auditFailed")
    public CommonResult auditFailed(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        String reasonsFailure = MapUtil.getStr(map, "reasonsFailure");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(reasonsFailure)) {
            return CommonResult.error(400, "请输入不通过理由");
        }
        ContractQuotation contractQuotation = this.contractQuotationService.getById(id);
        if (!ContractQuotationProStatusEnum.TWO.getCode().equals(contractQuotation.getOptStatus())
                && !ContractQuotationProStatusEnum.THREE.getCode().equals(contractQuotation.getOptStatus())
                && !ContractQuotationProStatusEnum.FOUR.getCode().equals(contractQuotation.getOptStatus())) {
            return CommonResult.error(400, "该状态不能进行操作");
        }
        this.contractQuotationService.auditOpt(id, reasonsFailure, ContractQuotationProStatusEnum.FIVE);
        return CommonResult.success(map);
    }


    /**
     * 完善资料
     *
     * @return
     */
    @ApiOperation("完善资料")
    @PostMapping("/completeMaterial")
    public CommonResult completeMaterial(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        List<FileView> signContractFiles = MapUtil.get(map, "signContractFiles", new TypeReference<List<FileView>>() {
        });
        List<FileView> signOfferFiles = MapUtil.get(map, "signOfferFiles", new TypeReference<List<FileView>>() {
        });
        if (signContractFiles == null) {
            return CommonResult.error(400, "请上传签署合同附件");
        }
        if (signOfferFiles == null) {
            return CommonResult.error(400, "请上传签署报价附件");
        }
        ContractQuotation contractQuotation = this.contractQuotationService.getById(id);
        ContractQuotationProStatusEnum statusEnum = ContractQuotationProStatusEnum.getEnum(contractQuotation.getOptStatus());
        if (statusEnum == null || statusEnum.getNextOpt() == null) {
            return CommonResult.error(400, "该状态不能进行操作");
        }

        this.contractQuotationService.updateById(new ContractQuotation().setId(id)
                .setOptStatus(statusEnum.getNextOpt()).setSignContractFile(StringUtils.getFileStr(signContractFiles))
                .setSignContractFileName(StringUtils.getFileNameStr(signContractFiles))
                .setSignOfferFile(StringUtils.getFileStr(signOfferFiles))
                .setSignOfferFileName(StringUtils.getFileNameStr(signOfferFiles))
                .setUpdateUser(UserOperator.getToken()).setUpdateTime(LocalDateTime.now()));
        return CommonResult.success();
    }


    /**
     * 获取客户合同报价
     *
     * @return
     */
    @ApiOperation("获取客户合同报价")
    @PostMapping("/getCustomerContractQuotation")
    public CommonResult<List<InitComboxStrVO>> getCustomerContractQuotation(@RequestBody Map<String, Object> map) {
        String customerCode = MapUtil.getStr(map, "unitCode");
        Long legalEntityId = MapUtil.getLong(map, "legalEntityId");
        List<ContractQuotation> list = this.contractQuotationService.getByCondition(new ContractQuotation().setCustomerCode(customerCode)
                .setLegalEntityId(legalEntityId)
                .setType(1));
        List<InitComboxStrVO> tmps = new ArrayList<>();
        for (ContractQuotation contractQuotation : list) {
            if (LocalDate.now().compareTo(contractQuotation.getEndTime()) > 0) {
                continue;
            }
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(contractQuotation.getNumber());
            initComboxStrVO.setId(contractQuotation.getId());
            tmps.add(initComboxStrVO);
        }
        return CommonResult.success(tmps);
    }


    /**
     * 导入费用
     *
     * @return
     */
    @ApiOperation("导入费用")
    @PostMapping("/importCost")
    public CommonResult<List<ContractQuotationDetailsVO>> importCost(@RequestBody Map<String, Object> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        Long contractQuotationId = MapUtil.getLong(map, "contractQuotationId");
        List<ContractQuotationDetailsVO> list=this.contractQuotationService.importCost(mainOrderId,contractQuotationId);

        return CommonResult.success(list);
    }

}

