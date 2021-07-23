package com.jayud.oms.service;

import com.jayud.common.utils.FileView;
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
     *
     * @param form
     * @return
     */
    List<LogisticsTrackVO> findReplyStatus(QueryLogisticsTrackForm form);

    /*
     * 根据条件查询
     */
    List<LogisticsTrack> getByCondition(LogisticsTrack logisticsTrack);

    /**
     * 获取附件
     *
     * @return
     */
    List<FileView> getAttachments(Long orderId, Integer businessType, String path);

    /**
     * 根据orderId和类型删除物流轨迹跟踪信息
     */
    public boolean deleteLogisticsTrackByType(Long orderId,Integer type);


    /**
     * 根据子订单id查询所有子订单物流轨迹
     * @param subOrderIds
     * @param type
     * @return
     */
    public List<LogisticsTrack>  getLogisticsTrackByType(List<Long> subOrderIds,Integer type);

    /**
     * 根据子订单id和订单状态以及子订单类型获取历史轨迹
     * @param id
     * @param status
     * @param type
     * @return
     */
    LogisticsTrack getLogisticsTrackByOrderIdAndStatusAndType(Long id, String status, int type);
}
