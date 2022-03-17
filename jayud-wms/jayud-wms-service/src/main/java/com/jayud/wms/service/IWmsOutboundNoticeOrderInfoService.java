package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfo;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库通知单 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IWmsOutboundNoticeOrderInfoService extends IService<WmsOutboundNoticeOrderInfo> {

    /**
    *  分页查询
    * @param: wmsOutboundNoticeOrderInfoVO
    * @param: wmsOutboundNoticeOrderInfoVO
    * @param: currentPage
    * @param: pageSize
    * @param: req
    * @return
    */
    IPage<WmsOutboundNoticeOrderInfoVO> selectPage(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO,
                                                   Integer currentPage,
                                                   Integer pageSize,
                                                   HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundNoticeOrderInfoVO
    * @return
    */
    List<WmsOutboundNoticeOrderInfoVO> selectList(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoForExcel(Map<String, Object> paramMap);

    /**
     * @description 根据编码查询详情
     * @author  ciro
     * @date   2021/12/22 10:11
     * @param: wmsOutboundNoticeOrderInfoVO
     * @return: com.jayud.model.vo.WmsOutboundNoticeOrderInfoVO
     **/
    WmsOutboundNoticeOrderInfoVO queryByCode(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO);

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2021/12/22 10:42
     * @param: wmsOutboundNoticeOrderInfoVO
     * @return: com.jayud.model.vo.WmsOutboundNoticeOrderInfoVO
     **/
    BaseResult saveInfo(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO);

    /**
     * @description 根据id删除
     * @author  ciro
     * @date   2021/12/22 17:23
     * @param: id
     * @param: orderNumber
     * @return: void
     **/
    void delById(long id,String orderNumber);

    /**
     * @description 根据订单号修改统计数值
     * @author  ciro
     * @date   2022/1/25 15:19
     * @param: orderNumber
     * @return: void
     **/
    void changeAllAccount(String orderNumber);

}
