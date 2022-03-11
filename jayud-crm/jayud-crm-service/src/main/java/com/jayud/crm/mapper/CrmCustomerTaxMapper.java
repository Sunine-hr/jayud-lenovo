package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCustomerTax;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.jayud.crm.model.vo.CrmCustomerTaxVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 开票资料 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-03
 */
@Mapper
public interface CrmCustomerTaxMapper extends BaseMapper<CrmCustomerTax> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: page
     * @param: crmCustomerTax
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerTax>
     **/
    IPage<CrmCustomerTaxVO> pageList(@Param("page") Page<CrmCustomerTax> page, @Param("crmCustomerTax") CrmCustomerTax crmCustomerTax);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerTax
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerTax>
     **/
    List<CrmCustomerTaxVO> list(@Param("crmCustomerTax") CrmCustomerTax crmCustomerTax);


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
    List<LinkedHashMap<String, Object>> queryCrmCustomerTaxForExcel(Map<String, Object> paramMap);

    /**
     * 根据客户id 修改是否是默认开票信息
     * @param crmCustomerTax
     * @return
     */
    int updateCrmCustomerTaxList(@Param("crmCustomerTax") CrmCustomerTax crmCustomerTax);
}
