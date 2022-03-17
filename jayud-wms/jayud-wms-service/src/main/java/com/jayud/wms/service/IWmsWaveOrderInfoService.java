package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsWaveOrderInfo;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsWaveInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次单 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsWaveOrderInfoService extends IService<WmsWaveOrderInfo> {

    /**
    *  分页查询
    * @param wmsWaveOrderInfo
    * @param req
    * @return
    */
    IPage<WmsWaveOrderInfo> selectPage(WmsWaveOrderInfo wmsWaveOrderInfo,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsWaveOrderInfo
    * @return
    */
    List<WmsWaveOrderInfo> selectList(WmsWaveOrderInfo wmsWaveOrderInfo);



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
    List<LinkedHashMap<String, Object>> queryWmsWaveOrderInfoForExcel(Map<String, Object> paramMap);

    /**
     * @description 生成波次
     * @author  ciro
     * @date   2021/12/27 9:20
     * @param: wmsWaveInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult createWave(WmsWaveInfoVO wmsWaveInfoVO);

    /**
     * @description 根据波次号查询
     * @author  ciro
     * @date   2021/12/27 10:32
     * @param: waveNumber
     * @return: com.jayud.model.vo.WmsWaveInfoVO
     **/
    WmsWaveInfoVO queryByWaveNumber(String waveNumber);

    /**
     * @description 修改波次单出库单
     * @author  ciro
     * @date   2021/12/27 14:25
     * @param: wmsWaveInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult changeOrder(WmsWaveInfoVO wmsWaveInfoVO);


    /**
     * @description 删除波次
     * @author  ciro
     * @date   2022/1/4 16:04
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult delWave(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 修改波次状态
     * @author  ciro
     * @date   2022/1/4 9:26
     * @param: wareNumber
     * @return: boolean
     **/
    boolean updateByWareNumber(String wareNumber);

    /**
     * @description 根据波次号获取波次数据
     * @author  ciro
     * @date   2022/1/4 9:26
     * @param: orderNumber
     * @return: com.jayud.model.po.WmsWaveOrderInfo
     **/
    WmsWaveOrderInfo getWmsWaveOrderInfoByWaveNumber(String orderNumber);

    /**
     * @description 删除波次
     * @author  ciro
     * @date   2021/12/31 11:23
     * @param: wmsWaveInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult delWave(WmsWaveInfoVO wmsWaveInfoVO);

    /**
     * @description 分配波次库存
     * @author  ciro
     * @date   2021/12/31 14:33
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult allocateWaveInventory(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 撤销库存分配
     * @author  ciro
     * @date   2021/12/31 15:33
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult caneclAllocateWaveInventory(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 根据波次号查询
     * @author  ciro
     * @date   2022/1/4 14:23
     * @param: waveNumber
     * @return: com.jayud.model.po.WmsWaveOrderInfo
     **/
    WmsWaveOrderInfo queryByWaveOrderNumber(String waveNumber);

    /**
     * @description 增加临时关系表数据
     * @author  ciro
     * @date   2022/1/4 15:50
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult addOrderRelation(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 删除临时关系表数据
     * @author  ciro
     * @date   2022/1/4 15:52
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult delOrderRelation(OutboundOrderNumberVO outboundOrderNumberVO);
}
