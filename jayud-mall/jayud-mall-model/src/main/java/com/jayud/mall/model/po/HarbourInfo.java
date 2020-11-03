package com.jayud.mall.model.po;

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
 * 机场、港口信息
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HarbourInfo对象", description="机场、港口信息")
public class HarbourInfo extends Model<HarbourInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "代码")
    private String idCode;

    @ApiModelProperty(value = "中文名称")
    private String codeName;

    @ApiModelProperty(value = "英文名称")
    private String codeNameEn;

    @ApiModelProperty(value = "国家代码")
    private String stateCode;

    @ApiModelProperty(value = "地址一")
    private String addressFirst;

    @ApiModelProperty(value = "地址二")
    private String addressSecond;

    @ApiModelProperty(value = "地址三")
    private String addressThirdly;

    @ApiModelProperty(value = "省id")
    private Integer pid;

    @ApiModelProperty(value = "省/州名")
    private String pname;

    @ApiModelProperty(value = "城市id")
    private Integer cid;

    @ApiModelProperty(value = "城市名")
    private String cname;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "类型(1机场 2港口)")
    private Integer genre;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private Integer userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
