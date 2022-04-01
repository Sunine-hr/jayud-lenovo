package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryQualityInspectionMaterialForm;
import com.jayud.wms.model.po.QualityInspectionMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 质检物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface QualityInspectionMaterialMapper extends BaseMapper<QualityInspectionMaterial> {
    /**
    *   分页查询
    */
    IPage<QualityInspectionMaterial> pageList(@Param("page") Page<QualityInspectionMaterial> page, @Param("qualityInspectionMaterial") QualityInspectionMaterial qualityInspectionMaterial);

    /**
    *   列表查询
    */
    List<QualityInspectionMaterial> list(@Param("qualityInspectionMaterial") QualityInspectionMaterial qualityInspectionMaterial);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryQualityInspectionMaterialForExcel(Map<String, Object> paramMap);

    /**
     *   校验查询此订单有没有该物料
     */
    QualityInspectionMaterial findQualityInspectionMaterialOne(@Param("qualityInspectionMaterial") QueryQualityInspectionMaterialForm qualityInspectionMaterial);

    /**
     * @description 根据质检单id集合删除
     * @author  ciro
     * @date   2022/4/1 11:43
     * @param: qualityIds
     * @param: username
     * @return: int
     **/
    int logicDelByQualityIds(@Param("qualityIds") List<Long> qualityIds,@Param("username") String username);

}
