package com.jayud.crm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.FileClient;
import com.jayud.crm.model.bo.AddCrmCustomerAgreementForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.enums.FileModuleEnum;
import com.jayud.crm.model.po.CrmFile;
import com.jayud.crm.model.vo.CrmContractQuotationVO;
import com.jayud.crm.model.vo.CrmCustomerAgreementVO;
import com.jayud.crm.service.ICrmFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCustomerAgreementService;
import com.jayud.crm.model.po.CrmCustomerAgreement;

import java.time.LocalDate;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基本档案_协议管理(crm_customer_agreement) 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_协议管理(crm_customer_agreement)")
@RestController
@RequestMapping("/crmCustomerAgreement")
public class CrmCustomerAgreementController {


    @Autowired
    public ICrmCustomerAgreementService crmCustomerAgreementService;
    @Autowired
    public AuthClient authClient;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private ICrmFileService crmFileService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-02
     * @param: crmCustomerAgreement
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmCustomerAgreement>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CrmCustomerAgreementVO>> selectPage(CrmCustomerAgreement crmCustomerAgreement,
                                                                        @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                        HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(crmCustomerAgreementService.selectPage(crmCustomerAgreement, currentPage, pageSize, req)));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-02
     * @param: crmCustomerAgreement
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmCustomerAgreement>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCustomerAgreementVO>> selectList(CrmCustomerAgreement crmCustomerAgreement,
                                                             HttpServletRequest req) {
        return BaseResult.ok(crmCustomerAgreementService.selectList(crmCustomerAgreement));
    }


    /**
     * @description 新增/编辑
     **/
    @ApiOperation("新增/编辑")
    @PostMapping("/saveOrUpdate")
    public BaseResult saveOrUpdate(@RequestBody AddCrmCustomerAgreementForm form) {
        form.checkParam();
        if (form.getId() != null) {
            CrmCustomerAgreement agreement = this.crmCustomerAgreementService.getById(form.getId());
            if (agreement.getFLevel() != null && agreement.getFStep() != 0
                    && !agreement.getFStep().equals(agreement.getFLevel())) {
                return BaseResult.error(SysTips.UNDER_REVIEW_NOT_OPT);
            }
        }
        this.crmCustomerAgreementService.saveOrUpdate(form);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

    @ApiOperation("自动生成编号")
    @GetMapping("/autoGenerateNum")
    public BaseResult autoGenerateNum() {
        return this.authClient.getOrderFeign(CrmDictCode.QUOTATION_NUM_CODE, new Date());
    }


    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id) {
        crmCustomerAgreementService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerAgreement>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "int", required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCustomerAgreementVO> queryById(@RequestParam(name = "id", required = true) int id) {
        CrmCustomerAgreement crmCustomerAgreement = crmCustomerAgreementService.getById(id);
        CrmCustomerAgreementVO convert = ConvertUtil.convert(crmCustomerAgreement, CrmCustomerAgreementVO.class);
        List<LocalDate> dates = new ArrayList<>();
        dates.add(convert.getBeginDate());
        dates.add(convert.getEndDate());
        convert.setAgreementTime(dates);
        Object url = this.fileClient.getBaseUrl().getData();
        List<CrmFile> files = this.crmFileService.list(new QueryWrapper<>(new CrmFile().setIsDeleted(false).setBusinessId(convert.getId()).setCode(FileModuleEnum.CA.getCode())));
        files.forEach(e -> {
            e.setUploadFileUrl(url + e.getUploadFileUrl());
        });
        convert.setFiles(files);
        return BaseResult.ok(convert);
    }


    /**
     * @description 根据查询条件导出收货单
     * @author jayud
     * @date 2022-03-02
     * @param: response  响应对象
     * @param: queryReceiptForm  参数queryReceiptForm
     * @param: req
     * @return: void
     **/
    @ApiOperation("根据查询条件导出基本档案_协议管理(crm_customer_agreement)")
    @PostMapping(path = "/exportCrmCustomerAgreement")
    public void exportCrmCustomerAgreement(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                    "自动ID",
                    "协议编号",
                    "客户ID",
                    "客户名称",
                    "业务类型(报关，货代，物流等)",
                    "业务类型值",
                    "协议类型",
                    "协议名称",
                    "协议开始日期",
                    "协议结束日期",
                    "协议有效期",
                    "是否顺延",
                    "顺延天数(1:顺延6个月,2:顺延12个月,3:顺延24个月)",
                    "协议版本",
                    "协议说明",
                    "销售员id",
                    "销售员",
                    "法人主体id",
                    "法人主体名称",
                    "我司原件份数",
                    "是否默认协议",
                    "是否归档",
                    "归档编号",
                    "归档人",
                    "归档时间",
                    "审核级别",
                    "当前级别",
                    "审核状态",
                    "流程实例",
                    "报价单id",
                    "租户编码",
                    "备注",
                    "是否删除，0未删除，1已删除",
                    "创建人",
                    "创建时间",
                    "更新人",
                    "更新时间"
            );
            List<LinkedHashMap<String, Object>> dataList = crmCustomerAgreementService.queryCrmCustomerAgreementForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "基本档案_协议管理(crm_customer_agreement)", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
