package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.LogisticsTrack;
import com.jayud.mall.model.vo.LogisticsTrackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 物流轨迹跟踪表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-25
 */
@Mapper
@Component
public interface LogisticsTrackMapper extends BaseMapper<LogisticsTrack> {

    List<LogisticsTrackVO> findLogisticsTrackByOrderId(@Param("orderId") String orderId);

}
