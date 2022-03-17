package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToDistributionMaterial;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToDistributionMaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单-分配物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-24
 */
@Mapper
public interface WmsOutboundOrderInfoToDistributionMaterialMapper extends BaseMapper<WmsOutboundOrderInfoToDistributionMaterial> {
    /**
    *   分页查询
    */
    IPage<WmsOutboundOrderInfoToDistributionMaterial> pageList(@Param("page") Page<WmsOutboundOrderInfoToDistributionMaterial> page, @Param("wmsOutboundOrderInfoToDistributionMaterial") WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial);

    /**
    *   列表查询
    */
    List<WmsOutboundOrderInfoToDistributionMaterial> list(@Param("wmsOutboundOrderInfoToDistributionMaterial") WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToDistributionMaterialForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据出库单删除分配数据
     * @author  ciro
     * @date   2021/12/27 14:20
     * @param: orderNumber
     * @param: username
     * @return: int
     **/
    int delByOrderNumber(@Param("orderNumber") String orderNumber,@Param("username") String username);

    /**
     * @description 根据波次号删除分配数据
     * @author  ciro
     * @date   2021/12/27 14:21
     * @param: orderNumber
     * @param: username
     * @return: int
     **/
    int delByWaveNumber(@Param("waveNumber") String orderNumber,@Param("username") String username);

    /**
     * 根据物料信息，查询 分配物料信息
     * @param orderMaterialIds
     * @return
     */
    List<WmsOutboundOrderInfoToDistributionMaterialVO> selectListByOrderMaterialIds(@Param("orderMaterialIds") List<Long> orderMaterialIds);
}
