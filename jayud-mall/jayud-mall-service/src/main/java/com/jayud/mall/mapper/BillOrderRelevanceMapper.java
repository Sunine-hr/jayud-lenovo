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
}
