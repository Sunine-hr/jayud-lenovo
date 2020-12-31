package com.jayud.airfreight.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 空运异常反馈
 * </p>
 *
 * @author LDR
 * @since 2020-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AirExceptionFeedback对象", description="空运异常反馈")
public class AddAirExceptionFeedbackForm extends Model<AddAirExceptionFeedbackForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "空运订单id")
    @NotNull(message = "空运订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "异常原因(1.货物破损,2.潮湿,3.丢失,4.其他)")
    @NotBlank(message = "请选择异常原因")
    private Integer type;

    @ApiModelProperty(value = "异常描述")
    private String describe;

    @ApiModelProperty(value = "异常发生时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "预计完成时间")
    private LocalDateTime completionTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
