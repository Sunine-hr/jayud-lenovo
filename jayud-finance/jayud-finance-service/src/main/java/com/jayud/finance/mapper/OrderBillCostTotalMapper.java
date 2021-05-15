package com.jayud.finance.mapper;

import com.jayud.finance.po.OrderBillCostTotal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.vo.OrderBillCostTotalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderBillCostTotalMapper extends BaseMapper<OrderBillCostTotal> {

    /**
     * 根据费用ID获取元素费用信息，并根据结算币种转换
     * @param costIds
     * @param settlementCurrency
     * @return
     */
    List<OrderBillCostTotalVO> findOrderFBillCostTotal(@Param("costIds") List<Long> costIds,@Param("settlementCurrency") String settlementCurrency,
                                                       @Param("accountTermStr") String accountTermStr);

    /**
     * 根据费用ID获取元素费用信息，并根据结算币种转换
     * @param costIds
     * @param settlementCurrency
     * @return
     */
    List<OrderBillCostTotalVO> findOrderSBillCostTotal(@Param("costIds") List<Long> costIds,@Param("settlementCurrency") String settlementCurrency,
                                                       @Param("accountTermStr") String accountTermStr);


    List<Map<String,Object>> totalCurrencyAmount(@Param("billNos")List<String> billNos);
}
