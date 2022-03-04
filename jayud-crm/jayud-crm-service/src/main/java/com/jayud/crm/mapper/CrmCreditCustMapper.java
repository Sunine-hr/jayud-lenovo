package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCreditCust;
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
 * 基本档案_额度_额度授信管理(crm_credit_cust) Mapper 接口
 *
 * @author jayud
 * @since 2022-03-04
 */
@Mapper
public interface CrmCreditCustMapper extends BaseMapper<CrmCreditCust> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-04
     * @param: page
     * @param: crmCreditCust
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditCust>
     **/
    IPage<CrmCreditCust> pageList(@Param("page") Page<CrmCreditCust> page, @Param("crmCreditCust") CrmCreditCust crmCreditCust);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-04
     * @param: crmCreditCust
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditCust>
     **/
    List<CrmCreditCust> list(@Param("crmCreditCust") CrmCreditCust crmCreditCust);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-04
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-04
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-04
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCreditCustForExcel(Map<String, Object> paramMap);
}
