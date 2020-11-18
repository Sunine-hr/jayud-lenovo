package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jayud.common.utils.DateUtils.DATE_TIME_PATTERN;

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

    @ApiModelProperty(value = "中港订单id", required = true)
    @NotNull(message = "中港订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "车辆通关选择的通关状态(T_9:通过完成,T_9_1:车辆通关查验,T_9_2:车辆通关其他异常)")
    private String status;

    @ApiModelProperty(value = "操作状态(0车辆提货,1车辆过磅,2车辆通关,3货物派送,4订单签收)")
    private Integer optStatus;
    @ApiModelProperty(value = "过磅数")
    private Double carWeighNum;

//    @ApiModelProperty(value = "操作人")
//    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
//    @NotEmpty(message = "操作时间不能为空")
    private String operatorTime= DateUtils.format(new Date(),DATE_TIME_PATTERN);

//    @ApiModelProperty(value = "附件")
//    private String statusPic;
//
//    @ApiModelProperty(value = "附件名称")
//    private String statusPicName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    //    @ApiModelProperty(value = "备注")
    //    private String description;


    @ApiModelProperty(value = "通关时间")
    private String goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private String preGoCustomsTime;


    @ApiModelProperty(value = "操作指令,cmd = extCustomsRelease外部报关放行,confirmOrder确认接单,carTakeGoods车辆提货,carWeigh车辆过磅, " +
            "goCustomsAudit通过前审核，goCustomsCheck通关前复核,carGoCustoms车辆通关,hkClearCustoms香港清关,carEnterWarehouse车辆入仓," +
            "carOutWarehouse车辆出仓,carSend车辆派送,confirmSignIn确认签收", required = true)
    private String cmd;

    @ApiModelProperty(value = "下一步指令")
    private String nextCmd;
}
