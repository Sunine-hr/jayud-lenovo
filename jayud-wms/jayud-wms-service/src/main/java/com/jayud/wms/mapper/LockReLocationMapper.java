package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.LockReLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐库位锁定 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-17
 */
@Mapper
public interface LockReLocationMapper extends BaseMapper<LockReLocation> {
    /**
    *   分页查询
    */
    IPage<LockReLocation> pageList(@Param("page") Page<LockReLocation> page, @Param("lockReLocation") LockReLocation lockReLocation);

    /**
    *   列表查询
    */
    List<LockReLocation> list(@Param("lockReLocation") LockReLocation lockReLocation);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryLockReLocationForExcel(Map<String, Object> paramMap);
}
