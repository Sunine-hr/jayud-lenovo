package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsCustomerDevelopmentSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户开发设置表 Mapper 接口
 *
 * @author jyd
 * @since 2022-02-14
 */
@Mapper
public interface WmsCustomerDevelopmentSettingMapper extends BaseMapper<WmsCustomerDevelopmentSetting> {
    /**
    *   分页查询
    */
    IPage<WmsCustomerDevelopmentSetting> pageList(@Param("page") Page<WmsCustomerDevelopmentSetting> page, @Param("wmsCustomerDevelopmentSetting") WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting);

    /**
    *   列表查询
    */
    List<WmsCustomerDevelopmentSetting> list(@Param("wmsCustomerDevelopmentSetting") WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsCustomerDevelopmentSettingForExcel(Map<String, Object> paramMap);
}
