package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillCustomsCounterListVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "类型(1 报关 2清关)")
    private Integer type;

    @ApiModelProperty(value = "报关、清关id(报关bill_customs_info id 清关bill_clearance_info id)")
    private Long customsId;

    @ApiModelProperty(value = "柜子清单id(counter_list_info id)")
    private Long counterListId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*柜子清单信息*/
    @ApiModelProperty(value = "柜子id(ocean_counter id)")
    private Long counterId;

    @ApiModelProperty(value = "柜号(ocean_counter cntr_no)")
    private String cntrNo;

    @ApiModelProperty(value = "清单名称")
    private String fileName;

    @ApiModelProperty(value = "模版文件地址(附件)")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    private String describes;

    @ApiModelProperty(value = "总箱数")
    private Integer cartons;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;



}
