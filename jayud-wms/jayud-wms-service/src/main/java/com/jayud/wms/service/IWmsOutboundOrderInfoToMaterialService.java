package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.ConfirmInformationForm;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToMaterial;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单-物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-22
 */
public interface IWmsOutboundOrderInfoToMaterialService extends IService<WmsOutboundOrderInfoToMaterial> {

    /**
    *  分页查询
    * @param wmsOutboundOrderInfoToMaterialVO
    * @param req
    * @return
    */
    IPage<WmsOutboundOrderInfoToMaterialVO> selectPage(WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO,
                                                       Integer currentPage,
                                                       Integer pageSize,
                                                       HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundOrderInfoToMaterialVO
    * @return
    */
    List<WmsOutboundOrderInfoToMaterialVO> selectList(WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToMaterialForExcel(Map<String, Object> paramMap);


    /**
     * @description 转出库单
     * @author  ciro
     * @date   2021/12/23 10:03
     * @param: noticeOrderNumber
     * @param: orderNumber
     * @return: java.util.List<com.jayud.model.po.WmsOutboundOrderInfoToMaterial>
     **/
    List<WmsOutboundOrderInfoToMaterial> transferOut(String noticeOrderNumber,String orderNumber);

    /**
     * @description 分配库存
     * @author  ciro
     * @date   2021/12/30 18:02
     * @param: materialList
     * @return: void
     **/
    void allocateInventory(List<WmsOutboundOrderInfoToMaterialVO> materialList);

    List<QueryScanInformationVO> queryScanInformation(String orderNumber, String materialCode);

    List<WmsOutboundOrderInfoToMaterial> getOutboundOrderInfoToMaterialByWaveNumber(String orderNumber);

    /**
     * @description 转换最小单位数量
     * @author  ciro
     * @date   2021/12/30 18:11
     * @param: materialCode 物料编码
     * @param: unit         单位
     * @param: count        数量
     * @return: java.math.BigDecimal
     **/
    BigDecimal getDistributionAccount(String materialCode, String unit, BigDecimal count);


    /**
     * @description 完成出库单
     * @author  ciro
     * @date   2022/1/26 10:09
     * @param: orderNumberList
     * @return: void
     **/
    void finishOrder(List<String> orderNumberList);


    /**
     * @description 确认出库
     * @author  ciro
     * @date   2022/4/7 10:15
     * @param: confirmInformationForm
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO>>
     **/
    BaseResult<List<WmsOutboundOrderInfoToMaterialVO>> comfirmOutput(ConfirmInformationForm confirmInformationForm);

    /**
     * @description 获取入仓号
     * @author  ciro
     * @date   2022/4/7 16:13
     * @param: orderNumber
     * @return: java.lang.String
     **/
    String getInWarehouseNumbers(String orderNumber);
}
