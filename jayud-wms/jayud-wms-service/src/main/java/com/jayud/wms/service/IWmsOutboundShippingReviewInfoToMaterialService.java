package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * wms-出库发运复核物料信息 服务类
 *
 * @author jayud
 * @since 2022-04-07
 */
public interface IWmsOutboundShippingReviewInfoToMaterialService extends IService<WmsOutboundShippingReviewInfoToMaterial> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfoToMaterial
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>
     **/
    IPage<WmsOutboundShippingReviewInfoToMaterial> selectPage(WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfoToMaterial
     * @param: req
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>
     **/
    List<WmsOutboundShippingReviewInfoToMaterial> selectList(WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-04-07
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-07
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsOutboundShippingReviewInfoToMaterialForExcel(Map<String, Object> paramMap);

    /**
     * @description 确认发运复核
     * @author  ciro
     * @date   2022/4/7 14:45
     * @param: reviewInfo
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>>
     **/
    BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> comfirmReview(WmsOutboundShippingReviewInfoVO reviewInfo);

    /**
     * @description 撤销发运复核
     * @author  ciro
     * @date   2022/4/7 15:03
     * @param: reviewInfo
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>>
     **/
    BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> cancelReview(WmsOutboundShippingReviewInfoVO reviewInfo);

}
