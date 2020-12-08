package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.enums.VivoFileTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.vaildator.EnumAnnotation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BookingFileTransferDataForm {

    @NotEmpty(message = "Booking编号必填")
    @Length(max = 32, message = "Booking编号最大长度32位")
    @ApiModelProperty(value = "Booking编号")
    private String bookingNo;

    @NotNull(message = "fileType必填")
    @EnumAnnotation(message = "File_type状态错误", target = {VivoFileTypeEnum.class})
    private Integer fileType;

    @NotNull(message = "File_size必填")
    private Integer fileSize;

    //订舱文件
    private FileView fileView;

}
