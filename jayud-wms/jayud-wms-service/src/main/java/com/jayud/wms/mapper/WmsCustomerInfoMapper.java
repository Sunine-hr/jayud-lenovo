package com.jayud.wms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsCustomerInfo;
import com.jayud.wms.model.vo.WmsCustomerInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * <p>
 * 客户信息Mapper 接口
 * </p>

 */
@Mapper
public interface WmsCustomerInfoMapper extends BaseMapper<WmsCustomerInfo> {

    /**
     *   分页查询
     */
    IPage<WmsCustomerInfoVO> pageList(@Param("page") Page<WmsCustomerInfo> page, @Param("wmsCustomerInfo") WmsCustomerInfo wmsCustomerInfo);

    /**
     *  查询列表
     * @param wmsCustomerInfo
     * @return
     */
    List<WmsCustomerInfoVO> selectList( @Param("wmsCustomerInfo")WmsCustomerInfo wmsCustomerInfo);

    WmsCustomerInfo getWmsCustomerInfoByCode(@Param("customerCode") String customerCode,@Param("customerName")  String customerName);



    // 导出
    List<LinkedHashMap<String, Object>> queryWmsCustomerInfoForExcel(@Param("wmsCustomerInfo") WmsCustomerInfo wmsCustomerInfo);



}
