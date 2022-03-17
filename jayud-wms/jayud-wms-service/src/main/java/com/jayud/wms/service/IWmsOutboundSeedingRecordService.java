package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsOutboundSeedingRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库播种记录 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsOutboundSeedingRecordService extends IService<WmsOutboundSeedingRecord> {

    /**
    *  分页查询
    * @param wmsOutboundSeedingRecord
    * @param req
    * @return
    */
    IPage<WmsOutboundSeedingRecord> selectPage(WmsOutboundSeedingRecord wmsOutboundSeedingRecord,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundSeedingRecord
    * @return
    */
    List<WmsOutboundSeedingRecord> selectList(WmsOutboundSeedingRecord wmsOutboundSeedingRecord);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundSeedingRecordForExcel(Map<String, Object> paramMap);


}
