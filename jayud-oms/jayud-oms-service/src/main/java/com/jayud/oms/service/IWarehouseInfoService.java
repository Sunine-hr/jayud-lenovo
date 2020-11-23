package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryWarehouseInfoForm;
import com.jayud.oms.model.po.WarehouseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.WarehouseInfoVO;

/**
 * <p>
 * 仓库信息表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
public interface IWarehouseInfoService extends IService<WarehouseInfo> {

    IPage<WarehouseInfoVO> findWarehouseInfoByPage(QueryWarehouseInfoForm form);

    boolean checkUnique(WarehouseInfo warehouseInfo);

    boolean saveOrUpdateWarehouseInfo(WarehouseInfo warehouseInfo);

    boolean enableOrDisableWarehouse(Long id);
}
