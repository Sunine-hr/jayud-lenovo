package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.OrderInteriorStatusQueryForm;
import com.jayud.mall.model.po.OrderInteriorStatus;
import com.jayud.mall.model.vo.OrderInteriorStatusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 订单内部状态表(非流程状态) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-20
 */
@Mapper
@Component
public interface OrderInteriorStatusMapper extends BaseMapper<OrderInteriorStatus> {

    /**
     * 查询，订单内部状态
     * @param mapParm
     * @return
     */
    OrderInteriorStatusVO findOrderInteriorStatusByMapParm(@Param("mapParm") Map<String, Object> mapParm);

    /**
     * 查询订单内部状态
     * @param form
     * @return
     */
    OrderInteriorStatusVO findOrderInteriorStatusByOrderIdAndCode(@Param("form") OrderInteriorStatusQueryForm form);

    /**
     * 根据id,查询
     * @param id
     * @return
     */
    OrderInteriorStatusVO findOrderInteriorStatusById(@Param("id") Long id);
}
