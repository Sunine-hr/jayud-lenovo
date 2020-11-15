package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 司机反馈状态操作
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-13
 */
@Data
public class DriverFeedbackStatusForm extends Model<DriverFeedbackStatusForm> {


    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单ID")
    private Long orderId;

    @ApiModelProperty(value = "车辆通关选择的通关状态(T_9:通过完成,T_9_1:车辆通关查验,T_9_2:车辆通关其他异常)")
    private String clearanceStatus;

    @ApiModelProperty(value = "操作状态(0车辆提货,1车辆过磅,2车辆通关,3货物派送,4订单签收)")
    private Integer optStatus;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件")
    private String statusPic;

    @ApiModelProperty(value = "附件名称")
    private String statusPicName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    //    @ApiModelProperty(value = "备注")
    //    private String description;


    @ApiModelProperty(value = "通关时间")
    private String goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private String preGoCustomsTime;

}
