package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 入库节点操作流程
 */
@Data
@Slf4j
public class StorageOutProcessOptForm {

    @NotNull(message = "主订单id不能为空")
    @ApiModelProperty(value = "主订单id", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "出库订单号", required = true)
    private String orderNo;

    @NotNull(message = "出库订单id不能为空")
    @ApiModelProperty(value = "出库订单id", required = true)
    private Long orderId;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private String operatorTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @ApiModelProperty(value = "备注")
    private String description;

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

    @ApiModelProperty(value = "操作情况 success 审核通过  fail 审核不通过")
    private String cmd;

    @ApiModelProperty(value = " 出库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "商品出库")
    private List<OutWarehouseGoodsForm> outWarehouseGoodsForms;


    public void setStatus(String status) {
        this.status = status;
        this.statusName = OrderStatusEnum.getDesc(status);
    }

    public void checkProcessOpt(OrderStatusEnum statusEnum) {
        boolean pass = true;
        switch (statusEnum) {
            case CCE_1: //出库接单
//            case CCE_2: //打印拣货单
//            case CCE_3: //仓储拣货
            case CCE_4: //出仓确认
            case CCE_5: //出仓异常
                pass = checkOptInfo();
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
