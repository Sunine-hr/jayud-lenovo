package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.Workbench;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-17
 */
@Mapper
public interface WorkbenchMapper extends BaseMapper<Workbench> {
    /**
    *   分页查询
    */
    IPage<Workbench> pageList(@Param("page") Page<Workbench> page, @Param("workbench") Workbench workbench);

    /**
    *   列表查询
    */
    List<Workbench> list(@Param("workbench") Workbench workbench);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWorkbenchForExcel(Map<String, Object> paramMap);
}
