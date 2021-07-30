package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 国家表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BCountryVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "国家代码")
    private String codeNo;

    @ApiModelProperty(value = "国家中文名称")
    private String nameCh;

    @ApiModelProperty(value = "国家英文名称")
    private String nameEn;

    @ApiModelProperty(value = "过关海关编码")
    private String hsCode;

    @ApiModelProperty(value = "国家国际代码")
    private String codeInter;

}
