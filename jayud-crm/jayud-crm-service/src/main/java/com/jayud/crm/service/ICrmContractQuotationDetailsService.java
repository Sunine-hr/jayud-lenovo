package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.po.CrmContractQuotationDetails;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同报价详情 服务类
 *
 * @author jayud
 * @since 2022-03-01
 */
public interface ICrmContractQuotationDetailsService extends IService<CrmContractQuotationDetails> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: crmContractQuotationDetails
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmContractQuotationDetails>
     **/
    IPage<CrmContractQuotationDetails> selectPage(CrmContractQuotationDetails crmContractQuotationDetails,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: crmContractQuotationDetails
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmContractQuotationDetails>
     **/
    List<CrmContractQuotationDetails> selectList(CrmContractQuotationDetails crmContractQuotationDetails);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-01
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-01
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmContractQuotationDetailsForExcel(Map<String, Object> paramMap);


}
