package com.jayud.tms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.tms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 中港运输订单 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderTransportMapper extends BaseMapper<OrderTransport> {

    /**
     * 获取订单详情
     *
     * @param mainOrderNo
     * @return
     */
    public InputOrderTransportVO getOrderTransport(@Param("mainOrderNo") String mainOrderNo);

    /**
     * 分页查询
     *
     * @param page
     * @param form
     * @return
     */
    IPage<OrderTransportVO> findTransportOrderByPage(Page page, @Param("form") QueryOrderTmsForm form);

    /**
     * 确认派车渲染PDF数据
     *
     * @param orderNo
     * @param classCode
     * @return
     */
    SendCarPdfVO initPdfData(@Param("orderNo") String orderNo, @Param("classCode") String classCode);

    /**
     * 分页查询司机的中港订单信息
     */
    List<DriverOrderTransportVO> getDriverOrderTransport(@Param("form") QueryDriverOrderTransportForm form,
                                                         @Param("status") List<String> status);

    /**
     * 中港运输各个菜单列表数据量统计
     * @return
     */
    StatisticsDataNumberVO statisticsDataNumber();
}
