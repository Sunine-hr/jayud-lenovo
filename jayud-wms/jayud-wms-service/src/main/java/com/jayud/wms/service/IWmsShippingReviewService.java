package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.AddWmsShippingReviewForm;
import com.jayud.wms.model.bo.CloseTheBoxForm;
import com.jayud.wms.model.po.WmsShippingReview;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsShippingReviewVO;
import com.jayud.common.dto.WmsShippingReviewForm;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发运复核 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsShippingReviewService extends IService<WmsShippingReview> {

    /**
    *  分页查询
    * @param wmsShippingReview
    * @param req
    * @return
    */
    IPage<WmsShippingReviewVO> selectPage(WmsShippingReview wmsShippingReview,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsShippingReview
    * @return
    */
    List<WmsShippingReviewVO> selectList(WmsShippingReview wmsShippingReview);



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
    List<LinkedHashMap<String, Object>> queryWmsShippingReviewForExcel(Map<String, Object> paramMap);


    boolean saveWmsShippingReview(List<AddWmsShippingReviewForm> form);

    boolean CloseTheBox(CloseTheBoxForm form);

    boolean singleReviewSubmission(AddWmsShippingReviewForm form);

    List<QueryScanInformationVO> queryScanInformation(String orderNumber, String materialCode);

    List<LinkedHashMap<String, Object>> exportShipmentReviewVariance(Map<String, Object> paramMap);

    List<WmsShippingReview> getWmsShippingReviewByOrderNumber(String orderNumber);

    List<WmsShippingReview> getWmsShippingReviewByWaveNumber(String orderNumber);

    boolean updateWmsShippingReviewStatusByOrderNumber(String orderNumber);

    boolean updateWmsShippingReviewStatusByWaveNumber(String orderNumber);

    boolean unpacking(Long id);

    /**
     * @description APP确认
     * @author  ciro
     * @date   2022/1/10 10:05
     * @param: wmsShippingReviewForm
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult confirmForApp(WmsShippingReviewForm wmsShippingReviewForm);

}
