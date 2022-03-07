package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.bo.AddCrmCustomerAgreementForm;
import com.jayud.crm.model.po.CrmCustomerAgreement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCustomerAgreementVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_协议管理(crm_customer_agreement) 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmCustomerAgreementService extends IService<CrmCustomerAgreement> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerAgreement
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerAgreement>
     **/
    IPage<CrmCustomerAgreementVO> selectPage(CrmCustomerAgreement crmCustomerAgreement,
                                             Integer currentPage,
                                             Integer pageSize,
                                             HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCustomerAgreement
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerAgreement>
     **/
    List<CrmCustomerAgreementVO> selectList(CrmCustomerAgreement crmCustomerAgreement);



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
    List<LinkedHashMap<String, Object>> queryCrmCustomerAgreementForExcel(Map<String, Object> paramMap);

    /**
     * 新增/编辑
     * @param form
     */
    void saveOrUpdate(AddCrmCustomerAgreementForm form);

    /**
     * 是否协议编号存在
     * @param agreementCode
     * @return
     */
    boolean exitNumber(String agreementCode);

}
