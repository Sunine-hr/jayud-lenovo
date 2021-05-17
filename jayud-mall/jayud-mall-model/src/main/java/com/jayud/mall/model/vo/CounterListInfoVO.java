package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CounterListInfoVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "柜子id(ocean_counter id)")
    private Long counterId;

    @ApiModelProperty(value = "柜号(ocean_counter cntr_no)")
    private String cntrNo;

    @ApiModelProperty(value = "清单名称")
    private String fileName;

    @ApiModelProperty(value = "模版文件地址(附件)")
    private String templateUrl;

    @ApiModelProperty(value = "模版文件地址(附件)文件上传")
    private List<TemplateUrlVO> templateUrls;

    @ApiModelProperty(value = "说明")
    private String describes;

    @ApiModelProperty(value = "总箱数")
    private Integer cartons;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "柜子箱号信息list")
    private List<CounterCaseInfoVO> counterCaseInfos;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Long billId;

}
