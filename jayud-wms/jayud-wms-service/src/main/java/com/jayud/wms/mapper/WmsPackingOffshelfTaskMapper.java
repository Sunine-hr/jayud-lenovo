package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.dto.OutboundToPacking.WmsOutboundToPackingDTO;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import com.jayud.wms.model.vo.DeliveryReportVO;
import com.jayud.wms.model.vo.WmsPackingOffshelfVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拣货下架任务 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-24
 */
@Mapper
public interface WmsPackingOffshelfTaskMapper extends BaseMapper<WmsPackingOffshelfTask> {
    /**
    *   分页查询
    */
    IPage<WmsPackingOffshelfTask> pageList(@Param("page") Page<WmsPackingOffshelfTask> page, @Param("wmsPackingOffshelfTask") WmsPackingOffshelfTask wmsPackingOffshelfTask);

    /**
    *   列表查询
    */
    List<WmsPackingOffshelfTask> list(@Param("wmsPackingOffshelfTask") WmsPackingOffshelfTask wmsPackingOffshelfTask);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsPackingOffshelfTaskForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据编号删除
     * @author  ciro
     * @date   2021/12/27 15:15
     * @param: orderNumber
     * @param: waveNumber
     * @return: int
     **/
    int delByNumber(@Param("orderNumber") String orderNumber,@Param("waveNumber") String waveNumber,@Param("detailNumber") String detailNumber,@Param("username") String username);

    /**
     * @description 获取下架任务
     * @author  ciro
     * @date   2022/1/6 10:11
     * @param: wmsPackingOffshelfVO
     * @return: java.util.List<com.jayud.model.vo.WmsPackingOffshelfVO>
     **/
    List<WmsPackingOffshelfVO> getPackingTask(@Param("wmsPackingOffshelfVO") WmsPackingOffshelfVO wmsPackingOffshelfVO);

    /**
     * 出库报表
     * @param queryShelfOrderTaskForm
     * @return
     */
    List<DeliveryReportVO> selectDeliveryReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm);

    /**
     * @description 查询出库单到拣货下架数据
     * @author  ciro
     * @date   2022/3/10 17:42
     * @param: orderNumber
     * @return: java.util.List<com.jayud.model.dto..WmsOutboundToPackingDTO>
     **/
    List<WmsOutboundToPackingDTO> selectOutboundToPackingMsg(@Param("orderNumber") String orderNumber);
}
