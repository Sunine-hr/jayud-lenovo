package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.oms.model.vo.gps.GPSOrderInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * GPS轨迹回放
 * </p>
 *
 * @author LDR
 * @since 2021-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TrackPlaybackVO extends Model<TrackPlaybackVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty("经纬度数组")
    private List<List<Double>> pointPositions;

    @ApiModelProperty(value = "gps信息")
    private List<GpsPositioningVO> gpsPositioningVOs;


    public void assemblyBasicData(GPSOrderInfoVO gpsOrderInfoVO) {
        this.plateNumber = gpsOrderInfoVO.getPlateNumber();
        this.driverName = gpsOrderInfoVO.getDriverName();
        this.customerName = gpsOrderInfoVO.getCustomerName();
        this.goodsInfo = gpsOrderInfoVO.getGoodsInfo();
    }
}
