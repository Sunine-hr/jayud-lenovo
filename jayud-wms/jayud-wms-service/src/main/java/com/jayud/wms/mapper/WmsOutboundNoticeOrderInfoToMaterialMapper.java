package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库通知订单-物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface WmsOutboundNoticeOrderInfoToMaterialMapper extends BaseMapper<WmsOutboundNoticeOrderInfoToMaterial> {
    /**
    *   分页查询
    */
    IPage<WmsOutboundNoticeOrderInfoToMaterialVO> pageList(@Param("page") Page<WmsOutboundNoticeOrderInfoToMaterialVO> page, @Param("wmsOutboundNoticeOrderInfoToMaterialVO") WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO);

    /**
    *   列表查询
    */
    List<WmsOutboundNoticeOrderInfoToMaterialVO> list(@Param("wmsOutboundNoticeOrderInfoToMaterialVO") WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoToMaterialForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 根据通知单编号、物料编码逻辑删除
     * @author  ciro
     * @date   2021/12/22 15:39
     * @param: orderNumber  通知单编号
     * @param: materialCodeList 物料编码集合
     * @param: username 用户名称
     * @return: int
     **/
    int delteByOrderNumberAndMaterialCode(@Param("orderNumber") String orderNumber,
                                          @Param("materialCodeList") List<String> materialCodeList,
                                          @Param("idList") List<String> idList,
                                          @Param("username") String username);
}
