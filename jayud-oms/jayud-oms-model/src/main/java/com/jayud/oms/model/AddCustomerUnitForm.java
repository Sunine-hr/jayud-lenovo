package com.jayud.oms.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.model.vo.CustomerUnitVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 客户结算单位
 * </p>
 *
 * @author LDR
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomerUnit对象", description = "客户结算单位")
public class AddCustomerUnitForm extends Model<AddCustomerUnitForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "客户id")
    @NotNull(message = "请绑定客户id")
    private Long customerId;

    @ApiModelProperty(value = "业务类型")
    @NotEmpty(message = "请选择业务类型")
    private String businessType;

    @ApiModelProperty(value = "操作部门code")
    @NotEmpty(message = "请选择操作部门")
    private String optDepartmentCode;

    @ApiModelProperty(value = "结算代码code")
    @NotEmpty(message = "请选择结算代码")
    private String unitCode;

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;
//
//    @ApiModelProperty(value = "创建人")
//    private String createUser;
//
//    @ApiModelProperty(value = "更新人")
//    private String updateUser;
//
//    @ApiModelProperty(value = "更新时间")
//    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(CustomerUnitVO.class));
    }
}
