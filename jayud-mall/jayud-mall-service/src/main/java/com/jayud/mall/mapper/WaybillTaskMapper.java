package com.jayud.mall.mapper;

import com.jayud.mall.model.po.WaybillTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.WaybillTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 运单(订单)任务列表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Mapper
@Component
public interface WaybillTaskMapper extends BaseMapper<WaybillTask> {

    /**
     * 根据报价id，查询运单任务信息list
     * @param offerInfoId 报价id
     * @return
     */
    @Deprecated
    List<WaybillTaskVO> findWaybillTaskByOfferInfoId(@Param(value = "offerInfoId") Integer offerInfoId);

    /**
     * 根据订单id，查询运单任务信息list
     * @param orderInfoId
     * @return
     */
    @Deprecated
    List<WaybillTaskVO> findWaybillTaskByOrderInfoId(@Param("orderInfoId") Long orderInfoId);
}
