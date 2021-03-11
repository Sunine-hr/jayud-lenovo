package com.jayud.trailer.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.trailer.po.TrailerDispatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 海运节点操作流程
 */
@Data
@Slf4j
public class TrailerProcessOptForm {

    @NotNull(message = "主订单id不能为空")
    @ApiModelProperty(value = "主订单id", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "拖车订单号", required = true)
    private String orderNo;

    @NotNull(message = "拖车订单id不能为空")
    @ApiModelProperty(value = "拖车订单id", required = true)
    private Long id;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "派车信息")
    private AddTrailerDispatchFrom trailerDispatchVO = new AddTrailerDispatchFrom();

    @ApiModelProperty(value = "业务类型(0:空运),(1,纯报关),(2,中港运输) 前端不用管")
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

    public void checkProcessOpt(OrderStatusEnum statusEnum) {
        boolean pass = true;
        switch (statusEnum) {
            case TT_1: //拖车接单
            case TT_4: //拖车提柜
            case TT_5: //拖车到仓
            case TT_6: //拖车离仓
            case TT_8: //确认还柜
                pass = checkOptInfo();
                break;
            case TT_2: //拖车派车
            case TT_3: //派车审核
                if (StringUtils.isNotEmpty(this.paperStripSeal)) throw new JayudBizException("封条必填", 400);
                if (StringUtils.isNotEmpty(this.cabinetNumber)) throw new JayudBizException("柜号必填", 400);
                if (!checkOptInfo()) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
                pass = this.trailerDispatchVO.checkDispatchOptParam();
                break;
            case TT_7: //拖车过磅
                pass = this.trailerDispatchVO.checkWeightOptParam();
                if (!checkOptInfo()) throw new JayudBizException(ResultEnum.VALIDATE_FAILED);
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
