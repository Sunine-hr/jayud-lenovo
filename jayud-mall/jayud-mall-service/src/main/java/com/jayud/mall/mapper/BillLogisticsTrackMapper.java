package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.BillLogisticsTrack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提单物流轨迹跟踪表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-04
 */
@Mapper
@Component
public interface BillLogisticsTrackMapper extends BaseMapper<BillLogisticsTrack> {

}
