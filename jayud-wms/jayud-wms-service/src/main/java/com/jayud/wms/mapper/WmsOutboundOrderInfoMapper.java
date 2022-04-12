package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsOutboundOrderInfo;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 出库单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface WmsOutboundOrderInfoMapper extends BaseMapper<WmsOutboundOrderInfo> {
    /**
    *   分页查询
    */
    IPage<WmsOutboundOrderInfoVO> pageList(@Param("page") Page<WmsOutboundOrderInfoVO> page, @Param("wmsOutboundOrderInfoVO") WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
    *   列表查询
    */
    List<WmsOutboundOrderInfoVO> list(@Param("wmsOutboundOrderInfoVO") WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据条件查找未分配、可生成波次单得出库单
     * @author  ciro
     * @date   2021/12/23 14:15
     * @param: wmsOutboundOrderInfoVO
     * @return: java.util.List<com.jayud.model.vo.WmsOutboundOrderInfoVO>
     **/
    IPage<WmsOutboundOrderInfoVO> selectUnStockToWavePage(@Param("page") Page<WmsOutboundOrderInfoVO> page,@Param("wmsOutboundOrderInfoVO") WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * @description 根据出库单修改状态
     * @author  ciro
     * @date   2021/12/24 17:04
     * @param: orderNumber
     * @return: int
     **/
    int updateByOrderNumber(@Param("orderNumber") String orderNumber,@Param("waveNumber") String waveNumber,@Param("status") int status,@Param("username") String username);

    /**
     * @description 根据出库单号获取通知单号
     * @author  ciro
     * @date   2021/12/27 9:36
     * @param: orderNumber
     * @return: java.lang.String
     **/
    String getNoticOrderNumberByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * @description 出库单和波次单关联
     * @author  ciro
     * @date   2021/12/27 11:19
     * @param: waveOrderNumber
     * @param: orderNumberList
     * @return: int
     **/
    int createWaveRelation(@Param("waveOrderNumber") String waveOrderNumber,
                           @Param("orderNumberList") List<String> orderNumberList,
                           @Param("username") String username);

    /**
     * @description 删除出库单和波次关系
     * @author  ciro
     * @date   2022/1/4 10:55
     * @param: waveOrderNumber
     * @param: username
     * @return: int
     **/
    int delWaveRelation(@Param("waveOrderNumber") String waveOrderNumber,
                           @Param("username") String username);


    /**
     * @description
     * @author  ciro
     * @date   2022/1/4 15:19
     * @param: wmsOutboundOrderInfoVO
     * @return: java.util.List<com.jayud.model.vo.WmsOutboundOrderInfoVO>
     **/
    List<WmsOutboundOrderInfoVO> selectUnStockToWaveList(@Param("wmsOutboundOrderInfoVO") WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * @description 根据id逻辑删除
     * @author  ciro
     * @date   2022/4/6 17:50
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDelById(@Param("id") Long id,@Param("username") String username);

    /**
     * @description 根据时间获取完成数量
     * @author  ciro
     * @date   2022/4/12 13:59
     * @param: tenantCode
     * @param: yearAndMonth
     * @return: java.util.LinkedList<java.util.LinkedHashMap>
     **/
    LinkedList<LinkedHashMap> selectFinishCountByTime(@Param("tenantCode") String tenantCode, @Param("yearAndMonth") String yearAndMonth);

}
