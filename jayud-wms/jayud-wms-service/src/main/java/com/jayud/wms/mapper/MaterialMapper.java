package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QualityMaterialForm;
import com.jayud.wms.model.po.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 货单物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-21
 */
@Mapper
public interface MaterialMapper extends BaseMapper<Material> {
    /**
    *   分页查询
    */
    IPage<Material> pageList(@Param("page") Page<Material> page, @Param("material") Material material);

    /**
    *   列表查询
    */
    List<Material> list(@Param("material") Material material);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryMaterialForExcel(Map<String, Object> paramMap);

    /**
     *   根据条件查询 收货单 绑定 物料信息
     */
    List<Material> findMaterialOne(@Param("material") Material material);

    /**
     * 根据序列编号和收货单号 查询物料信息
     */
    List<Material> findMaterialSNOne(@Param("material") QualityMaterialForm material);


   int  updateAllMaterialList(@Param("material") Material material);
}
