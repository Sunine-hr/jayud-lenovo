package com.jayud.mall.model.bo;

import com.jayud.mall.model.vo.TemplateUrlVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsClearanceFileForm {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "商品id(customer_goods id)")
    private Integer goodId;

    @ApiModelProperty(value = "申报类型(2 清关)")
    private Integer type;

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
