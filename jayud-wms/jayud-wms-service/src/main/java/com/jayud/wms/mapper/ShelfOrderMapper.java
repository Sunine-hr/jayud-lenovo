package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryShelfOrderForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.ShelfOrder;
import com.jayud.wms.model.vo.ShelfOrderVO;
import com.jayud.wms.model.vo.WarehouseingReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 上架单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-23
 */
@Mapper
public interface ShelfOrderMapper extends BaseMapper<ShelfOrder> {
    /**
    *   分页查询
    */
    IPage<ShelfOrderVO> pageList(@Param("page") Page<QueryShelfOrderForm> page, @Param("shelfOrder") QueryShelfOrderForm shelfOrder);

    /**
    *   列表查询
    */
    List<ShelfOrderVO> list(@Param("shelfOrder") QueryShelfOrderForm shelfOrder);

    /**
     * 查询导出
     * @param shelfOrder
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfOrderForExcel(@Param("shelfOrder") QueryShelfOrderForm shelfOrder);

    /**
     *
     * @param queryShelfOrderTaskForm
     * @return
     */
    List<WarehouseingReportVO> selectWarehousingReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm);

}
