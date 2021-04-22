package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromoteOrderVO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "公司id/渠道id(promote_company company_id)")
    private Integer companyId;

    @ApiModelProperty(value = "公司名称/渠道公司名称(promote_company company_name)")
    private String companyName;

    @ApiModelProperty(value = "(客户)公司名")
    private String clientCompanyName;

    @ApiModelProperty(value = "(客户)联系人")
    private String clientContacts;

    @ApiModelProperty(value = "(客户)联系电话")
    private String clientPhone;

    @ApiModelProperty(value = "(客户)公司地址")
    private String clientCompanyAddress;

    @ApiModelProperty(value = "(客户)运营平台")
    private String clientManagePlatform;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
