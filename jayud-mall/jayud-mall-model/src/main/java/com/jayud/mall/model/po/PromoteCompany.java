package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 推广公司表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PromoteCompany对象", description = "推广公司表")
public class PromoteCompany extends Model<PromoteCompany> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公司id/渠道id")
    @TableId(value = "company_id", type = IdType.AUTO)
    private Integer companyId;

    @ApiModelProperty(value = "公司名称/渠道公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "公司地址")
    private String companyAddress;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    @ApiModelProperty(value = "创建人id(system_user id)")
    private Long createId;

    @ApiModelProperty(value = "创建人名称(system_user name)")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "客户数")
    private Integer clientNumber;

    @ApiModelProperty(value = "父id(promote_company company_id)")
    private Integer parentId;

    @ApiModelProperty(value = "标题")
    private String title;

    @Override
    protected Serializable pkVal() {
        return this.companyId;
    }

}
