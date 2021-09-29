package com.jayud.oms.model.bo;

import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.model.vo.GpsPositioningVO;
import com.jayud.oms.model.vo.TrackPlaybackVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
public class QueryGPSRecord {

    @ApiModelProperty(value = "统计维度(1:车辆维度 2订单维度)")
    private Integer type;
    //    @ApiModelProperty(value = "供应商")
//    private Long supplierId;
    @ApiModelProperty(value = "车牌")
    private String licensePlate;

    @ApiModelProperty(value = "时间区间")
    private List<String> timeInterval;

    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "子订单类型")
    private String subType;
    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(GpsPositioningVO.class));
    }

    public void setTimeInterval(List<String> timeInterval) {
        this.timeInterval = timeInterval;
        if (!CollectionUtils.isEmpty(timeInterval)) {
            this.startTime = timeInterval.get(0);
            this.endTime = timeInterval.get(1);
        }
    }

    public void checkParam() {
        if (type == null) throw new JayudBizException("选择车辆或单据");
        switch (this.type) {
            case 1:
                if (StringUtils.isEmpty(this.licensePlate)) {
                    throw new JayudBizException("请选择车辆信息");
                }
                if (CollectionUtils.isEmpty(timeInterval)) {
                    throw new JayudBizException("请选择区间范围");
                }
                break;
            case 2:
                if (StringUtils.isEmpty(this.subType)) {
                    throw new JayudBizException(ResultEnum.PARAM_ERROR);
                }
                if (StringUtils.isEmpty(orderNo)) {
                    throw new JayudBizException("请选择订单号");
                }
                break;
        }

    }
}
