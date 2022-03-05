package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ciro
 * @date 2022/3/5 13:43
 * @description: 客户信息校验
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="客户信息校验", description="客户信息校验")
public class CrmCustomerCheckForm {

    @ApiModelProperty(value = "主键ID")
    protected Long id;

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "统一信用代码")
    private String unCreditCode;

}
