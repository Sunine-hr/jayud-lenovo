package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.bo.QueryStatisticalReport;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputReceivableCostVO;
import com.jayud.oms.model.vo.StatisticsOrderBaseCost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单对应应收费用明细 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderReceivableCostMapper extends BaseMapper<OrderReceivableCost> {

    List<InputReceivableCostVO> findReceivableCost(@Param("form") GetCostDetailForm form);

    InputReceivableCostVO getWriteBackSCostData(@Param("costId") Long costId);

    /**
     * 查询待处理费用审核
     */
    List<Map<String, Object>> getPendingExpenseApproval(@Param("subType") String subType,
                                                        @Param("orderNos") List<String> orderNos,
                                                        @Param("legalIds") List<Long> legalIds);

    /**
     * 统计应收金额
     *
     * @param orderNos
     * @param subType
     * @param status
     * @return
     */
    List<Map<String, Object>> statisticsOrderAmount(@Param("orderNos") List<String> orderNos,
                                                    @Param("subType") String subType, @Param("status") List<String> status);

    /**
     * 统计主订单费用
     *
     * @param form
     * @param legalIds
     * @param status
     * @return
     */
    List<Map<String, Object>> statisticsMainOrderCost(@Param("form") QueryStatisticalReport form, @Param("legalIds") List<Long> legalIds, @Param("status") List<String> status);

    List<StatisticsOrderBaseCost> getBaseStatisticsAllCost(@Param("form") QueryStatisticalReport form,
                                                           @Param("legalIds") List<Long> legalIds,
                                                           @Param("status") List<String> status);
}
