package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsWaveToOutboundInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次-出库单关系 服务类
 *
 * @author jyd
 * @since 2021-12-27
 */
public interface IWmsWaveToOutboundInfoService extends IService<WmsWaveToOutboundInfo> {

    /**
    *  分页查询
    * @param wmsWaveToOutboundInfo
    * @param req
    * @return
    */
    IPage<WmsWaveToOutboundInfo> selectPage(WmsWaveToOutboundInfo wmsWaveToOutboundInfo,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsWaveToOutboundInfo
    * @return
    */
    List<WmsWaveToOutboundInfo> selectList(WmsWaveToOutboundInfo wmsWaveToOutboundInfo);



    /**
     * 物理删除
     * @param id
     */
    void phyDelById(int id);


    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsWaveToOutboundInfoForExcel(Map<String, Object> paramMap);


}
