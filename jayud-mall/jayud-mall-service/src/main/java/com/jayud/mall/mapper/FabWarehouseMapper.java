package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 应收/FBA仓库 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface FabWarehouseMapper extends BaseMapper<FabWarehouse> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<FabWarehouse> findFabWarehouseByPage(Page<FabWarehouse> page, QueryFabWarehouseForm form);
}
