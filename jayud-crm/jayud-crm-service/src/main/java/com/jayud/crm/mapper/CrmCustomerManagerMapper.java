package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCustomerManager;
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
 * 基本档案_客户_客户维护人(crm_customer_manager) Mapper 接口
 *
 * @author jayud
 * @since 2022-03-03
 */
@Mapper
public interface CrmCustomerManagerMapper extends BaseMapper<CrmCustomerManager> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: page
     * @param: crmCustomerManager
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerManager>
     **/
    IPage<CrmCustomerManager> pageList(@Param("page") Page<CrmCustomerManager> page, @Param("crmCustomerManager") CrmCustomerManager crmCustomerManager);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerManager
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerManager>
     **/
    List<CrmCustomerManager> list(@Param("crmCustomerManager") CrmCustomerManager crmCustomerManager);


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
     * @description 批量逻辑删除
     * @author  ciro
     * @date   2022/3/4 9:44
     * @param: ids
     * @param: username
     * @return: int
     **/
    int logicDelByIds(@Param("ids") List<Long> ids,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-03
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerManagerForExcel(Map<String, Object> paramMap);

    /**
    * @description 根据id集合删除
    * @author  ciro
    * @date   2022/3/14 17:10:31
    * @param custIds	客户id集合
    * @param username	操作人信息
    * @param isCharger	是否负责人
    * @return: void
    **/
    void logicDelByCustIds(@Param("custIds") List<Long> custIds,@Param("username") String username,@Param("isCharger") Boolean isCharger);

    /**
     * @description 根据用户id集合删除用户负责人
     * @author  ciro
     * @date   2022/3/7 18:06
     * @param: custIds
     * @param: username
     * @return: void
     **/
    void delChargerManager(@Param("custIds") List<Long> custIds,@Param("username") String username);
}
