package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsWaveOrderInfo;
import com.jayud.wms.model.vo.WmsWaveInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-24
 */
@Mapper
public interface WmsWaveOrderInfoMapper extends BaseMapper<WmsWaveOrderInfo> {
    /**
    *   分页查询
    */
    IPage<WmsWaveOrderInfo> pageList(@Param("page") Page<WmsWaveOrderInfo> page, @Param("wmsWaveOrderInfo") WmsWaveOrderInfo wmsWaveOrderInfo);

    /**
    *   列表查询
    */
    List<WmsWaveOrderInfo> list(@Param("wmsWaveOrderInfo") WmsWaveOrderInfo wmsWaveOrderInfo);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsWaveOrderInfoForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 更新波次单状态
     * @author  ciro
     * @date   2021/12/27 15:33
     * @param: waveOrderNumber
     * @param: statusType
     * @param: username
     * @return: int
     **/
    int updateByWaveNumber(@Param("waveOrderNumber") String waveOrderNumber,
                           @Param("statusType") int statusType,
                        @Param("username") String username);

    /**
     * @description 根据条件逻辑删除
     * @author  ciro
     * @date   2021/12/31 11:26
     * @param: wmsWaveInfoVO
     * @param: username
     * @return: int
     **/
    int delByParam(@Param("wmsWaveInfoVO") WmsWaveInfoVO wmsWaveInfoVO,
                           @Param("username") String username);

    /**
     * @description 物理删除关系表数据
     * @author  ciro
     * @date   2022/1/4 15:37
     * @param: waveNumber
     * @return: int
     **/
    int delMiddel(@Param("waveNumber") String waveNumber);

    /**
     * @description 插入关系表数据
     * @author  ciro
     * @date   2022/1/4 15:39
     * @param: waveNumber
     * @param: orderNumberlist
     * @return: int
     **/
    int insertMiddel(@Param("waveNumber") String waveNumber,@Param("orderNumberlist") List<String> orderNumberlist);
}
