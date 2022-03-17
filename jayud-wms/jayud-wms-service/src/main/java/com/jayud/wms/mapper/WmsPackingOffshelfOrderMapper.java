package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsPackingOffshelfOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拣货下架单 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-06
 */
@Mapper
public interface WmsPackingOffshelfOrderMapper extends BaseMapper<WmsPackingOffshelfOrder> {
    /**
    *   分页查询
    */
    IPage<WmsPackingOffshelfOrder> pageList(@Param("page") Page<WmsPackingOffshelfOrder> page, @Param("wmsPackingOffshelfOrder") WmsPackingOffshelfOrder wmsPackingOffshelfOrder);

    /**
    *   列表查询
    */
    List<WmsPackingOffshelfOrder> list(@Param("wmsPackingOffshelfOrder") WmsPackingOffshelfOrder wmsPackingOffshelfOrder);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsPackingOffshelfOrderForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);
}
