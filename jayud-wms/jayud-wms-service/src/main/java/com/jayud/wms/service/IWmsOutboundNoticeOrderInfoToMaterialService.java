package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;
import com.jayud.common.result.BasePage;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库通知订单-物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IWmsOutboundNoticeOrderInfoToMaterialService extends IService<WmsOutboundNoticeOrderInfoToMaterial> {

    /**
    *  分页查询
    * @param wmsOutboundNoticeOrderInfoToMaterialVO
    * @param req
    * @return
    */
    IPage<WmsOutboundNoticeOrderInfoToMaterialVO> selectPage(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundNoticeOrderInfoToMaterialVO
    * @return
    */
    List<WmsOutboundNoticeOrderInfoToMaterialVO> selectList(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoToMaterialForExcel(Map<String, Object> paramMap);

    /**
     * @description 保存物料数据
     * @author  ciro
     * @date   2021/12/22 14:59
     * @param: wmsOutboundNoticeOrderInfoVO
     * @return: void
     **/
    BaseResult saveMaterial(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO);

    /**
     * @description 根据出库单编号删除
     * @author  ciro
     * @date   2021/12/22 17:38
     * @param: orderNumber
     * @return: void
     **/
    void delByOrderNumber(String orderNumber);

    /**
     * @description 获取库存物料信息
     * @author  ciro
     * @date   2022/3/30 16:01
     * @param: inventoryDetail
     * @return: java.util.List<com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO>
     **/
    List<WmsOutboundNoticeOrderInfoToMaterialVO> getInventoryMetailDetailList(InventoryDetail inventoryDetail);

    /**
     * @description 查询货品数据
     * @author  ciro
     * @date   2022/4/2 13:47
     * @param: material
     * @param: currentPage
     * @param: pageSize
     * @return: com.jayud.common.result.BasePage<com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO>
     **/
    BasePage<WmsOutboundNoticeOrderInfoToMaterialVO> selectInvenDetail(WmsOutboundNoticeOrderInfoToMaterialVO material,Integer currentPage,Integer pageSize);

    /**
     * @description 获取入仓号
     * @author  ciro
     * @date   2022/4/7 16:09
     * @param: orderNumber
     * @return: java.lang.String
     **/
    String getInWarehouseNumbers(String orderNumber);
}
