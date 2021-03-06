package com.jayud.oms.mapper;

import com.jayud.oms.model.po.GpsPositioning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-08-30
 */
@Mapper
public interface GpsPositioningMapper extends BaseMapper<GpsPositioning> {

    List<GpsPositioning> findGpsPositioningByPlateNumbers(@Param("plateNumbers") List<String> plateNumbers);
}
