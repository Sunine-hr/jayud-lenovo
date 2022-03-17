package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryQualityInspectionForm;
import com.jayud.wms.model.po.QualityInspection;
import com.jayud.wms.model.vo.QualityInspectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 质检检测 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface QualityInspectionMapper extends BaseMapper<QualityInspection> {
    /**
    *   分页查询
    */
    IPage<QualityInspectionVO> pageList(@Param("page") Page<QueryQualityInspectionForm> page, @Param("qualityInspection") QueryQualityInspectionForm qualityInspection);

    /**
    *   列表查询
    */
    List<QualityInspection> list(@Param("qualityInspection") QualityInspection qualityInspection);

    /**
     * 查询导出
     * @param qualityInspection
     * @return
     */
    List<LinkedHashMap<String, Object>> queryQualityInspectionForExcel(@Param("qualityInspection") QueryQualityInspectionForm qualityInspection);
}
