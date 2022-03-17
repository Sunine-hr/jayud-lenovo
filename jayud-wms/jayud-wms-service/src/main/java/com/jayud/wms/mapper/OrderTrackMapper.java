package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.OrderTrack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单轨迹 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-18
 */
@Mapper
public interface OrderTrackMapper extends BaseMapper<OrderTrack> {
    /**
    *   分页查询
    */
    IPage<OrderTrack> pageList(@Param("page") Page<OrderTrack> page, @Param("orderTrack") OrderTrack orderTrack);

    /**
    *   列表查询
    */
    List<OrderTrack> list(@Param("orderTrack") OrderTrack orderTrack);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryOrderTrackForExcel(Map<String, Object> paramMap);
}
