package com.jayud.oms.model.bo;

import com.jayud.oms.model.po.CustomerLineRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 客户线路管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Data
public class AddCustomerLineForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "客户线路名称")
    private String customerLineName;

    @ApiModelProperty(value = "客户线路编号", required = true)
    @NotEmpty(message = "客户线路编号不能为空")
    @Length(max = 14, message = "客户线路编号最大长度14位")
    @Length(min = 14, message = "客户线路编号长度为14位")
    private String customerLineCode;

    @ApiModelProperty(value = "司机ID", required = true)
    @NotNull(message = "司机ID不能为空")
    private Long driverInfoId;

    @ApiModelProperty(value = "线路ID", required = true)
    @NotNull(message = "线路ID不能为空")
    private Long lineId;

    @ApiModelProperty(value = "线路规则(周一/周日等，多个用,拼接，-数据字典配置)")
    private String lineRule;

    @Valid
    @ApiModelProperty(value = "线路规则(周一/周日等，多个用,拼接，-数据字典配置)")
    @NotNull(message = "线路规则不能为空")
    @Size(min = 1, message = "线路规则不能为空")
    private List<String> lineRules;

    @ApiModelProperty(value = "车牌号")
    private String vehicleNo;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "客户线路客户列表")
    private List<CustomerLineRelation> customerLineRelations;

}
