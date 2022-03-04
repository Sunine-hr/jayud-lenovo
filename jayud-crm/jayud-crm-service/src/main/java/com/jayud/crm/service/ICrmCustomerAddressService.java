package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.CrmCustomerAddressForm;
import com.jayud.crm.model.po.CrmCustomerAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_地址 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCustomerAddressService extends IService<CrmCustomerAddress> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAddress
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerAddress>
     **/
    IPage<CrmCustomerAddress> selectPage(CrmCustomerAddress crmCustomerAddress,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAddress
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerAddress>
     **/
    List<CrmCustomerAddress> selectList(CrmCustomerAddress crmCustomerAddress);


    BaseResult saveOrUpdateCrmCustomerAddress(CrmCustomerAddressForm crmCustomerAddressForm);
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
    void logicDel(List<Long> ids);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-03
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmCustomerAddressForExcel(Map<String, Object> paramMap);


}
