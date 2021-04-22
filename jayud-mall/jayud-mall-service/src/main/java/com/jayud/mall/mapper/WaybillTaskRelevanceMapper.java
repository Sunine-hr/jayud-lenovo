package com.jayud.mall.mapper;

import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.WaybillTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 运单(订单）任务关联 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Mapper
@Component
public interface WaybillTaskRelevanceMapper extends BaseMapper<WaybillTaskRelevance> {

    /**
     * 根据订单id，找运营组和任务(组装数据)
     * @param orderId
     * @return
     */
    List<WaybillTaskVO> findWaybillTaskByOrderInfoId(@Param("orderId") Long orderId);
}
