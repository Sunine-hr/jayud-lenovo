package com.jayud.Inlandtransport.model.bo;

import com.jayud.common.UserOperator;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点操作流程
 */
@Data
@Slf4j
public class ProcessOptForm {


    @ApiModelProperty(value = "主订单id", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "操作人")
    private String operatorUser = UserOperator.getToken();

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "审核意见")
    private String describes;

    @ApiModelProperty(value = "派车信息")
    private SendCarForm sendCarForm;

    @ApiModelProperty(value = "业务类型 前端不用管")
    private Integer businessType;

    @ApiModelProperty(value = "状态码,前台不用传")
    private String status;

    @ApiModelProperty(value = "状态名称,前台不用传")
    private String statusName;

    @ApiModelProperty(value = "附件,前台不用传")
    private String statusPic;

    @ApiModelProperty(value = "附件名称,前台不用传")
    private String statusPicName;

    public void setStatus(String status) {
        this.status = status;
        this.statusName = OrderStatusEnum.getDesc(status);
    }

    public void checkProcessOpt(OrderStatusEnum nextStatus) {
        boolean pass = true;
        if (nextStatus == null) {
            throw new JayudBizException("不存在该状态的操作");
        }

        switch (nextStatus) {
            case INLANDTP_NL_2: //派车
                if (this.sendCarForm == null) throw new JayudBizException("请填写派车信息");
                //设置操作时间
                this.sendCarForm.checkSendCarOptParam();
                this.operatorTime = DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN);
                break;
            case INLANDTP_NL_1: //接单
            case INLANDTP_NL_4: //确认派车
            case INLANDTP_NL_5: //车辆提货
            case INLANDTP_NL_6:
                pass = checkOptInfo();
                break;
            case INLANDTP_NL_3: //派车审核
                //设置操作时间
                this.operatorTime = DateUtils.LocalDateTime2Str(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN);
                break;
        }
        if (!pass) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
    }


    public boolean checkOptInfo() {
        if (StringUtils.isEmpty(this.operatorUser)) {
            log.warn("操作人必填");
            return false;
        }
        if (StringUtils.isEmpty(this.operatorTime)) {
            log.warn("操作时间必填");
            return false;
        }
        return true;
    }
}
