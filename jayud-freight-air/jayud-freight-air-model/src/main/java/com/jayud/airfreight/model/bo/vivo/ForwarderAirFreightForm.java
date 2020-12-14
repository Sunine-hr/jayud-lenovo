package com.jayud.airfreight.model.bo.vivo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.CommonResult;
import com.jayud.common.vaildator.DateTime;
import com.jayud.common.vaildator.NumberEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 追踪信息
 *
 * @author william
 * @description
 * @Date: 2020-09-16 17:20
 */
@Data
public class ForwarderAirFreightForm {
    @JsonProperty("Booking_no")
    @SerializedName("Booking_no")
    @ApiModelProperty(value = "订单号")
    @NotEmpty(message = "订单号不能为空")
    @Length(max = 32, message = "订单号字段的最大长度为32")
    private String bookingNo;

    @JsonProperty("Bill_of_lading")
    @SerializedName("Bill_of_lading")
    @ApiModelProperty(value = "提单号")
    @NotEmpty(message = "提单号不能为空")
    @Length(max = 32, message = "提单号字段的最大长度为32")
    private String billOfLading;

    @JsonProperty("Operation_type")
    @SerializedName("Operation_type")
    @ApiModelProperty(value = "操作类型")
    @NotEmpty(message = "操作类型不能为空")
    @Length(max = 10, message = "操作类型最大长度为10")
    private String operationType;


    @JsonProperty("Line")
    @SerializedName("Line")
    @ApiModelProperty(value = "空运费用")
    @NotEmpty(message = "费用项不能为空")
    private List<ForwarderAirOrderCostItems> line;


    public CommonResult checkParam() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<ForwarderAirFreightForm>> set = validator.validate(this);
        for (ConstraintViolation<ForwarderAirFreightForm> constraintViolation : set) {
            return CommonResult.error(400, constraintViolation.getMessage());
        }
        return null;
    }
}
