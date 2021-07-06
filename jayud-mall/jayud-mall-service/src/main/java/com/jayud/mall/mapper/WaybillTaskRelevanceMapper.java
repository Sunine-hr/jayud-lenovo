package com.jayud.mall.mapper;

import com.jayud.mall.model.bo.WaybillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;
import com.jayud.mall.model.vo.WaybillTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询，运单任务
     * @param form
     * @return
     */
    List<WaybillTaskRelevanceVO> findWaybillTaskRelevance(@Param("form") WaybillTaskRelevanceQueryForm form);

    /**
     * 根据id，查询任务
     * @param id
     * @return
     */
    WaybillTaskRelevanceVO findWaybillTaskRelevanceById(@Param("id") Long id);

    /**
     * 查询，完成任务后，激活 的 其他任务
     * @param paraMap
     * @return
     */
    List<WaybillTaskRelevance> findWaybillTaskRelevanceByParaMap(@Param("paraMap") Map<String, Object> paraMap);
}
