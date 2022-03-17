package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryWarehouseAreaForm;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.model.vo.WarehouseAreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库区信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-14
 */
@Mapper
public interface WarehouseAreaMapper extends BaseMapper<WarehouseArea> {
    /**
     *   分页查询
     */
    IPage<WarehouseAreaVO> pageList(@Param("page") Page<WarehouseAreaVO> page, @Param("warehouseArea") QueryWarehouseAreaForm warehouseArea);

    /**
     *   列表查询
     */
    List<WarehouseAreaVO> list(@Param("warehouseArea") WarehouseArea warehouseArea);

    List<LinkedHashMap<String, Object>> queryWarehouseAreaForExcel(Map<String, Object> paramMap);
}