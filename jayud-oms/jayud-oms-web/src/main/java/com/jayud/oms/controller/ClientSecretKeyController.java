package com.jayud.oms.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddBankAccountFrom;
import com.jayud.oms.model.bo.AddClientSecretKeyForm;
import com.jayud.oms.model.bo.BankAccountFrom;
import com.jayud.oms.model.po.BankAccount;
import com.jayud.oms.model.po.ClientSecretKey;
import com.jayud.oms.model.vo.BankAccountVO;
import com.jayud.oms.model.vo.ClientSecretKeyVO;
import com.jayud.oms.service.IBankAccountService;
import com.jayud.oms.service.IClientSecretKeyService;
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
 * 客户对外接口秘钥表
 * </p>
 *
 * @author wh
 * @since 2021-11-10
 */
@RestController
@Api(tags = "客户对外接口秘钥表")
@RequestMapping("/clientSecretKey")
public class ClientSecretKeyController {

    @Autowired
    private IClientSecretKeyService clientSecretKeyService;

    @ApiOperation(value = "新增或修改编辑秘钥表")
    @PostMapping(value = "/saveOrUpdateClientSecretKeyOne")
    public CommonResult saveOrUpdateClientSecretKeyOne(@Valid @RequestBody AddClientSecretKeyForm form) {
        ClientSecretKey convert = ConvertUtil.convert(form, ClientSecretKey.class);
        return CommonResult.success(this.clientSecretKeyService.saveOrUpdateAddr(convert));
    }


    @ApiOperation(value = "根据客户id查询私钥信息解密")
    @PostMapping("/findClientSecretKeyOne")
    public CommonResult findClientSecretKeyOne(@RequestBody String appId) {
        ClientSecretKeyVO clientSecretKeyOne = this.clientSecretKeyService.findClientSecretKeyOne(appId);
        return CommonResult.success(clientSecretKeyOne);
    }



    @ApiOperation("根据id删除客户秘钥信息")
    @PostMapping("/deleteClientSecretKeyId")
    public CommonResult<ClientSecretKey> deleteClientSecretKeyId(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        this.clientSecretKeyService.updateById(new ClientSecretKey().setId(id).setStatus(StatusEnum.DELETE.getCode()));
        return CommonResult.success();
    }


    @ApiOperation(value = "启用/禁用客户秘钥信息,id是银行主键")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(500, "id is required");
        }
        ClientSecretKey tmp = this.clientSecretKeyService.getById(id);
        Integer status = StatusEnum.ENABLE.getCode().equals(tmp.getStatus()) ? StatusEnum.DISABLE.getCode() : StatusEnum.ENABLE.getCode();
        ClientSecretKey clientSecretKey = new ClientSecretKey().setId(id).setStatus(Integer.valueOf(status));
        return CommonResult.success(this.clientSecretKeyService.saveOrUpdateAddr(clientSecretKey));
    }

//    @ApiOperation(value = "根据主键获取银行信息详情,id是银行主键")
//    @PostMapping(value = "/getBankAccountIdById")
//    public CommonResult getBankAccountIdById(@RequestBody Map<String, String> map) {
//        if (StringUtils.isEmpty(map.get("id"))) {
//            return CommonResult.error(500, "id is required");
//        }
//        Long id = Long.parseLong(map.get("id"));
//        BankAccount bankAccount = this.bankAccountService.getById(id);
//        return CommonResult.success(ConvertUtil.convert(bankAccount, BankAccountVO.class));
//    }

//    @ApiOperation(value = "自动生成编号")
//    @PostMapping(value = "/ClientSecretKeyNum")
//    public CommonResult ClientSecretKeyNum() {
//        return CommonResult.success(this.clientSecretKeyService.bankAccountNum());
//    }


}
