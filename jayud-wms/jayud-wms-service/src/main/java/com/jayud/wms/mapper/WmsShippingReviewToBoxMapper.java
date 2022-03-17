package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsShippingReviewToBox;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发运复核-箱数关系 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-24
 */
@Mapper
public interface WmsShippingReviewToBoxMapper extends BaseMapper<WmsShippingReviewToBox> {
    /**
    *   分页查询
    */
    IPage<WmsShippingReviewToBox> pageList(@Param("page") Page<WmsShippingReviewToBox> page, @Param("wmsShippingReviewToBox") WmsShippingReviewToBox wmsShippingReviewToBox);

    /**
    *   列表查询
    */
    List<WmsShippingReviewToBox> list(@Param("wmsShippingReviewToBox") WmsShippingReviewToBox wmsShippingReviewToBox);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsShippingReviewToBoxForExcel(Map<String, Object> paramMap);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int phyDelById(@Param("id") int id);
}
