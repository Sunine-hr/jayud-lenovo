package com.jayud.crm.model.form;

import com.jayud.auth.model.po.SysDictItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

}
