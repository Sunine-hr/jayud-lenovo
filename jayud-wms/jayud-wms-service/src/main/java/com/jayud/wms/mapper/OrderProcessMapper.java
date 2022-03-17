package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.OrderProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单流程 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-14
 */
@Mapper
public interface OrderProcessMapper extends BaseMapper<OrderProcess> {
    /**
    *   分页查询
    */
    IPage<OrderProcess> pageList(@Param("page") Page<OrderProcess> page, @Param("orderProcess") OrderProcess orderProcess);

    /**
    *   列表查询
    */
    List<OrderProcess> list(@Param("orderProcess") OrderProcess orderProcess);
}
