package com.jayud.wms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.wms.model.po.WmsOwerToWarehouseRelation;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 客户信息Mapper 接口
 * </p>

 */
@Mapper
public interface WmsOwerToWarehouseRelationMapper extends BaseMapper<WmsOwerToWarehouseRelation> {

//    /**
//     *   分页查询
//     */
//    IPage<WmsOwerToWarehouseRelation> pageList(@Param("page") Page<WmsOwerToWarehouseRelation> page, @Param("wmsOwerToWarehouseRelation")WmsOwerToWarehouseRelation wmsOwerToWarehouseRelation);

    int updateWmsOwerToWarehouseRelationId(WmsOwerToWarehouseRelation form);


//
//    WmsCustomerInfo getWmsCustomerInfoByCode(@Param("customerCode") String customerCode,@Param("customerName")  String customerName);



}
