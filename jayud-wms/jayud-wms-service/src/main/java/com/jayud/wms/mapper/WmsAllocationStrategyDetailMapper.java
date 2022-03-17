package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsAllocationStrategyDetail;
import com.jayud.wms.model.vo.WmsAllocationStrategyDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配策略详情 Mapper 接口
 *
 * @author jyd
 * @since 2022-01-17
 */
@Mapper
public interface WmsAllocationStrategyDetailMapper extends BaseMapper<WmsAllocationStrategyDetail> {
    /**
    *   分页查询
    */
    IPage<WmsAllocationStrategyDetailVO> pageList(@Param("page") Page<WmsAllocationStrategyDetailVO> page, @Param("wmsAllocationStrategyDetailVO") WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO);

    /**
    *   列表查询
    */
    List<WmsAllocationStrategyDetailVO> list(@Param("wmsAllocationStrategyDetailVO") WmsAllocationStrategyDetailVO wmsAllocationStrategyDetailVO);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsAllocationStrategyDetailForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据策略id删除
     * @author  ciro
     * @date   2022/1/20 11:15
     * @param: strategyId
     * @param: username
     * @return: int
     **/
    int delByStrategyId(@Param("strategyId") long strategyId,@Param("username") String username);
}
