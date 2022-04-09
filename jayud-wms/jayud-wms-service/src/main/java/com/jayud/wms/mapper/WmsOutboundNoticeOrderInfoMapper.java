package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfo;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库通知单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-22
 */
@Mapper
public interface WmsOutboundNoticeOrderInfoMapper extends BaseMapper<WmsOutboundNoticeOrderInfo> {
    /**
    *   分页查询
    */
    IPage<WmsOutboundNoticeOrderInfoVO> pageList(@Param("page") Page<WmsOutboundNoticeOrderInfoVO> page, @Param("wmsOutboundNoticeOrderInfoVO") WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO);

    /**
    *   列表查询
    */
    List<WmsOutboundNoticeOrderInfoVO> list(@Param("wmsOutboundNoticeOrderInfoVO") WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    /**
     * @description 逻辑删除
     * @author  ciro
     * @date   2022/4/9 10:41
     * @param: id
     * @return: int
     **/
    int logicDelById(@Param("id") Long id,@Param("username") String username);
}
