package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.OrderTrack;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单轨迹 服务类
 *
 * @author jyd
 * @since 2021-12-18
 */
public interface IOrderTrackService extends IService<OrderTrack> {

    /**
    *  分页查询
    * @param orderTrack
    * @param req
    * @return
    */
    IPage<OrderTrack> selectPage(OrderTrack orderTrack,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param orderTrack
    * @return
    */
    List<OrderTrack> selectList(OrderTrack orderTrack);

    /**
     * 保存(新增+编辑)
     * @param orderTrack
     */
    OrderTrack saveOrUpdateOrderTrack(OrderTrack orderTrack);

    /**
     * 逻辑删除
     * @param id
     */
    void delOrderTrack(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryOrderTrackForExcel(Map<String, Object> paramMap);


}
