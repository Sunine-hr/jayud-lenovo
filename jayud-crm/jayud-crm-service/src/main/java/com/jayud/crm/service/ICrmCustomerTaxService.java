package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.CrmCustomerTaxForm;
import com.jayud.crm.model.po.CrmCustomerTax;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCustomerTaxVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 开票资料 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCustomerTaxService extends IService<CrmCustomerTax> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerTax
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerTax>
     **/
    IPage<CrmCustomerTaxVO> selectPage(CrmCustomerTax crmCustomerTax,
                                       Integer currentPage,
                                       Integer pageSize,
                                       HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerTax
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerTax>
     **/
    List<CrmCustomerTaxVO> selectList(CrmCustomerTax crmCustomerTax);

    /**
     * 创建编辑
     * @param crmCustomerTaxForm
     * @return
     */
    BaseResult saveOrUpdateCrmCustomerTax(CrmCustomerTaxForm crmCustomerTaxForm);

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
    List<LinkedHashMap<String, Object>> queryCrmCustomerTaxForExcel(Map<String, Object> paramMap);


}
