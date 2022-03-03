package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.bo.AddCrmCustomerAgreementSubForm;
import com.jayud.crm.model.po.CrmCustomerAgreementSub;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCustomerAgreementSubVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_协议管理_子协议(crm_customer_agreement_sub) 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCustomerAgreementSubService extends IService<CrmCustomerAgreementSub> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAgreementSub
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCustomerAgreementSub>
     **/
    IPage<CrmCustomerAgreementSubVO> selectPage(CrmCustomerAgreementSub crmCustomerAgreementSub,
                                                Integer currentPage,
                                                Integer pageSize,
                                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCustomerAgreementSub
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCustomerAgreementSub>
     **/
    List<CrmCustomerAgreementSub> selectList(CrmCustomerAgreementSub crmCustomerAgreementSub);



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
    List<LinkedHashMap<String, Object>> queryCrmCustomerAgreementSubForExcel(Map<String, Object> paramMap);


    /**
     * 自动生成编号
     * @param caId
     * @return
     */
    Map<String, Object> autoGenerateNum(Long caId);

    /**
     * 新增/编辑
     * @param form
     */
    void saveOrUpdate(AddCrmCustomerAgreementSubForm form);

    /**
     * 是否存在编号
     * @param agreementCode
     * @return
     */
    boolean exitNumber(String agreementCode);
}
