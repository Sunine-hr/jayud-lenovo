package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
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

    @ApiModelProperty("经纬度数组")
    private List<List<Double>> pointPositions;

    @ApiModelProperty(value = "gps信息")
    private List<GpsPositioningVO> gpsPositioningVOs;


}
