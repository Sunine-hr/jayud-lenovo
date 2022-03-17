package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.ShelfMoveTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 货架移动任务 Mapper 接口
 *
 * @author jyd
 * @since 2022-03-05
 */
@Mapper
public interface ShelfMoveTaskMapper extends BaseMapper<ShelfMoveTask> {
    /**
    *   分页查询
    */
    IPage<ShelfMoveTask> pageList(@Param("page") Page<ShelfMoveTask> page, @Param("shelfMoveTask") ShelfMoveTask shelfMoveTask);

    /**
    *   列表查询
    */
    List<ShelfMoveTask> list(@Param("shelfMoveTask") ShelfMoveTask shelfMoveTask);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfMoveTaskForExcel(Map<String, Object> paramMap);
}
