package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsOutboundOrderInfo;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IWmsOutboundOrderInfoService extends IService<WmsOutboundOrderInfo> {

    /**
    *  分页查询
    * @param wmsOutboundOrderInfoVO
    * @param req
    * @return
    */
    IPage<WmsOutboundOrderInfoVO> selectPage(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                           Integer currentPage,
                                           Integer pageSize,
                                           HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundOrderInfoVO
    * @return
    */
    List<WmsOutboundOrderInfoVO> selectList(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoForExcel(Map<String, Object> paramMap);

    /**
     * @description 转出库
     * @author  ciro
     * @date   2021/12/22 17:58
     * @param: wmsOutboundNoticeOrderInfoVO
     * @return: void
     **/
    BaseResult transferOut(OutboundOrderNumberVO outboundOrderNumberV);


    /**
     * @description 根据编码查询
     * @author  ciro
     * @date   2021/12/23 11:43
     * @param: wmsOutboundOrderInfoVO
     * @return: com.jayud.model.vo.WmsOutboundOrderInfoVO
     **/
    WmsOutboundOrderInfoVO queryByCode(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * @description 根据条件查找未分配、可生成波次单得出库单
     * @author  ciro
     * @date   2021/12/23 14:16
     * @param: wmsOutboundOrderInfoVO
     * @return: java.util.List<com.jayud.model.vo.WmsOutboundOrderInfoVO>
     **/
    IPage<WmsOutboundOrderInfoVO> selectUnStockToWave(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                                     Integer currentPage,
                                                     Integer pageSize);


    /**
     * @description 库存分配
     * @author  ciro
     * @date   2021/12/23 15:29
     * @param: outboundOrderNumberVO
     * @return: void
     **/
    BaseResult allocateInventory(OutboundOrderNumberVO outboundOrderNumberVO);



    /**
     * @description 根据编码查询库位信息
     * @author  ciro
     * @date   2021/12/24 17:15
     * @param: owerCode 货主编号
     * @param: warehouseCode    仓库编号
     * @param: materialCode     物料编号
     * @param: nowAccount       目前数量
     * @param: batchCode        批次编号
     * @param: materialProductionDate   生产日期
     * @param: customField1     字段1
     * @param: customField2     字段2
     * @param: customField3     字段3
     * @return: com.jyd.component.commons.result.Result<java.util.List<com.jayud.model.po.InventoryDetail>>
     **/
    BaseResult<List<InventoryDetail>> getWarehouseByCode(String owerCode,
                                                     String warehouseCode,
                                                     String materialCode,
                                                     BigDecimal nowAccount,
                                                     String batchCode,
                                                     LocalDateTime materialProductionDate,
                                                     String customField1,
                                                     String customField2,
                                                     String customField3);
    /**
     * @description 锁定库存
     * @author  ciro
     * @date   2021/12/24 14:22
     * @param: detailList
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult lockInventory(List<InventoryDetail> detailList);


    /**
     * @description 撤销库存分配
     * @author  ciro
     * @date   2021/12/24 16:57
     * @param: outboundOrderNumberVO
     * @return: Result
     **/
    BaseResult cancelAllocateInventory(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 是否转为出库单
     * @author  ciro
     * @date   2021/12/27 15:40
     * @param: noticeOrderNumber
     * @return: boolean
     **/
    boolean isChangeOrder(String noticeOrderNumber);

    boolean updateByOrderNumber(String orderNumber);

    WmsOutboundOrderInfo getWmsOutboundOrderInfoByOrderNumber(String orderNumber);

    /**
     * @description 集合删除
     * @author  ciro
     * @date   2021/12/28 11:43
     * @param: infoList
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult delByIdAndOrderNumber(List<WmsOutboundOrderInfoVO> infoList);

    /**
     * @description 判断是否生成拣货下架单
     * @author  ciro
     * @date   2021/12/31 15:49
     * @param: orderNumber
     * @param: isWave
     * @return: boolean
     **/
    boolean checkIsOffshelf(String orderNumber,boolean isWave);

    /**
     * @description 取消库存分配数量
     * @author  ciro
     * @date   2021/12/31 16:05
     * @param: orderNumber
     * @param: isWave
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult cancelOutputAllocation(String orderNumber,boolean isWave);

    /**
     * @description 查询可以生成波次订单列表
     * @author  ciro
     * @date   2022/1/4 15:18
     * @param: wmsOutboundOrderInfoVO
     * @return: java.util.List<com.jayud.model.vo.WmsOutboundOrderInfoVO>
     **/
    List<WmsOutboundOrderInfoVO> selectUnStockToWaveList(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * @description 完成出库单
     * @author  ciro
     * @date   2022/1/26 10:03
     * @param: orderNumberList
     * @return: void
     **/
    void finishOrder(List<String> orderNumberList);

    /**
     * @description 保存信息
     * @author  ciro
     * @date   2022/4/6 16:24
     * @param: wmsOutboundOrderInfoVO
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult saveInfo(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * @description 删除订单
     * @author  ciro
     * @date   2022/4/6 17:18
     * @param: deleteForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult delOrder(DeleteForm deleteForm);

}
