package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.ComCustomerForm;
import com.jayud.crm.model.bo.CrmCustomerRiskForm;
import com.jayud.crm.model.po.CrmCustomer;
import com.jayud.crm.model.po.CrmCustomerRisk;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_风险客户（crm_customer_risk） 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmCustomerRiskService extends IService<CrmCustomerRisk> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerRisk
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerRisk>
     **/
    IPage<CrmCustomerRisk> selectPage(CrmCustomerRiskForm crmCustomerRiskForm,
                                      Integer currentPage,
                                      Integer pageSize,
                                      HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerRisk
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerRisk>
     **/
    List<CrmCustomerRisk> selectList(CrmCustomerRisk crmCustomerRisk);

    BaseResult saveOrUpdateCrmCustomerRisk(CrmCustomerRiskForm crmCustomerRiskForm);

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
    void logicDel(List<Long> ids);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-02
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerRiskForExcel(Map<String, Object> paramMap);

    /**
     * @description 判断客户是否存在风险
     * @author  ciro
     * @date   2022/3/4 18:14
     * @param: crmCustomer
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult checkIsRisk(CrmCustomer crmCustomer);



    /**
     * @description 根据客户集合判断黑名单
     * @author  ciro
     * @date   2022/3/4 18:28
     * @param: comCustomerForm
     * @return: com.jayud.crm.model.bo.ComCustomerForm
     **/
    ComCustomerForm checkIsRiskByCutsIds(ComCustomerForm comCustomerForm);

}
