package com.jayud.crm.mapper;

import com.jayud.crm.model.po.CrmCustomerAgreementSub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.jayud.crm.model.vo.CrmCustomerAgreementSubVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_协议管理_子协议(crm_customer_agreement_sub) Mapper 接口
 *
 * @author jayud
 * @since 2022-03-03
 */
@Mapper
public interface CrmCustomerAgreementSubMapper extends BaseMapper<CrmCustomerAgreementSub> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: page
     * @param: crmCustomerAgreementSub
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerAgreementSub>
     **/
    IPage<CrmCustomerAgreementSubVO> pageList(@Param("page") Page<CrmCustomerAgreementSub> page, @Param("crmCustomerAgreementSub") CrmCustomerAgreementSub crmCustomerAgreementSub);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAgreementSub
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerAgreementSub>
     **/
    List<CrmCustomerAgreementSub> list(@Param("crmCustomerAgreementSub") CrmCustomerAgreementSub crmCustomerAgreementSub);


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
    List<LinkedHashMap<String, Object>> queryCrmCustomerAgreementSubForExcel(Map<String, Object> paramMap);
}
