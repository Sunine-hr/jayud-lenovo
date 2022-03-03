package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.CrmCustomerRelationsForm;
import com.jayud.crm.model.po.CrmCustomerRelations;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_联系人(crm_customer_relations) 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmCustomerRelationsService extends IService<CrmCustomerRelations> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerRelations
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerRelations>
     **/
    IPage<CrmCustomerRelations> selectPage(CrmCustomerRelations crmCustomerRelations,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerRelations
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerRelations>
     **/
    List<CrmCustomerRelations> selectList(CrmCustomerRelations crmCustomerRelations);

    BaseResult saveOrUpdateCrmCustomerRelations(CrmCustomerRelationsForm crmCustomerRelationsForm);

    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-02
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-02
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerRelationsForExcel(Map<String, Object> paramMap);


}
