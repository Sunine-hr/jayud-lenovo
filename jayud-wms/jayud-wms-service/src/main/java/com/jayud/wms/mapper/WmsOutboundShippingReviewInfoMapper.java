package com.jayud.wms.mapper;

import com.jayud.wms.model.po.WmsOutboundShippingReviewInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;

/**
 * wms-出库发运复核 Mapper 接口
 *
 * @author jayud
 * @since 2022-04-07
 */
@Mapper
public interface WmsOutboundShippingReviewInfoMapper extends BaseMapper<WmsOutboundShippingReviewInfo> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-07
     * @param: page
     * @param: wmsOutboundShippingReviewInfo
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>
     **/
    IPage<WmsOutboundShippingReviewInfo> pageList(@Param("page") Page<WmsOutboundShippingReviewInfo> page, @Param("wmsOutboundShippingReviewInfo") WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfo
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfo>
     **/
    List<WmsOutboundShippingReviewInfo> list(@Param("wmsOutboundShippingReviewInfo") WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-04-07
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-07
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsOutboundShippingReviewInfoForExcel(Map<String, Object> paramMap);

    /**
     * @description 修改发运复核状态
     * @author  ciro
     * @date   2022/4/7 14:59
     * @param: statusType
     * @param: orderNumber
     * @param: username
     * @return: int
     **/
    int changeStatusByOrderNumber(@Param("statusType") int statusType,@Param("orderNumber") String orderNumber,@Param("username") String username);

    /**
     * @description 根据编码删除
     * @author  ciro
     * @date   2022/4/7 15:11
     * @param: numbers
     * @param: username
     * @return: int
     **/
    int delByOrderNumbers(@Param("numbers") List<String> numbers,@Param("username") String username);

    /**
     * @description 根据时间获取完成数量
     * @author  ciro
     * @date   2022/4/12 13:59
     * @param: tenantCode
     * @param: yearAndMonth
     * @return: java.util.LinkedList<java.util.LinkedHashMap>
     **/
    LinkedList<LinkedHashMap> selectFinishCountByTime(@Param("tenantCode") String tenantCode, @Param("yearAndMonth") String yearAndMonth);
}
