package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.OperationWarehouseForm;
import com.jayud.storage.model.bo.QueryWarehouseForm;
import com.jayud.storage.model.bo.SaveWarehouseForm;
import com.jayud.storage.model.po.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.WarehouseVO;

import java.util.List;

/**
 * <p>
 * 仓库表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
public interface IWarehouseService extends IService<Warehouse> {

    List<WarehouseVO> findWarehouse(QueryWarehouseForm form);

    IPage<WarehouseVO> findWarehouseByPage(QueryWarehouseForm form);

    void operationWarehouse(OperationWarehouseForm form);

    void saveWarehouse(SaveWarehouseForm form);

    WarehouseVO findWarehouseById(Long id);
}
