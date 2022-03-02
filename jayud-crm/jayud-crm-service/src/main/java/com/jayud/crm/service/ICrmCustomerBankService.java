package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.po.CrmCustomerBank;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_银行账户(crm_customer_bank) 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmCustomerBankService extends IService<CrmCustomerBank> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerBank
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerBank>
     **/
    IPage<CrmCustomerBank> selectPage(CrmCustomerBank crmCustomerBank,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerBank
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerBank>
     **/
    List<CrmCustomerBank> selectList(CrmCustomerBank crmCustomerBank);



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
     * @description 根据id批量逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: ids
     * @return: com.jyd.component.commons.result.Result
     **/
    void logicDelByIds(Long[] ids);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-02
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerBankForExcel(Map<String, Object> paramMap);

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2022/3/2 14:18
     * @param: crmCustomerBank
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCustomerBank>
     **/
    BaseResult<CrmCustomerBank> saveBank(CrmCustomerBank crmCustomerBank);

}
