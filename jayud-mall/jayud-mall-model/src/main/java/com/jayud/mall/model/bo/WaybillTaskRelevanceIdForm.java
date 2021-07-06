package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class WaybillTaskRelevanceIdForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "id不能为空")
    private Long id;

    //物流轨迹
    @ApiModelProperty(value = "物流轨迹描述")
    private String logisticsTrackDescription;

    @ApiModelProperty(value = "物流轨迹时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime logisticsTrackCreateTime;

    //延期 -- 仅针对 延期
    @ApiModelProperty(value = "延期天数，默认延期1天")
    private Integer postponeDays;


}
