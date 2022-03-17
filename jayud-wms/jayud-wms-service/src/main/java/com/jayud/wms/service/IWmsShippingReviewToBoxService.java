package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsShippingReviewToBox;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发运复核-箱数关系 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsShippingReviewToBoxService extends IService<WmsShippingReviewToBox> {

    /**
    *  分页查询
    * @param wmsShippingReviewToBox
    * @param req
    * @return
    */
    IPage<WmsShippingReviewToBox> selectPage(WmsShippingReviewToBox wmsShippingReviewToBox,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsShippingReviewToBox
    * @return
    */
    List<WmsShippingReviewToBox> selectList(WmsShippingReviewToBox wmsShippingReviewToBox);



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
    List<LinkedHashMap<String, Object>> queryWmsShippingReviewToBoxForExcel(Map<String, Object> paramMap);


}
