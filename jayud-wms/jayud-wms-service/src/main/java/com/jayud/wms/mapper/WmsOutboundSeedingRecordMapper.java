package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsOutboundSeedingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库播种记录 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-24
 */
@Mapper
public interface WmsOutboundSeedingRecordMapper extends BaseMapper<WmsOutboundSeedingRecord> {
    /**
    *   分页查询
    */
    IPage<WmsOutboundSeedingRecord> pageList(@Param("page") Page<WmsOutboundSeedingRecord> page, @Param("wmsOutboundSeedingRecord") WmsOutboundSeedingRecord wmsOutboundSeedingRecord);

    /**
    *   列表查询
    */
    List<WmsOutboundSeedingRecord> list(@Param("wmsOutboundSeedingRecord") WmsOutboundSeedingRecord wmsOutboundSeedingRecord);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsOutboundSeedingRecordForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);
}
