package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsPackingOffshelfOrder;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拣货下架单 服务类
 *
 * @author jyd
 * @since 2022-01-06
 */
public interface IWmsPackingOffshelfOrderService extends IService<WmsPackingOffshelfOrder> {

    /**
    *  分页查询
    * @param wmsPackingOffshelfOrder
    * @param req
    * @return
    */
    IPage<WmsPackingOffshelfOrder> selectPage(WmsPackingOffshelfOrder wmsPackingOffshelfOrder,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsPackingOffshelfOrder
    * @return
    */
    List<WmsPackingOffshelfOrder> selectList(WmsPackingOffshelfOrder wmsPackingOffshelfOrder);



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
    List<LinkedHashMap<String, Object>> queryWmsPackingOffshelfOrderForExcel(Map<String, Object> paramMap);

    /**
     * @description 保存下架单数据
     * @author  ciro
     * @date   2022/1/6 15:56
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult saveOrder(OutboundOrderNumberVO outboundOrderNumberVO);


}
