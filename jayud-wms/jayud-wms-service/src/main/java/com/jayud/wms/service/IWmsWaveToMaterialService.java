package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsWaveToMaterial;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 波次单-物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-27
 */
public interface IWmsWaveToMaterialService extends IService<WmsWaveToMaterial> {

    /**
    *  分页查询
    * @param wmsWaveToMaterial
    * @param req
    * @return
    */
    IPage<WmsWaveToMaterial> selectPage(WmsWaveToMaterial wmsWaveToMaterial,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsWaveToMaterial
    * @return
    */
    List<WmsWaveToMaterial> selectList(WmsWaveToMaterial wmsWaveToMaterial);



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
    List<LinkedHashMap<String, Object>> queryWmsWaveToMaterialForExcel(Map<String, Object> paramMap);


    List<QueryScanInformationVO> queryScanInformation(String orderNumber, String materialCode);

    List<WmsWaveToMaterial> getWmsWaveToMaterialByWaveNumber(String wareNumber);
    /**
     * @description 保存波次喝物料信息关系
     * @author  ciro
     * @date   2021/12/27 9:58
     * @param: materialVOList
     * @param: waveOrderNumber
     * @return: void
     **/
    void addWaveToMaterial(List<WmsOutboundOrderInfoToMaterialVO> materialVOList,String waveOrderNumber);
}
