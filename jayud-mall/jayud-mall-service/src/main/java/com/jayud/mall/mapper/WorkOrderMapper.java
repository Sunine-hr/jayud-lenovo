package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryWorkOrderForm;
import com.jayud.mall.model.po.WorkOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.WorkOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单工单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-26
 */
@Mapper
@Component
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {

    /**
     * 订单工单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<WorkOrderVO> findWorkOrderByPage(Page<WorkOrderVO> page, @Param("form") QueryWorkOrderForm form);

    /**
     * 根据id，查询工单
     * @param id
     * @return
     */
    WorkOrderVO findWorkOrderById(@Param("id") Long id);
}
