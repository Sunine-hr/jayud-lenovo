package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * wms-出库发运复核 服务类
 *
 * @author jayud
 * @since 2022-04-07
 */
public interface IWmsOutboundShippingReviewInfoService extends IService<WmsOutboundShippingReviewInfo> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>
     **/
    IPage<WmsOutboundShippingReviewInfo> selectPage(WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfo
     * @param: req
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>
     **/
    List<WmsOutboundShippingReviewInfo> selectList(WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo);



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
    List<LinkedHashMap<String, Object>> queryWmsOutboundShippingReviewInfoForExcel(Map<String, Object> paramMap);

    /**
     * @description 转发运复核
     * @author  ciro
     * @date   2022/4/7 13:37
     * @param: wmsOutboundOrderInfoVO
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult<WmsOutboundShippingReviewInfo> changeToReview(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO);

    /**
     * @description 根据信息查询发运复核数据
     * @author  ciro
     * @date   2022/4/7 14:31
     * @param: info
     * @return: com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO
     **/
    WmsOutboundShippingReviewInfoVO queryByCode(WmsOutboundShippingReviewInfo info);

    /**
     * @description 根据编码集合删除
     * @author  ciro
     * @date   2022/4/7 15:09
     * @param: deleteForm
     * @return: void
     **/
    void delByOrderNumbers(DeleteForm deleteForm);

    /**
     * @description 编辑
     * @author  ciro
     * @date   2022/4/8 15:22
     * @param: info
     * @return: void
     **/
    void edit(WmsOutboundShippingReviewInfo info);

}
