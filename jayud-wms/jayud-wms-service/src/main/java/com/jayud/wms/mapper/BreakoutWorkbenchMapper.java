package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.BreakoutWorkbench;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台类型信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-17
 */
@Mapper
public interface BreakoutWorkbenchMapper extends BaseMapper<BreakoutWorkbench> {
    /**
    *   分页查询
    */
    IPage<BreakoutWorkbench> pageList(@Param("page") Page<BreakoutWorkbench> page, @Param("breakoutWorkbench") BreakoutWorkbench breakoutWorkbench);

    /**
    *   列表查询
    */
    List<BreakoutWorkbench> list(@Param("breakoutWorkbench") BreakoutWorkbench breakoutWorkbench);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryBreakoutWorkbenchForExcel(Map<String, Object> paramMap);
}
