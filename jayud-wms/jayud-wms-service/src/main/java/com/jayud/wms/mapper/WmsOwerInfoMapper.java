package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.WmsOwerInfoForm;
import com.jayud.wms.model.po.WmsOwerInfo;
import com.jayud.wms.model.vo.WmsOwerInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 货主信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-13
 */
@Mapper
public interface WmsOwerInfoMapper extends BaseMapper<WmsOwerInfo> {
    /**
    *   分页查询
    */
    IPage<WmsOwerInfo> pageList(@Param("page") Page<WmsOwerInfo> page, @Param("wmsOwerInfo") WmsOwerInfo wmsOwerInfo);

    /**
    *   列表查询
    */
    List<WmsOwerInfo> list(@Param("wmsOwerInfo") WmsOwerInfo wmsOwerInfo);



    WmsOwerInfo getWmsWmsOwerInfoCodeName(@Param("owerCode")String owerCode,@Param("owerName") String owerName);


    WmsOwerInfoVO  findWmsWmsOwerInfoCodeNameOne(@Param("id")  Long id);

    /**
     *   列表查询
     */
    List<WmsOwerInfo> selectWmsOwerInfoWarehouseIdList(@Param("wmsOwerInfoForm") WmsOwerInfoForm wmsOwerInfoForm);

    //导出
    List<LinkedHashMap<String, Object>> queryWmsOwerInfoForExcel(@Param("wmsOwerInfo") WmsOwerInfo wmsOwerInfo);
}
