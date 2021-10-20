package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.scm.model.bo.AddAccountBankBillForm;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.TableEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.AccountBankBillVO;
import com.jayud.scm.model.vo.CustomerVO;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 水单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@RestController
@RequestMapping("/accountBankBill")
@Api(tags = "水单管理")
public class AccountBankBillController {

    @Autowired
    private IAccountBankBillService accountBankBillService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleActionService systemRoleActionService;

    @Autowired
    private ISystemUserRoleRelationService systemUserRoleRelationService;

    @Autowired
    private IAcctReceiptService acctReceiptService;

    @ApiOperation(value = "根据id查询水单信息")
    @PostMapping(value = "/getAccountBankBillById")
    public CommonResult<AccountBankBillVO> getAccountBankBillById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        AccountBankBillVO accountBankBillVO = accountBankBillService.getAccountBankBillById(id);
        return CommonResult.success(accountBankBillVO);
    }

    @ApiOperation(value = "新增或修改水单信息")
    @PostMapping(value = "/saveOrUpdateAccountBankBill")
    public CommonResult saveOrUpdateAccountBankBill(@RequestBody AddAccountBankBillForm form) {


        if(form.getVerificationMoney()!= null && form.getBillMoney().compareTo(form.getVerificationMoney()) == -1){
            return CommonResult.error(444,"核销金额应小于等于水单金额");
        }

        boolean result = accountBankBillService.saveOrUpdateAccountBankBill(form);
        if(!result){
            return CommonResult.error(444,"添加或修改水单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "审核")
    @PostMapping(value = "/reviewWaterBill")
    public CommonResult reviewWaterBill(@RequestBody PermissionForm form) {
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取按钮权限
//        SystemAction systemAction = systemActionService.getSystemActionByActionCode(form.getActionCode());

        if(!systemUser.getUserName().equals("admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
            for (SystemRole systemRole : enabledRolesByUserId) {
                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
                if(systemRoleAction == null){
                    return CommonResult.error(444,"该用户没有该按钮权限");
                }
            }
        }

        //拥有按钮权限，判断是否为审核按钮

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());

        return accountBankBillService.reviewWaterBill(form);
    }

    @ApiOperation(value = "反审")
    @PostMapping(value = "/reverseReviewWaterBill")
    public CommonResult reverseReviewWaterBill(@RequestBody PermissionForm form) {
        //获取登录用户
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        //获取按钮权限
//        SystemAction systemAction = systemActionService.getSystemActionByActionCode(form.getActionCode());

        if(!systemUser.getUserName().equals("admin")){
            //获取登录用户所属角色
            List<SystemRole> enabledRolesByUserId = systemUserRoleRelationService.getEnabledRolesByUserId(systemUser.getId());
            for (SystemRole systemRole : enabledRolesByUserId) {
                SystemRoleAction systemRoleAction = systemRoleActionService.getSystemRoleActionByRoleIdAndActionCode(systemRole.getId(),form.getActionCode());
                if(systemRoleAction == null){
                    return CommonResult.error(444,"该用户没有该按钮权限");
                }
            }
        }

        //拥有按钮权限，判断是否为审核按钮

        form.setTable(TableEnum.getDesc(form.getKey()));
        form.setUserId(systemUser.getId().intValue());
        form.setUserName(systemUser.getUserName());
        AccountBankBill accountBankBill = accountBankBillService.getById(form.getId());
        if(accountBankBill.getBillArMoney() != null && accountBankBill.getBillArMoney().compareTo(new BigDecimal(0)) == 1){
            return CommonResult.error(1,"该水单已经到账，无法进行反审");
        }
        AcctReceipt acctReceipt = acctReceiptService.getAcctReceiptByJoinBillId(accountBankBill.getId());
        if(acctReceipt != null){
            return CommonResult.error(1,"该水单已经生成收款单，无法进行反审");
        }

        return accountBankBillService.reverseReviewWaterBill(form);
    }

    @ApiOperation(value = "到账")
    @PostMapping(value = "/arrival")
    public CommonResult arrival(@RequestBody QueryCommonForm form) {
        AcctReceipt acctReceipt = acctReceiptService.getAcctReceiptByJoinBillId(form.getId());
        AccountBankBill accountBankBill = this.accountBankBillService.getById(form.getId());
        if(!accountBankBill.getCheckStateFlag().equals("Y")){
            return CommonResult.error(444,"该水单未审核，无法进行到账操作");
        }
        if(acctReceipt != null){
            return CommonResult.error(1,"该水单已经生成收款单，无法进行到账操作");
        }
        if(form.getBillArMoney()!= null && accountBankBill.getBillMoney().compareTo(form.getBillArMoney()) == -1){
            return CommonResult.error(444,"到账金额应小于等于水单金额");
        }
        boolean result = accountBankBillService.arrival(form);
        if(!result){
            return CommonResult.error(444,"到账失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "生成收款单")
    @PostMapping(value = "/generateCollectionDoc")
    public CommonResult generateCollectionDoc(@RequestBody QueryCommonForm form) {
        AcctReceipt acctReceipt = acctReceiptService.getAcctReceiptByJoinBillId(form.getId());
        if(acctReceipt != null){
            return CommonResult.error(444,"该水单已经生成收款单，无法再次生成");
        }
        AccountBankBill accountBankBill = this.accountBankBillService.getById(form.getId());
        if(!accountBankBill.getCheckStateFlag().equals("Y")){
            return CommonResult.error(444,"该水单未审核，无法生成收款单");
        }
        if(accountBankBill.getBillArMoney() == null){
            return CommonResult.error(444,"该水单未进行到账操作，无法生成收款单");
        }
        boolean result = acctReceiptService.generateCollectionDoc(accountBankBill);
        if(!result){
            return CommonResult.error(444,"生成收款单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "出口水单核销自动生成付款单")
    @PostMapping(value = "/automaticallyGeneratePayment ")
    public CommonResult automaticallyGeneratePayment(@RequestBody QueryCommonForm form) {
        return accountBankBillService.automaticallyGeneratePayment(form);

    }

}

