package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryWarehouseForm;
import com.jayud.storage.model.po.Warehouse;
import com.jayud.storage.model.vo.WarehouseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仓库表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    List<WarehouseVO> findWarehouse(@Param("form") QueryWarehouseForm form);

    IPage<WarehouseVO> findWarehouseByPage(Page<WarehouseVO> page, @Param("form") QueryWarehouseForm form);

    WarehouseVO findWarehouseById(@Param("id") Long id);
}
