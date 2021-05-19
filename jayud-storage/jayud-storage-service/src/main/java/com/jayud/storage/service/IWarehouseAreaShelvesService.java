package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.OperationForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelves2Form;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesForm;
import com.jayud.storage.model.po.WarehouseAreaShelves;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.WarehouseAreaShelvesFormVO;
import com.jayud.storage.model.vo.WarehouseAreaShelvesVO;

/**
 * <p>
 * 商品区域货架表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
public interface IWarehouseAreaShelvesService extends IService<WarehouseAreaShelves> {

    boolean saveOrUpdateWarehouseAreaShelves(WarehouseAreaShelvesForm form);

    void operationWarehouseAreaShelves(OperationForm form);

    IPage<WarehouseAreaShelvesVO> findWarehouseAreaShelvesByPage(QueryWarehouseAreaShelvesForm form);

    IPage<WarehouseAreaShelvesFormVO> findWarehouseAreaShelvesLocationByPage(QueryWarehouseAreaShelves2Form form);

    WarehouseAreaShelves getWarehouseAreaShelvesByShelvesName(String name);

    WarehouseAreaShelvesFormVO getWarehouseAreaShelvesByShelvesId(Long shelvesId);
}
