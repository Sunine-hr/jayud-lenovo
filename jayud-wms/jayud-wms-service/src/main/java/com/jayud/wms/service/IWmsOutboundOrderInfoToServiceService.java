package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToService;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单-附加服务 服务类
 *
 * @author jayud
 * @since 2022-04-06
 */
public interface IWmsOutboundOrderInfoToServiceService extends IService<WmsOutboundOrderInfoToService> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-06
     * @param: wmsOutboundOrderInfoToService
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>
     **/
    IPage<WmsOutboundOrderInfoToService> selectPage(WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-06
     * @param: wmsOutboundOrderInfoToService
     * @param: req
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundOrderInfoToService>
     **/
    List<WmsOutboundOrderInfoToService> selectList(WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-06
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-04-06
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-06
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToServiceForExcel(Map<String, Object> paramMap);

    /**
     * @description 保存服务数据
     * @author  ciro
     * @date   2022/4/6 16:11
     * @param: orderNumber
     * @param: serviceList
     * @return: void
     **/
    void saveService(String orderNumber,List<WmsOutboundOrderInfoToService> serviceList);

}
