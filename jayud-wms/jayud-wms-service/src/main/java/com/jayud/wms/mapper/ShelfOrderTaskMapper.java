package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.ShelfOrderTask;
import com.jayud.wms.model.vo.ShelfOrderTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 上架任务单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-23
 */
@Mapper
public interface ShelfOrderTaskMapper extends BaseMapper<ShelfOrderTask> {
    /**
    *   分页查询
    */
    IPage<ShelfOrderTaskVO> pageList(@Param("page") Page<QueryShelfOrderTaskForm> page, @Param("shelfOrderTask") QueryShelfOrderTaskForm shelfOrderTask);

    /**
    *   列表查询
    */
    List<ShelfOrderTaskVO> list(@Param("shelfOrderTask") QueryShelfOrderTaskForm shelfOrderTask);

    /**
     *   列表查询 过滤掉强制上架
     */
    List<ShelfOrderTaskVO> listFeign(@Param("shelfOrderTask") QueryShelfOrderTaskForm shelfOrderTask);
    /**
     * 查询导出
     * @param shelfOrderTask
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfOrderTaskForExcel(@Param("shelfOrderTask") QueryShelfOrderTaskForm shelfOrderTask);
}
