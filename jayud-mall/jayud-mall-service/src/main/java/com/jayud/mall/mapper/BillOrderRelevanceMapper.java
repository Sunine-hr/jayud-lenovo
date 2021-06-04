package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.BillOrderRelevance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单关联订单(任务通知表) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-17
 */
@Mapper
@Component
public interface BillOrderRelevanceMapper extends BaseMapper<BillOrderRelevance> {

    /**
     * 根据提单id，查询 提单关联订单(任务通知表)
     * @param billId
     * @return
     */
    List<BillOrderRelevance> findBillOrderRelevanceByBillId(@Param("billId") Long billId);

    /**
     * 查询提单关联的订单，是否通知物流轨迹
     * @param billId  提单id(ocean_bill id)
     * @param isInform 是否通知运单物流轨迹(1通知 2不通知)
     * @return
     */
    List<BillOrderRelevance> findBillOrderRelevanceByBillIdAndIsInform(@Param("billId") Integer billId, @Param("isInform") String isInform);
}
