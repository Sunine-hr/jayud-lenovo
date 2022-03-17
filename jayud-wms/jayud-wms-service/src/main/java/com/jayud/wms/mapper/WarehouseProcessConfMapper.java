package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WarehouseProcessConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库流程配置 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-14
 */
@Mapper
public interface WarehouseProcessConfMapper extends BaseMapper<WarehouseProcessConf> {
    /**
    *   分页查询
    */
    IPage<WarehouseProcessConf> pageList(@Param("page") Page<WarehouseProcessConf> page, @Param("warehouseProcessConf") WarehouseProcessConf warehouseProcessConf);

    /**
    *   列表查询
    */
    List<WarehouseProcessConf> list(@Param("warehouseProcessConf") WarehouseProcessConf warehouseProcessConf);
}
