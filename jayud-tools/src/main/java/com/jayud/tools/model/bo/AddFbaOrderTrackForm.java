package com.jayud.tools.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * fba订单轨迹表
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Data
public class AddFbaOrderTrackForm {

    @ApiModelProperty(value = "自增id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "轨迹时间")
    private String trajectoryTime;

    @ApiModelProperty(value = "订单id集合")
    private List<Integer> orderIds;

    @ApiModelProperty(value = "操作信息")
    private String operationInformation;

    @ApiModelProperty(value = "备注")
    private String remark;
}
