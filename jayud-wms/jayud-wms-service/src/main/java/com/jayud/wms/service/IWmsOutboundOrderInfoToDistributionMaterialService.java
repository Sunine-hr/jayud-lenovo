package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToDistributionMaterial;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToDistributionMaterialVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库单-分配物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsOutboundOrderInfoToDistributionMaterialService extends IService<WmsOutboundOrderInfoToDistributionMaterial> {

    /**
    *  分页查询
    * @param wmsOutboundOrderInfoToDistributionMaterial
    * @param req
    * @return
    */
    IPage<WmsOutboundOrderInfoToDistributionMaterial> selectPage(WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsOutboundOrderInfoToDistributionMaterial
    * @return
    */
    List<WmsOutboundOrderInfoToDistributionMaterial> selectList(WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToDistributionMaterialForExcel(Map<String, Object> paramMap);

    /**
     * @description 判断是否分配
     * @author  ciro
     * @date   2021/12/27 14:57
     * @param: orderNumber
     * @param: waveNumber
     * @return: boolean
     **/
    boolean checkIsAllocation(String orderNumber,String waveNumber);

    /**
     * @description 根据出库单/波次号获取分配数据
     * @author  ciro
     * @date   2021/12/30 15:16
     * @param: orderNumber
     * @param: isWave
     * @return: java.util.List<com.jayud.model.bo.InventoryDetailForm>
     **/
    List<InventoryDetailForm> getInventoryByOrderNumber(String orderNumber,boolean isWave);

    /**
     * @description 取消没有生成拣货下架分配
     * @author  ciro
     * @date   2022/1/24 13:46
     * @param: orderNumber
     * @param: isWave
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult cancleDistributionToUnpacking(String orderNumber,boolean isWave);

    /**
     * @description 初始化分配数据
     * @author  ciro
     * @date   2022/1/26 13:42
     * @param: distributionMaterialList
     * @return: java.util.List<com.jayud.model.bo.InventoryDetailForm>
     **/
    List<InventoryDetailForm> initInventoryDetail(List<WmsOutboundOrderInfoToDistributionMaterial> distributionMaterialList);

    /**
     * 根据物料信息，查询 分配物料信息
     * @param orderMaterialIds
     * @return
     */
    List<WmsOutboundOrderInfoToDistributionMaterialVO> selectListByOrderMaterialIds(List<Long> orderMaterialIds);
}
