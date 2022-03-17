package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QuerySowingResultsForm;
import com.jayud.wms.model.po.SowingResults;
import com.jayud.wms.model.vo.SowingResultsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 播种结果 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-23
 */
@Mapper
public interface SowingResultsMapper extends BaseMapper<SowingResults> {
    /**
    *   分页查询
    */
    IPage<SowingResultsVO> pageList(@Param("page") Page<QuerySowingResultsForm> page, @Param("sowingResults") QuerySowingResultsForm sowingResults);

    /**
    *   列表查询
    */
    List<SowingResultsVO> list(@Param("sowingResults") QuerySowingResultsForm sowingResults);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> querySowingResultsForExcel(Map<String, Object> paramMap);
}
