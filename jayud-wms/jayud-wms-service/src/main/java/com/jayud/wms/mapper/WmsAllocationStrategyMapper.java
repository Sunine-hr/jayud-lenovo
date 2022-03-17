package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsAllocationStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配策略 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-17
 */
@Mapper
public interface WmsAllocationStrategyMapper extends BaseMapper<WmsAllocationStrategy> {
    /**
    *   分页查询
    */
    IPage<WmsAllocationStrategy> pageList(@Param("page") Page<WmsAllocationStrategy> page, @Param("wmsAllocationStrategy") WmsAllocationStrategy wmsAllocationStrategy);

    /**
    *   列表查询
    */
    List<WmsAllocationStrategy> list(@Param("wmsAllocationStrategy") WmsAllocationStrategy wmsAllocationStrategy);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsAllocationStrategyForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据id逻辑删除
     * @author  ciro
     * @date   2022/1/22 9:35
     * @param: id
     * @return: int
     **/
    int logicDel(@Param("id") long id,@Param("username") String username);
}
