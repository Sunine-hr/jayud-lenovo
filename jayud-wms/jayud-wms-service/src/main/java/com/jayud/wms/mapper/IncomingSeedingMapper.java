package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.IncomingSeeding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库播种 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-23
 */
@Mapper
public interface IncomingSeedingMapper extends BaseMapper<IncomingSeeding> {
    /**
    *   分页查询
    */
    IPage<IncomingSeeding> pageList(@Param("page") Page<IncomingSeeding> page, @Param("incomingSeeding") IncomingSeeding incomingSeeding);

    /**
    *   列表查询
    */
    List<IncomingSeeding> list(@Param("incomingSeeding") IncomingSeeding incomingSeeding);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryIncomingSeedingForExcel(Map<String, Object> paramMap);
}
