package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverBillCostVO;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;
import com.jayud.oms.model.vo.StatisticsOrderBaseCostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单对应应付费用明细 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderPaymentCostMapper extends BaseMapper<OrderPaymentCost> {

    List<InputPaymentCostVO> findPaymentCost(@Param("form") GetCostDetailForm form);

    List<DriverOrderPaymentCostVO> getDriverOrderPaymentCost(@Param("orderNo") String orderNo);

    InputPaymentCostVO getWriteBackFCostData(@Param("costId") Long costId);

    /**
     * 查询待处理费用审核
     */
    List<Map<String, Object>> getPendingExpenseApproval(@Param("subType") String subType,
                                                        @Param("orderNos") List<String> orderNos,
                                                        @Param("legalIds") List<Long> legalIds);

    /**
     * 查询供应商应付异常费用
     *
     * @param form
     * @return
     */
    List<InputPaymentCostVO> getSupplierAbnormalCostDetail(GetCostDetailForm form);

    /**
     * 统计主订单费用
     *
     * @param form
     * @param legalIds
     * @param status
     * @return
     */
    List<Map<String, Object>> statisticsMainOrderCost(@Param("form") QueryStatisticalReport form, @Param("legalIds") List<Long> legalIds, @Param("status") List<String> status);

    List<StatisticsOrderBaseCostVO> getBaseStatisticsAllCost(@Param("form") QueryStatisticalReport form,
                                                             @Param("legalIds") List<Long> legalIds,
                                                             @Param("status") List<String> status);

    List<DriverBillCostVO> getDriverBillCost(@Param("orderNos") List<String> orderNos,
                                             @Param("status") List<String> status,@Param("time") String time);
}
