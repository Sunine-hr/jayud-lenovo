package com.jayud.oms.service;

import com.jayud.oms.model.bo.QueryLogisticsTrackForm;
import com.jayud.oms.model.po.LogisticsTrack;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.LogisticsTrackVO;

import java.util.List;

/**
 * <p>
 * 物流轨迹跟踪表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-22
 */
public interface ILogisticsTrackService extends IService<LogisticsTrack> {

    /**
     * 获取反馈状态
     * @param form
     * @return
     */
    List<LogisticsTrackVO> findReplyStatus(QueryLogisticsTrackForm form);
}
