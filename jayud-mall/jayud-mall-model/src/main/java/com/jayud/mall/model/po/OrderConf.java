package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 配载单
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderConf对象", description="配载单")
public class OrderConf extends Model<OrderConf> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "配载单号")
    private String orderNo;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "目的国家代码(country code)")
    private String destinationCountryCode;

    @ApiModelProperty(value = "创建用户id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "配载名称")
    private String confName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态代码\n" +
            "\n" +
            "0:准备\n" +
            "10:启用\n" +
            "20:开始配载\n" +
            "30:转运中\n" +
            "40:完成\n" +
            "-1:取消")
    private String statusCode;

    @ApiModelProperty(value = "状态名称")
    private String statusName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
