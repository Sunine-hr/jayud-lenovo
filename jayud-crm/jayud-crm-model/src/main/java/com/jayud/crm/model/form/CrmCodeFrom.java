package com.jayud.crm.model.form;

import com.jayud.auth.model.po.SysDictItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciro
 * @date 2022/3/2 11:01
 * @description: 字典项
 */
@Data
@ApiModel(value="数据编码对象", description="数据编码对象")
public class CrmCodeFrom {

    @ApiModelProperty(value = "客户状态")
    private List<SysDictItem> custStatus;

    @ApiModelProperty(value = "客户星级")
    private List<SysDictItem> custStar;

    @ApiModelProperty(value = "客户审核状态")
    private List<SysDictItem> custAuditStatus;

    @ApiModelProperty(value = "客户来源")
    private List<SysDictItem> custSources;

    @ApiModelProperty(value = "客户所属行业")
    private List<SysDictItem> custIndustry;

    @ApiModelProperty(value = "客户管理-服务类型")
    private List<SysDictItem> custServerType;

    @ApiModelProperty(value = "客户管理-对账方式")
    private List<SysDictItem> custReconciliationMethod;

    @ApiModelProperty(value = "客户管理-结算方式")
    private List<SysDictItem> custSettlementMethod;

    @ApiModelProperty(value = "客户管理-客户状态")
    private List<SysDictItem> custNormalStatus;

    @ApiModelProperty(value = "客户管理-银行币别")
    private List<SysDictItem> custBankCurrency;

    @ApiModelProperty(value = "客户管理-业务类型")
    private List<SysDictItem> custBusinessType;


    public void intit(){
        this.custStatus = new ArrayList<>();
        this.custStar = new ArrayList<>();
        this.custAuditStatus = new ArrayList<>();
        this.custSources = new ArrayList<>();
        this.custIndustry = new ArrayList<>();
        this.custServerType = new ArrayList<>();
        this.custReconciliationMethod = new ArrayList<>();
        this.custSettlementMethod = new ArrayList<>();
        this.custNormalStatus = new ArrayList<>();
        this.custBankCurrency = new ArrayList<>();
        this.custBusinessType = new ArrayList<>();
    }

}
