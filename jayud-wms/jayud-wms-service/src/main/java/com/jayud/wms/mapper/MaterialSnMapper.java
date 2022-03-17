package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.MaterialSn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料sn信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-21
 */
@Mapper
public interface MaterialSnMapper extends BaseMapper<MaterialSn> {
    /**
    *   分页查询
    */
    IPage<MaterialSn> pageList(@Param("page") Page<MaterialSn> page, @Param("materialSn") MaterialSn materialSn);

    /**
    *   列表查询
    */
    List<MaterialSn> list(@Param("materialSn") MaterialSn materialSn);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryMaterialSnForExcel(Map<String, Object> paramMap);
}
