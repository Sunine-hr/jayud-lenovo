package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.CrmCustomerFeaturesForm;
import com.jayud.crm.model.po.CrmCustomerFeatures;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_业务特征 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCustomerFeaturesService extends IService<CrmCustomerFeatures> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerFeatures
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerFeatures>
     **/
    IPage<CrmCustomerFeatures> selectPage(CrmCustomerFeatures crmCustomerFeatures,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerFeatures
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerFeatures>
     **/
    List<CrmCustomerFeatures> selectList(CrmCustomerFeatures crmCustomerFeatures);

    /**
     * 创建编辑
     * @param crmCustomerFeaturesForm
     * @return
     */
    BaseResult saveOrUpdateCrmCustomerFeatures(CrmCustomerFeaturesForm crmCustomerFeaturesForm);

    /**
     * 创建客户调用
     * 创建业务要求默认数据
     * @param custId
     */
    void saveCrmCustomerFeatures(Long custId);
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
    List<LinkedHashMap<String, Object>> queryCrmCustomerFeaturesForExcel(Map<String, Object> paramMap);


}
