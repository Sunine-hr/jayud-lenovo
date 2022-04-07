package com.jayud.wms.mapper;

import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * wms-出库发运复核物料信息 Mapper 接口
 *
 * @author jayud
 * @since 2022-04-07
 */
@Mapper
public interface WmsOutboundShippingReviewInfoToMaterialMapper extends BaseMapper<WmsOutboundShippingReviewInfoToMaterial> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-07
     * @param: page
     * @param: wmsOutboundShippingReviewInfoToMaterial
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>
     **/
    IPage<WmsOutboundShippingReviewInfoToMaterial> pageList(@Param("page") Page<WmsOutboundShippingReviewInfoToMaterial> page, @Param("wmsOutboundShippingReviewInfoToMaterial") WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-07
     * @param: wmsOutboundShippingReviewInfoToMaterial
     * @return: java.util.List<com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial>
     **/
    List<WmsOutboundShippingReviewInfoToMaterial> list(@Param("wmsOutboundShippingReviewInfoToMaterial") WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial);


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
    List<LinkedHashMap<String, Object>> queryWmsOutboundShippingReviewInfoToMaterialForExcel(Map<String, Object> paramMap);


    /**
     * @description 根据编码删除
     * @author  ciro
     * @date   2022/4/7 15:11
     * @param: numbers
     * @param: username
     * @return: int
     **/
    int delByOrderNumbers(@Param("numbers") List<String> numbers,@Param("username") String username);

}
