package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 应收/FBA仓库 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IFabWarehouseService extends IService<FabWarehouse> {

    /**
     * 分页查询应收仓库
     * @param form
     * @return
     */
    IPage<FabWarehouse> findFabWarehouseByPage(QueryFabWarehouseForm form);
}
