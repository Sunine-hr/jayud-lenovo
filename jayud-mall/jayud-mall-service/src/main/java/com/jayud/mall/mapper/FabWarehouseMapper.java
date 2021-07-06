package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.jayud.mall.model.vo.FabWarehouseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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
    IPage<FabWarehouseVO> findFabWarehouseByPage(Page<FabWarehouse> page, @Param("form") QueryFabWarehouseForm form);

    /**
     * 根据报价模板id，查询可达仓库，目的仓库
     * @param qie
     * @return
     */
    List<FabWarehouseVO> findFabWarehouseByqie(@Param("qie") Integer qie);

    /**
     * 根据仓库代码，获取目的地仓库（应收FBA仓库）
     * @param warehouseCode
     * @return
     */
    FabWarehouseVO findFabWarehouseByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    /**
     * 根据id，查询fba
     * @param id
     * @return
     */
    FabWarehouseVO findFabWarehouseById(@Param("id") Integer id);
}
