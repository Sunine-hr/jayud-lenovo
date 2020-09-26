package com.jayud.oms.mapper;

import com.jayud.oms.model.bo.QueryLogisticsTrackForm;
import com.jayud.oms.model.po.LogisticsTrack;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.LogisticsTrackVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 物流轨迹跟踪表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-22
 */
@Mapper
public interface LogisticsTrackMapper extends BaseMapper<LogisticsTrack> {

    List<LogisticsTrackVO> findReplyStatus(QueryLogisticsTrackForm form);

}
