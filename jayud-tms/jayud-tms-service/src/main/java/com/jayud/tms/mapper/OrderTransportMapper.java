package com.jayud.tms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.entity.DataControl;
import com.jayud.tms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.model.vo.statistical.BusinessPeople;
import com.jayud.tms.model.vo.statistical.TVOrderTransportVO;
import com.jayud.tms.model.vo.supplier.QuerySupplierBill;
import com.jayud.tms.model.vo.supplier.QuerySupplierBillInfo;
import com.jayud.tms.model.vo.supplier.SupplierBill;
import com.jayud.tms.model.vo.supplier.SupplierBillInfo;
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
     *
     * @return
     */
    StatisticsDataNumberVO statisticsDataNumber();

    /**
     * 根据主订单编号查询中港详情
     *
     * @param mainOrderNo
     * @return
     */
    List<OrderVO> getOrderTransportByMainOrderNo(@Param("mainOrderNo") List<String> mainOrderNo);

    /**
     * 分页查询
     *
     * @param dataControl
     * @param page
     * @param form
     * @return
     */
    IPage<OrderTransportVO> findTransportOrderByPage(Page<OrderTransportVO> page, @Param("form") QueryOrderTmsForm form,
                                                     @Param("dataControl") DataControl dataControl);

    /**
     * 查询订单状态数量
     *
     * @param status
     * @param dataControl
     * @return
     */
    public Integer getNumByStatus(@Param("status") String status,
                                  @Param("dataControl") DataControl dataControl);

    /**
     * 大屏幕展示订单数据(分页查询)
     *
     * @param page
     * @param legalNames
     * @param
     * @return
     */
    IPage<TVOrderTransportVO> findTVShowOrderByPage(Page<TVOrderTransportVO> page,
                                                    @Param("legalNames") List<String> legalNames);


    /**
     * 统计业务员排名(分页查询)
     *
     * @return
     */
    IPage<BusinessPeople> findStatisticsSalesmanRanking(Page<BusinessPeople> page,
                                                        @Param("legalNames") List<String> legalNames);

    /**
     * 根据主订单号集合查询中港详情信息
     *
     * @param mainOrderNos
     * @return
     */
    List<OrderTransportInfoVO> getTmsOrderInfoByMainOrderNos(@Param("mainOrderNos") List<String> mainOrderNos);

    /**
     * 根据主订单号查询中港详情信息
     *
     * @return
     */
    OrderTransportInfoVO getTmsOrderInfoById(@Param("id") Long id);

    /**
     * 分页查询供应商账单
     *
     * @param page
     * @param form
     * @return
     */
    IPage<SupplierBill> findSupplierBillByPage(Page<SupplierBill> page,
                                               @Param("form") QuerySupplierBill form);


    /**
     * 分页查询供应商账单
     *
     * @param page
     * @param form
     * @return
     */
    IPage<SupplierBillInfo> findSupplierBillInfoByPage(Page<SupplierBillInfo> page,
                                                       @Param("form") QuerySupplierBillInfo form);

    Integer isVirtualWarehouseByOrderNo(String orderNo);
}
