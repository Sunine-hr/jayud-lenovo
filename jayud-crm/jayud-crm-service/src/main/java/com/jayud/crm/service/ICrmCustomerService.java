package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.po.CrmCustomer;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_基本信息(crm_customer) 服务类
 *
 * @author jayud
 * @since 2022-02-28
 */
public interface ICrmCustomerService extends IService<CrmCustomer> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-28
     * @param: crmCustomer
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomer>
     **/
    IPage<CrmCustomer> selectPage(CrmCustomer crmCustomer,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-28
     * @param: crmCustomer
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomer>
     **/
    List<CrmCustomer> selectList(CrmCustomer crmCustomer);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-28
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);




}
