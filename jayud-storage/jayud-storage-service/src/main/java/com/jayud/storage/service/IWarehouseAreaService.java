package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.OperationForm;
import com.jayud.storage.model.bo.OperationWarehouseForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaForm;
import com.jayud.storage.model.bo.WarehouseAreaForm;
import com.jayud.storage.model.po.WarehouseArea;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.StorageInputOrderFormVO;
import com.jayud.storage.model.vo.WarehouseAreaVO;

import java.util.List;

/**
 * <p>
 * 仓库区域表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
public interface IWarehouseAreaService extends IService<WarehouseArea> {

    boolean saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseAreaForm);

    List<WarehouseAreaVO> getList(QueryWarehouseAreaForm form);

    IPage<WarehouseAreaVO> findWarehouseAreaByPage(QueryWarehouseAreaForm form);

    void operationWarehouseArea(OperationForm form);

    WarehouseArea getWarehouseAreaByAreaCode(String areaCode);

    WarehouseArea getWarehouseAreaByAreaName(String areaName);
}
