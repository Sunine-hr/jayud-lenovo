package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.vaildator.DateTime;
import com.jayud.common.vaildator.NumberEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 空运提单文件form
 *
 * @author william
 * @description
 * @Date: 2020-09-16 09:04
 */
@Data
public class ForwarderLadingFileForm {
    @JsonProperty("Booking_no")
    @SerializedName("Booking_no")
    @ApiModelProperty(value = "订单编号")
    @NotEmpty(message = "订单编号不能为空")
    @Length(max = 32, message = "订单编号字段长度最大为32")
    private String bookingNo;

    @JsonProperty("Forwarder_Booking_no")
    @SerializedName("Forwarder_Booking_no")
    @ApiModelProperty(value = "货代订单编号")
    @Length(max = 50, message = "货代订单编号字段长度最大为50")
    private String forwarderBookingNo;

    @JsonProperty("File_type")
    @SerializedName("File_type")
    @ApiModelProperty(value = "文件类型")
    @NotNull(message = "文件类型不能为空")
    @NumberEnum(enums = "1,2,3,4", message = "1=提单（B/L）、2=签收单（POD）、3=货损（cargo_damage）、4=其他提单文件（other）")
    private Integer fileType;

    @JsonProperty("ID")
    @SerializedName("ID")
    @ApiModelProperty(value = "唯一标识码")
    @Length(max = 32, message = "唯一标识码字段长度最大为32")
    private String id;

    @JsonProperty("Operation_type")
    @SerializedName("Operation_type")
    @ApiModelProperty(value = "操作类型")
    @Length(max = 10, message = "操作类型字段长度最大为10")
    @Pattern(regexp = "(add|update|delete)", message = "只允许填写 add/update/delete")
    private String operationType;

    @JsonProperty("Anomaly_classification")
    @SerializedName("Anomaly_classification")
    @ApiModelProperty(value = "异常分类")
    @Length(max = 50, message = "异常分类字段长度最大为50")
    @Pattern(regexp = "(1|2|3)", message = "异常分类项：1.货物破损、2.潮湿、3.丢失")
    private String anomalyClassification;

    @JsonProperty("Abnormal")
    @SerializedName("Abnormal")
    @ApiModelProperty(value = "异常描述")
    @Length(max = 500, message = "异常描述字段长度最大为500")
    private String abnormal;

    @JsonProperty("Occurrence_time")
    @SerializedName("Occurrence_time")
    @ApiModelProperty(value = "异常发生时间")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常发生时间必须为时间格式yyyy/M/dd HH:mm")
//    @JsonFormat(pattern = "yyyy/M/dd HH:mm",shape = JsonFormat.Shape.STRING,timezone = "GMT+8")
    private String occurrenceTime;

    @JsonProperty("Exception_finish_time")
    @SerializedName("Exception_finish_time")
    @ApiModelProperty(value = "异常处理完成时间")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常处理完成时间必须为时间格式yyyy/M/dd HH:mm")
//    @JsonFormat(pattern = "yyyy/M/dd HH:mm",shape = JsonFormat.Shape.STRING,timezone = "GMT+8")
    private String exceptionFinishTime;

    @JsonProperty("Exception_process")
    @SerializedName("Exception_process")
    @ApiModelProperty(value = "异常处理过程")
    @Length(max = 200, message = "异常处理过程字段长度最大为200")
    private String exceptionProcess;
}
