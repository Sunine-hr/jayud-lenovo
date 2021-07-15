package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.LocationCodeVO;
import com.jayud.storage.model.vo.WarehouseAreaShelvesLocationVO;
import com.jayud.storage.model.vo.WarehouseNameVO;

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

    /**
     * 根据货架名称获取库位信息
     * @param shelvesName
     * @return
     */
    List<LocationCodeVO> getListByShelvesName(String shelvesName);

    /**
     * 根据库位编码获取仓库名称
     * @param kuCode
     * @return
     */
    WarehouseNameVO getWarehouseNameByKuCode(String kuCode);

    WarehouseAreaShelvesLocation getLocation(Integer shelvesLine, Long shelvesId, Long shelvesType);

    List<WarehouseAreaShelvesLocation> getLocationByShelvesLine(Integer shelvesLine, Long shelvesId);
}
