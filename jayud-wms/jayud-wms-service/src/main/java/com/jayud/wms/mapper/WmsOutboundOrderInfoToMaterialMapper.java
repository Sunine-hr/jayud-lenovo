package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToMaterial;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单-物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface WmsOutboundOrderInfoToMaterialMapper extends BaseMapper<WmsOutboundOrderInfoToMaterial> {
    /**
    *   分页查询
    */
    IPage<WmsOutboundOrderInfoToMaterialVO> pageList(@Param("page") Page<WmsOutboundOrderInfoToMaterialVO> page, @Param("wmsOutboundOrderInfoToMaterialVO") WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO);

    /**
    *   列表查询
    */
    List<WmsOutboundOrderInfoToMaterialVO> list(@Param("wmsOutboundOrderInfoToMaterialVO") WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToMaterialForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据出库单修改状态
     * @author  ciro
     * @date   2021/12/24 17:04
     * @param: orderNumber
     * @return: int
     **/
    int updateByOrderNumber(@Param("orderNumber") String orderNumber,@Param("status") int status,@Param("username") String username);

    /**
     * @description 根据波次号修改状态
     * @author  ciro
     * @date   2021/12/27 14:18
     * @param: waveNumber
     * @param: status
     * @param: username
     * @return: int
     **/
    int updateByWaveNumber(@Param("waveNumber") String waveNumber,@Param("status") int status,@Param("username") String username);
    int updateByOrderNumber(@Param("orderNumber") String orderNumber,@Param("status") int status);

    List<QueryScanInformationVO> queryScanInformation(@Param("orderNumber") String orderNumber, @Param("materialCode") String materialCode);

    /**
     * @description 根据出库单号删除
     * @author  ciro
     * @date   2021/12/28 11:38
     * @param: orderNumber
     * @param: username
     * @return: int
     **/
    int delByOrderNumber(@Param("orderNumber") String orderNumber,@Param("username") String username);
}
