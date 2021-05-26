package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.LocationCodeVO;
import com.jayud.storage.model.vo.WarehouseAreaShelvesLocationVO;

import java.util.List;

/**
 * <p>
 * 仓库区域货架库位表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
public interface IWarehouseAreaShelvesLocationService extends IService<WarehouseAreaShelvesLocation> {

    List<WarehouseAreaShelvesLocation> getUpdateTime();

    IPage<WarehouseAreaShelvesLocationVO> findWarehouseAreaShelvesLocationByPage(QueryWarehouseAreaShelvesLocationForm form);

    boolean saveOrUpdateWarehouseAreaShelvesLocation(List<WarehouseAreaShelvesLocationForm> form);

    List<WarehouseAreaShelvesLocationVO> getListByShelvesId(QueryWarehouseAreaShelvesLocationForm form);

    List<LocationCodeVO> getList();

}
