package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.form.CrmCustomerForm;
import com.jayud.crm.model.po.CrmCustomerManager;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_客户维护人(crm_customer_manager) 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCustomerManagerService extends IService<CrmCustomerManager> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerManager
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerManager>
     **/
    IPage<CrmCustomerManager> selectPage(CrmCustomerManager crmCustomerManager,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerManager
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerManager>
     **/
    List<CrmCustomerManager> selectList(CrmCustomerManager crmCustomerManager);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-03
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-03
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-03
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerManagerForExcel(Map<String, Object> paramMap);

    /**
     * @description 保存客户时修改
     * @author  ciro
     * @date   2022/3/3 10:37
     * @param: userId
     * @param: custId
     * @return: void
     **/
    void saveByCustomer(CrmCustomerForm crmCustomerForm);


    /**
     * @description 保存我司对接人
     * @author  ciro
     * @date   2022/3/3 15:48
     * @param: crmCustomerManager
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerManager>
     **/
    BaseResult<CrmCustomerManager> saveManager(CrmCustomerManager crmCustomerManager);



}
