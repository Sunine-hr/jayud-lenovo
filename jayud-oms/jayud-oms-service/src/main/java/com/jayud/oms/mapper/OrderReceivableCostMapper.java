package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputReceivableCostVO;
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
                                                        @Param("orderNos") List<String> orderNos, List<Long> legalIds);
}
