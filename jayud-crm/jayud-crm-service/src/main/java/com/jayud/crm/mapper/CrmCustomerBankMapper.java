package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCustomerBank;
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
 * 基本档案_客户_银行账户(crm_customer_bank) Mapper 接口
 *
 * @author jayud
 * @since 2022-03-02
 */
@Mapper
public interface CrmCustomerBankMapper extends BaseMapper<CrmCustomerBank> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: page
     * @param: crmCustomerBank
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerBank>
     **/
    IPage<CrmCustomerBank> pageList(@Param("page") Page<CrmCustomerBank> page, @Param("crmCustomerBank") CrmCustomerBank crmCustomerBank);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerBank
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerBank>
     **/
    List<CrmCustomerBank> list(@Param("crmCustomerBank") CrmCustomerBank crmCustomerBank);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);

    /**
     * @description 根据id批量逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: ids
     * @param: username
     * @return: int
     **/
    int logicDelByIds(@Param("ids") List<Long> ids,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-02
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerBankForExcel(Map<String, Object> paramMap);
}
