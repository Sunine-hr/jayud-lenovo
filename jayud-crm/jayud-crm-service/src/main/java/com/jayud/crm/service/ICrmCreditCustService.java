package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.bo.AddCrmCreditCustForm;
import com.jayud.crm.model.po.CrmCreditCust;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_额度_额度授信管理(crm_credit_cust) 服务类
 *
 * @author jayud
 * @since 2022-03-04
 */
public interface ICrmCreditCustService extends IService<CrmCreditCust> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-04
     * @param: crmCreditCust
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditCust>
     **/
    IPage<CrmCreditCust> selectPage(CrmCreditCust crmCreditCust,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-04
     * @param: crmCreditCust
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditCust>
     **/
    List<CrmCreditCust> selectList(CrmCreditCust crmCreditCust);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-04
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-04
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-04
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCreditCustForExcel(Map<String, Object> paramMap);

    /**
     * 计算剩余授信额度
     * @param departId
     * @param creditId
     * @param userTenantCode
     * @return
     */
    BigDecimal calculationRemainingCreditLine(Long departId, String creditId, String userTenantCode);

    /**
     * 新增/编辑
     * @param form
     */
    void saveOrUpdate(AddCrmCreditCustForm form);
}
