package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.AddWmsOutboundSeedingForm;
import com.jayud.wms.model.po.WmsOutboundSeeding;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库播种 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsOutboundSeedingService extends IService<WmsOutboundSeeding> {

    /**
    *  分页查询
    * @param wmsOutboundSeeding
    * @param req
    * @return
    */
    IPage<WmsOutboundSeeding> selectPage(WmsOutboundSeeding wmsOutboundSeeding,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundSeeding
    * @return
    */
    List<WmsOutboundSeeding> selectList(WmsOutboundSeeding wmsOutboundSeeding);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundSeedingForExcel(Map<String, Object> paramMap);


    boolean saveOrUpdateWmsOutboundSeeding(List<AddWmsOutboundSeedingForm> form);
    /**
     * @description 生成播种数据
     * @author  ciro
     * @date   2021/12/25 11:02
     * @param: taskList
     * @return: void
     **/
    void createPackingToSeeding(List<WmsPackingOffshelfTask> taskList);
}
