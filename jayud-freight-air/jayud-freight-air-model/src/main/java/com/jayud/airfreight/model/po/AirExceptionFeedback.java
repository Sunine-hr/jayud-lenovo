package com.jayud.airfreight.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class AirExceptionFeedback extends Model<AirExceptionFeedback> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "空运订单id")
    private Long orderId;

    @ApiModelProperty(value = "异常原因(1.货物破损,2.潮湿,3.丢失,4.其他)")
    private Integer type;

    @ApiModelProperty(value = "异常描述")
    private String remarks;

    @ApiModelProperty(value = "异常发生时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "预计完成时间")
    private LocalDateTime completionTime;

    @ApiModelProperty(value = "文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
