package com.jayud.tools.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * fba订单轨迹表
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FbaOrderTrack对象", description="fba订单轨迹表")
public class FbaOrderTrack extends Model<FbaOrderTrack> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "轨迹时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime trajectoryTime;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @ApiModelProperty(value = "操作信息")
    private String operationInformation;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
