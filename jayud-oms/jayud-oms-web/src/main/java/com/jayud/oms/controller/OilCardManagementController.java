package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.oms.model.bo.AddOilCardManagementForm;
import com.jayud.oms.model.bo.QueryOilCardManagementForm;
import com.jayud.oms.model.enums.OilCardRechargeTypeEnum;
import com.jayud.oms.model.enums.OilCardStatusEnum;
import com.jayud.oms.model.enums.OilCardTypeEnum;
import com.jayud.oms.model.po.Dict;
import com.jayud.oms.model.po.OilCardManagement;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.OilCardManagementVO;
import com.jayud.oms.service.IDictService;
import com.jayud.oms.service.IOilCardManagementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 油卡管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
@RestController
@RequestMapping("/oilCardManagement")
public class OilCardManagementController {

    @Autowired
    private IOilCardManagementService oilCardManagementService;
    @Autowired
    private IDictService dictService;

    @ApiOperation(value = "新增/编辑油卡信息")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddOilCardManagementForm form) {
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
        return CommonResult.success(OilCardStatusEnum.initType());
    }

    @ApiOperation(value = "下拉油卡充值类型")
    @PostMapping("/initOilCardRechargeType")
    public CommonResult<List<InitComboxStrVO>> initOilCardRechargeType() {
        return CommonResult.success(OilCardRechargeTypeEnum.initType());
    }

    @ApiOperation(value = "分页查询油卡信息")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryOilCardManagementForm form) {
        IPage<OilCardManagementVO> iPage = this.oilCardManagementService.findByPage(form);
        return CommonResult.success(iPage);
    }
}

