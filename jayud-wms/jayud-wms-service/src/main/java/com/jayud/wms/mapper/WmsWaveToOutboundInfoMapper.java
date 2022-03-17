package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsWaveToOutboundInfo;
import com.jayud.wms.model.vo.WmsWaveInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次-出库单关系 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-27
 */
@Mapper
public interface WmsWaveToOutboundInfoMapper extends BaseMapper<WmsWaveToOutboundInfo> {
    /**
    *   分页查询
    */
    IPage<WmsWaveToOutboundInfo> pageList(@Param("page") Page<WmsWaveToOutboundInfo> page, @Param("wmsWaveToOutboundInfo") WmsWaveToOutboundInfo wmsWaveToOutboundInfo);

    /**
    *   列表查询
    */
    List<WmsWaveToOutboundInfo> list(@Param("wmsWaveToOutboundInfo") WmsWaveToOutboundInfo wmsWaveToOutboundInfo);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsWaveToOutboundInfoForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据波次单删除出库单关联
     * @author  ciro
     * @date   2021/12/27 14:50
     * @param: waveOrderNumber
     * @param: username
     * @return: int
     **/
    int delByWaveNumber(@Param("waveOrderNumber") String waveOrderNumber,
                        @Param("username") String username);

    /**
     * @description 根据条件逻辑删除
     * @author  ciro
     * @date   2021/12/31 11:31
     * @param: wmsWaveInfoVO
     * @param: username
     * @return: int
     **/
    int delByParam(@Param("wmsWaveInfoVO") WmsWaveInfoVO wmsWaveInfoVO,
                        @Param("username") String username);
}
