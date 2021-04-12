package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.po.Shipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.ShipmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 南京新智慧-运单装货信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
@Mapper
@Component
public interface ShipmentMapper extends BaseMapper<Shipment> {

    /**
     * 分页查询南京新智慧订单装货信息
     * @param page
     * @param form
     * @return
     */
    IPage<ShipmentVO> findShipmentByPage(Page<ShipmentVO> page, @Param("form") QueryShipmentForm form);

    /**
     * 根据运单号，查询运单装箱
     * @param shipment_id
     * @return
     */
    ShipmentVO findfindShipmentById(@Param("shipment_id") String shipment_id);
}
