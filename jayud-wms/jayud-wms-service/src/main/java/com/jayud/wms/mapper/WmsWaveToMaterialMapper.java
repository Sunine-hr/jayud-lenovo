package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsWaveToMaterial;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次单-物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-27
 */
@Mapper
public interface WmsWaveToMaterialMapper extends BaseMapper<WmsWaveToMaterial> {
    /**
    *   分页查询
    */
    IPage<WmsWaveToMaterial> pageList(@Param("page") Page<WmsWaveToMaterial> page, @Param("wmsWaveToMaterial") WmsWaveToMaterial wmsWaveToMaterial);

    /**
    *   列表查询
    */
    List<WmsWaveToMaterial> list(@Param("wmsWaveToMaterial") WmsWaveToMaterial wmsWaveToMaterial);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsWaveToMaterialForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据波次单删除物料
     * @author  ciro
     * @date   2021/12/27 14:50
     * @param: waveOrderNumber
     * @param: username
     * @return: int
     **/
    int delByWaveNumber(@Param("waveOrderNumber") String waveOrderNumber,
                        @Param("username") String username);

    List<QueryScanInformationVO> queryScanInformation(@Param("orderNumber") String orderNumber, @Param("materialCode") String materialCode);

    /**
     * @description 根据波次跟新状态
     * @author  ciro
     * @date   2021/12/31 15:58
     * @param: waveOrderNumber
     * @param: status
     * @param: username
     * @return: int
     **/
    int updateByWaveNumber(@Param("waveOrderNumber") String waveOrderNumber,
                           @Param("status") int status,
                        @Param("username") String username);
}
