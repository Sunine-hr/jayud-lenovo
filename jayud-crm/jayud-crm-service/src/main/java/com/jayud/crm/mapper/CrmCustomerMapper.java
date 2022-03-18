package com.jayud.crm.mapper;

import com.jayud.crm.model.bo.CrmCustomerForm;
import com.jayud.crm.model.po.CrmCustomer;
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
 * 基本档案_客户_基本信息(crm_customer) Mapper 接口
 *
 * @author jayud
 * @since 2022-03-01
 */
@Mapper
public interface CrmCustomerMapper extends BaseMapper<CrmCustomer> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: page
     * @param: crmCustomer
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.bo.CrmCustomerForm>
     **/
    IPage<CrmCustomerForm> pageList(@Param("page") Page<CrmCustomerForm> page, @Param("crmCustomer") CrmCustomerForm crmCustomer);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: crmCustomer
     * @return: java.util.List<com.jayud.crm.model.bo.CrmCustomerForm>
     **/
    List<CrmCustomerForm> list(@Param("crmCustomer") CrmCustomerForm crmCustomer);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-01
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerForExcel(Map<String, Object> paramMap);


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
     * @description 更新企业负责人信息
     * @author  ciro
     * @date   2022/3/7 16:12
     * @param: ids
     * @param: managerUserId
     * @param: managerUsername
     * @param: username
     * @return: int
     **/
    int updateManagerMsg(@Param("ids") List<Long> ids,@Param("managerUserId") Long managerUserId,@Param("managerUsername")String managerUsername,@Param("username") String username);

    /**
    * @description 根据id集合取消公海
    * @author  ciro
    * @date   2022/3/18 11:23:53
    * @param ids
    * @return: int
    **/
    int cnaleInpublicByIds(@Param("ids") List<Long> ids,@Param("managerUserId") Long managerUserId,@Param("managerUsername")String managerUsername,@Param("username") String username);
}
