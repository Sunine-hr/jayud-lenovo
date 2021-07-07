package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomsBaseFileVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "类型(1报关 2清关)")
    private Integer type;

    @ApiModelProperty(value = "海关基础资料id(customs_data id customs_clearance id)")
    private Long customsId;

    @ApiModelProperty(value = "国家代码(country code)")
    private String countryCode;

    @ApiModelProperty(value = "国家名称(country name)")
    private String countryName;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "模版文件地址")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    private String describe;

    //上传文件
    @ApiModelProperty(value = "模版文件地址(附件)文件上传")
    private List<TemplateUrlVO> templateUrls;

}
