package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsShippingReview;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsShippingReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发运复核 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-24
 */
@Mapper
public interface WmsShippingReviewMapper extends BaseMapper<WmsShippingReview> {
    /**
    *   分页查询
    */
    IPage<WmsShippingReviewVO> pageList(@Param("page") Page<WmsShippingReviewVO> page, @Param("wmsShippingReview") WmsShippingReview wmsShippingReview);

    /**
    *   列表查询
    */
    List<WmsShippingReviewVO> list(@Param("wmsShippingReview") WmsShippingReview wmsShippingReview);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsShippingReviewForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);

    Integer getBoxNumberByWareNumber(@Param("wareNumber")String wareNumber);

    List<QueryScanInformationVO> queryScanInformation(@Param("orderNumber") String orderNumber, @Param("materialCode") String materialCode);

    List<LinkedHashMap<String, Object>> exportShipmentReviewVariance(Map<String, Object> paramMap);
}
