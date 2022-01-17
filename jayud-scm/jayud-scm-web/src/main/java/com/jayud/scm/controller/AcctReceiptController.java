package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.AccountBankBillVO;
import com.jayud.scm.model.vo.AcctReceiptVO;
import com.jayud.scm.service.*;
import com.jayud.scm.utils.ExcelTemplateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收款单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@RestController
@RequestMapping("/acctReceipt")
@Api(tags = "收款单管理")
public class AcctReceiptController {

    @Autowired
    private IAcctReceiptService acctReceiptService;

    @Autowired
    private IAcctPayService acctPayService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @ApiOperation(value = "根据id查询收款单信息")
    @PostMapping(value = "/getAcctReceiptById")
    public CommonResult<AcctReceiptVO> getAcctReceiptById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        AcctReceiptVO accountBankBillVO = acctReceiptService.getAcctReceiptById(id);
        return CommonResult.success(accountBankBillVO);
    }

    @ApiOperation(value = "新增或修改收款单信息")
    @PostMapping(value = "/saveOrUpdateAcctReceipt")
    public CommonResult saveOrUpdateAcctReceipt(@RequestBody AddAcctReceiptForm form) {
        boolean result = acctReceiptService.saveOrUpdateAcctReceipt(form);
        if(!result){
            return CommonResult.error(444,"添加或修改收款单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "锁定金额")
    @PostMapping(value = "/lockAmount")
    public CommonResult lockAmount(@RequestBody QueryCommonForm form) {

//        AcctReceipt acctReceipt = acctReceiptService.getById(form.getId());
//        if(acctReceipt != null){
//
//        }

        boolean result = acctReceiptService.lockAmount(form);
        if(!result){
            return CommonResult.error(444,"锁定金额失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改汇率")
    @PostMapping(value = "/modifyExchangeRate")
    public CommonResult modifyExchangeRate(@RequestBody QueryCommonForm form) {
        boolean result = acctReceiptService.modifyExchangeRate(form);
        if(!result){
            return CommonResult.error(444,"修改汇率失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "释放额度")
    @PostMapping(value = "/releaseLimit")
    public CommonResult releaseLimit(@RequestBody QueryCommonForm form) {
        boolean result = acctReceiptService.releaseLimit(form);
        if(!result){
            return CommonResult.error(444,"释放额度失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "认领")
    @PostMapping(value = "/claim")
    public CommonResult claim(@RequestBody AddAcctReceiptClaimForm form) {
        boolean result = acctReceiptService.claim(form);
        if(!result){
            return CommonResult.error(444,"认领失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "生成付款单")
    @PostMapping(value = "/generatePaymentDocument")
    public CommonResult generatePaymentDocument(@RequestBody AddAcctPayReceiptForm form) {

        boolean result = acctPayService.generatePaymentDocument(form);
        if(!result){
            return CommonResult.error(444,"生成付款单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "判断是否能生成付款单")
    @PostMapping(value = "/isPay")
    public CommonResult isPay(@RequestBody QueryCommonForm form) {

        AcctReceipt acctReceipt = acctReceiptService.getById(form.getId());

        AcctPay acctPay = acctPayService.getAcctPayByPayToMeId(form.getId());
        if(acctPay != null){
            return CommonResult.error(444,"该收款单已经生成付款单，无法重复操作");
        }

        if(!acctReceipt.getModelType().equals(2)){
            return CommonResult.error(444,"只有出口的单，才能进行付款操作");
        }

        if(acctReceipt.getJoinBillId() != null){
            return CommonResult.error(444,"该收款单由‘水单’生成，无法进行付款");
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "取消认领")
    @PostMapping(value = "/cancelClaim")
    public CommonResult cancelClaim(@RequestBody QueryCommonForm form) {
        boolean result = acctReceiptService.cancelClaim(form);
        if(!result){
            return CommonResult.error(444,"取消认领失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "收款单管理，下载模板")
    @GetMapping(value = "/downloadTemplateByBookingOrderEntry")
    public void downloadTemplateByBookingOrderEntry(HttpServletResponse response){
        new ExcelTemplateUtil().downloadExcel(response, "acct_receipt.xls", "收款单模板.xls");
    }

    @ApiOperation(value = "收款单管理，导入")
    @PostMapping(value = "/importAcctReceipt")
    public CommonResult importAcctReceipt(@RequestParam("file") MultipartFile file){
        //用HttpServletRequest，获取请求头内容
        if (file.isEmpty()) {
            return CommonResult.error(-1, "文件为空！");
        }
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        acctReceiptService.importAcctReceipt(file);
        return CommonResult.success("导入成功!");
    }

    @ApiOperation(value = "反审")
    @PostMapping(value = "/reverseReviewAcctReceipt")
    public CommonResult reverseReviewAcctReceipt(@RequestBody PermissionForm form) {
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取按钮权限
//        SystemAction systemAction = systemActionService.getSystemActionByActionCode(form.getActionCode());

        if(!systemUser.getUserName().equals("admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
//            for (SystemRole systemRole : enabledRolesByUserId) {
//                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
//                if(systemRoleAction == null){
//                    return CommonResult.error(444,"该用户没有该按钮权限");
//                }
//            }
            List<Long> longs = new ArrayList<>();
            for (SystemRole systemRole : enabledRolesByUserId) {
                longs.add(systemRole.getId());
//                if(systemRoleAction == null){
//                    return CommonResult.error(444,"该用户没有该按钮权限");
//                }
            }
            List<SystemRoleAction> systemRoleActions = systemRoleActionService.getSystemRoleActionByRoleIdsAndActionCode(longs,form.getActionCode());
            if(CollectionUtil.isEmpty(systemRoleActions)){
                return CommonResult.error(444,"该用户没有该按钮权限");
            }
        }

        //拥有按钮权限，判断是否为审核按钮

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());
        AcctReceipt acctReceipt = acctReceiptService.getById(form.getId());
        if(acctReceipt.getIsClaim().equals(1)){
            return CommonResult.error(444,"该收款单已认领，无法反审，请取消认领后在审核");
        }

        return acctReceiptService.reverseReviewAcctReceipt(form);
    }
}

