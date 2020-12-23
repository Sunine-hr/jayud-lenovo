package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 派车单
 */
@Data
public class SendCarListPdfVO {

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "接单法人英文")
    private String legalEnName;

    @ApiModelProperty(value = "工作号")
    private String jobNumber;

    @ApiModelProperty(value = "日期")
    private String createTimeStr;

    @ApiModelProperty(value = "派车集合")
    private List<SendCarListVO> sendCarListVOList = new ArrayList<>();

}
