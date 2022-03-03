package com.jayud.crm.controller;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.UnitEnum;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.OmsClient;
import com.jayud.crm.model.bo.AddCrmContractQuotationForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.vo.CrmContractQuotationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmContractQuotationService;
import com.jayud.crm.model.po.CrmContractQuotation;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 合同报价 控制类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Api(tags = "合同报价")
@RestController
@RequestMapping("/crmContractQuotation")
public class CrmContractQuotationController {


    @Autowired
    private ICrmContractQuotationService crmContractQuotationService;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private AuthClient authClient;

    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-01
     * @param: crmContractQuotation
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmContractQuotation>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmContractQuotationVO>> selectPage(CrmContractQuotation crmContractQuotation,
                                                                @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                HttpServletRequest req) {
        return BaseResult.ok(crmContractQuotationService.selectPage(crmContractQuotation, currentPage, pageSize, req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-01
     * @param: crmContractQuotation
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmContractQuotation>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmContractQuotation>> selectList(CrmContractQuotation crmContractQuotation,
                                                             HttpServletRequest req) {
        return BaseResult.ok(crmContractQuotationService.selectList(crmContractQuotation));
    }

    /**
     * @description 获取审核通过合同报价
     **/
    @ApiOperation("获取审核通过合同报价")
    @GetMapping("/getApprovedInfos")
    public BaseResult<List<CrmContractQuotation>> getApprovedInfos() {
        return BaseResult.ok(crmContractQuotationService.selectList(new CrmContractQuotation().setIsDeleted(false).setCheckStateFlag("Y")));
    }


    @ApiOperation("新增/编辑合同报价")
    @GetMapping("/saveOrUpdate")
    public BaseResult saveOrUpdate(@RequestBody AddCrmContractQuotationForm form) {
        form.checkParam();
        if (form.getId() != null) {
            CrmContractQuotation contractQuotation = this.crmContractQuotationService.getById(form.getId());
            if (contractQuotation.getFLevel() != null && contractQuotation.getFStep() != 0
                    && !contractQuotation.getFStep().equals(contractQuotation.getFLevel())) {
                return BaseResult.error(SysTips.UNDER_REVIEW_NOT_OPT);
            }
        }
        form.checkCostDuplicate();
        return BaseResult.ok(this.crmContractQuotationService.saveOrUpdate(form));
    }


    @ApiOperation("自动生成编号")
    @PostMapping("/autoGenerateNum")
    public BaseResult autoGenerateNum() {
        return this.authClient.getOrderFeign(CrmDictCode.CONTRACT_AGREEMENT_NUM_CODE, new Date());
    }

    @ApiOperation("获取编辑详情")
    @PostMapping("/getEditInfoById")
    public BaseResult<CrmContractQuotationVO> getEditInfoById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return BaseResult.error(SysTips.ERROR_MSG);
        }
        CrmContractQuotationVO tmp = this.crmContractQuotationService.getEditInfoById(id);
        return BaseResult.ok(tmp);
    }


    /**
     * @description 物理删除
     * @author jayud
     * @date 2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id) {
//        crmContractQuotationService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        crmContractQuotationService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmContractQuotation>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmContractQuotation> queryById(@RequestParam(name = "id", required = true) int id) {
        CrmContractQuotation crmContractQuotation = crmContractQuotationService.getById(id);
        return BaseResult.ok(crmContractQuotation);
    }


    /**
     * @description 根据查询条件导出收货单
     * @author jayud
     * @date 2022-03-01
     * @param: response  响应对象
     * @param: queryReceiptForm  参数queryReceiptForm
     * @param: req
     * @return: void
     **/
    @ApiOperation("根据查询条件导出合同报价")
    @PostMapping(path = "/exportCrmContractQuotation")
    public void exportCrmContractQuotation(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "主键",
                    "报价编号(合同报价编号)",
                    "客户/供应商id",
                    "客户/供应商code",
                    "客户/供应商名称",
                    "报价名称",
                    "有效起始时间",
                    "有效结束时间",
                    "状态（0禁用 1启用）",
                    "合同对象(1:客户,2:供应商)",
                    "法人主体名称id",
                    "法人主体",
                    "销售员id",
                    "销售员名称",
                    "审核级别",
                    "当前级别",
                    "审核状态",
                    "租户编码",
                    "备注",
                    "是否删除，0未删除，1已删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmContractQuotationService.queryCrmContractQuotationForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "合同报价", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


    @ApiOperation(value = "录用费用页面-下拉选单位")
    @PostMapping(value = "/initCostUnit")
    public CommonResult<List<InitComboxStrVO>> initCostUnit() {
        return CommonResult.success(UnitEnum.initCostUnit());
    }

    @ApiOperation(value = "初始化车型尺寸,区分车型")
    @PostMapping(value = "/initVehicleSize")
    public CommonResult<List<Map<String, Object>>> initVehicleSize() {
        return omsClient.initVehicleSize();
    }

    @ApiOperation(value = "费用类别,idCode=费用名称的隐藏值")
    @PostMapping(value = "/initCostType")
    public CommonResult<List<InitComboxVO>> initCostType(@RequestBody Map<String, Object> param) {
        return omsClient.initCostType(param);
    }

    @ApiOperation(value = "录入费用:应收/付项目/币种 ")
    @PostMapping(value = "/initCost")
    public CommonResult initCost(@RequestBody Map<String, Object> param) {
        return omsClient.initCost(param);
    }
}
