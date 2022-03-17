package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryWarehouseForm;
import com.jayud.wms.model.po.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息表 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-14
 */
@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {
    /**
     *   分页查询
     */
    IPage<Warehouse> pageList(@Param("page") Page<Warehouse> page, @Param("warehouse") QueryWarehouseForm warehouse);

    /**
     *   列表查询
     * @param warehouse
     */
    List<Warehouse> list(@Param("warehouse") QueryWarehouseForm warehouse);

    List<LinkedHashMap<String, Object>> queryWarehouseForExcel(Map<String, Object> paramMap);

    /**
     * @description 根据货主id查询关联仓库信息
     * @author  ciro
     * @date   2022/2/9 17:38
     * @param: owerId
     * @return: com.jayud.model.po.Warehouse
     **/
    List<Warehouse> queryWarehouseByOwerId(@Param("owerId") Long owerId);
}
