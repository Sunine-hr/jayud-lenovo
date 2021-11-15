package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddBankAccountFrom;
import com.jayud.oms.model.bo.AddCustomerAddrForm;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.bo.QueryCustomerAddressForm;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.ContractQuotation;
import com.jayud.oms.model.po.CustomerAddress;
import com.jayud.oms.model.po.RegionCity;
import com.jayud.oms.model.vo.BankAccountVO;
import com.jayud.oms.model.vo.CustomerAddrVO;
import com.jayud.oms.service.IBankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 银行存款
 * </p>
 *
 * @author wh
 * @since 2021-11-10
 */
@RestController
@Api(tags = "银行账户信息")
@RequestMapping("/bankAccount")
public class BankAccountController {

    @Autowired
    private IBankAccountService bankAccountService;

    @ApiOperation(value = "新增或修改编辑银行信息")
    @PostMapping(value = "/saveOrUpdateBankAccount")
    public CommonResult saveOrUpdateBankAccount(@Valid @RequestBody AddBankAccountFrom form) {
        BankAccount bankAccount = ConvertUtil.convert(form, BankAccount.class);
        return CommonResult.success(this.bankAccountService.saveOrUpdateAddr(bankAccount));
    }

    @ApiOperation(value = "分页查询银行信息")
    @PostMapping("/findByPageBankAccount")
    public CommonResult<CommonPageResult<BankAccountVO>> findByPageBankAccount(@RequestBody BankAccountFrom form) {
        IPage<BankAccountVO> iPage = this.bankAccountService.findBankAccountByPage(form);
        return CommonResult.success(new CommonPageResult<>(iPage));
    }

    @ApiOperation(value = "查询银行信息")
    @PostMapping(value = "/list")
    public CommonResult<List<BankAccountVO>> list(@RequestBody BankAccountFrom form) {
        form.setPageSize(100000000);
        IPage<BankAccountVO> iPage = bankAccountService.findBankAccountByPage(form);
        return CommonResult.success(iPage.getRecords());
    }

    @ApiOperation("根据id删除银行存款信息")
    @PostMapping("/deleteBankAccountId")
    public CommonResult<BankAccountVO> deleteBankAccountId(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.bankAccountService.updateById(new BankAccount().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }


    @ApiOperation(value = "启用/禁用客户地址,id是银行主键")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        BankAccount tmp = this.bankAccountService.getById(id);
        Integer status = StatusEnum.ENABLE.getCode().equals(tmp.getStatus()) ? StatusEnum.DISABLE.getCode() : StatusEnum.ENABLE.getCode();
        BankAccount bankAccount = new BankAccount().setId(id).setStatus(Integer.valueOf(status));
        return CommonResult.success(this.bankAccountService.saveOrUpdateAddr(bankAccount));
    }

    @ApiOperation(value = "根据主键获取银行信息详情,id是银行主键")
    @PostMapping(value = "/getBankAccountIdById")
    public CommonResult getBankAccountIdById(@RequestBody Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("id"))) {
            return CommonResult.error(500, "id is required");
        }
        Long id = Long.parseLong(map.get("id"));
        BankAccount bankAccount = this.bankAccountService.getById(id);
        return CommonResult.success(ConvertUtil.convert(bankAccount, BankAccountVO.class));
    }

    @ApiOperation(value = "自动生成编号")
    @PostMapping(value = "/bankAccountNum")
    public CommonResult bankAccountNum() {
        return CommonResult.success(this.bankAccountService.bankAccountNum());
    }


}
