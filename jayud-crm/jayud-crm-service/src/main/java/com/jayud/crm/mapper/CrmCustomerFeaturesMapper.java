package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCustomerFeatures;
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
 * 基本档案_客户_业务特征 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-03
 */
@Mapper
public interface CrmCustomerFeaturesMapper extends BaseMapper<CrmCustomerFeatures> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: page
     * @param: crmCustomerFeatures
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerFeatures>
     **/
    IPage<CrmCustomerFeatures> pageList(@Param("page") Page<CrmCustomerFeatures> page, @Param("crmCustomerFeatures") CrmCustomerFeatures crmCustomerFeatures);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerFeatures
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerFeatures>
     **/
    List<CrmCustomerFeatures> list(@Param("crmCustomerFeatures") CrmCustomerFeatures crmCustomerFeatures);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-03
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerFeaturesForExcel(Map<String, Object> paramMap);
}
