package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.FabWarehouseArgsForm;
import com.jayud.mall.model.bo.FabWarehouseForm;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.AuditFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.jayud.mall.model.vo.FabWarehouseVO;

import java.util.List;

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
    IPage<FabWarehouseVO> findFabWarehouseByPage(QueryFabWarehouseForm form);

    /**
     * 查询仓库list
     * @param form
     * @return
     */
    List<FabWarehouseVO> findfabWarehouse(FabWarehouseArgsForm form);

    /**
     * 保存应收仓库
     * @param form
     * @return
     */
    CommonResult saveFabWarehouse(FabWarehouseForm form);

    /**
     * 删除应收仓库
     * @param id
     * @return
     */
    CommonResult deleteFabWarehouse(Integer id);

    /**
     * 审核应收仓库
     * @param form
     * @return
     */
    CommonResult auditFabWarehouse(AuditFabWarehouseForm form);

    /**
     * 根据代码，查询仓库
     * @param warehouseCode
     * @return
     */
    FabWarehouseVO findfabWarehouseByWarehouseCode(String warehouseCode);
}
