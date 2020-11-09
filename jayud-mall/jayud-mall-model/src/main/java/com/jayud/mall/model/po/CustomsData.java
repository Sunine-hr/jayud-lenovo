package com.jayud.mall.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报关资料表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomsData对象", description="报关资料表")
public class CustomsData extends Model<CustomsData> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IDcode，报关ID")
    private String idCode;

    @ApiModelProperty(value = "报关国家代码")
    private String cnCode;

    @ApiModelProperty(value = "报关HSCODE")
    private String hsCode;

    @ApiModelProperty(value = "报关中文品名")
    private String chName;

    @ApiModelProperty(value = "报关英文品名")
    private String enName;

    @ApiModelProperty(value = "材质")
    private String texture;

    @ApiModelProperty(value = "用途")
    private String uses;

    @ApiModelProperty(value = "申报单价")
    private BigDecimal declarePrice;

    @ApiModelProperty(value = "申报单价单位(currency 对应id)")
    private Integer cid;

    @ApiModelProperty(value = "净重")
    private BigDecimal suttle;

    @ApiModelProperty(value = "净重单位")
    private String suttleUnit;

    @ApiModelProperty(value = "毛重")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "毛重单位")
    private String grossUnit;

    @ApiModelProperty(value = "退税税率%")
    private Double drawbackTax;

    @ApiModelProperty(value = "图片")
    private String picUrl;

    @ApiModelProperty(value = "申报要素")
    private String declareElements;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
