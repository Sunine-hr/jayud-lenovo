package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.bo.AddCrmContractQuotationForm;
import com.jayud.crm.model.po.CrmContractQuotation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmContractQuotationVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同报价 服务类
 *
 * @author jayud
 * @since 2022-03-01
 */
public interface ICrmContractQuotationService extends IService<CrmContractQuotation> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: crmContractQuotation
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmContractQuotation>
     **/
    IPage<CrmContractQuotationVO> selectPage(CrmContractQuotation crmContractQuotation,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: crmContractQuotation
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmContractQuotation>
     **/
    List<CrmContractQuotation> selectList(CrmContractQuotation crmContractQuotation);



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
    List<LinkedHashMap<String, Object>> queryCrmContractQuotationForExcel(Map<String, Object> paramMap);

    /**
     * 新增/编辑合同报价
     * @param form
     * @return
     */
    Long saveOrUpdate(AddCrmContractQuotationForm form);

    /**
     * 是否存在合同报价编号
     * @param number
     * @return
     */
    boolean exitNumber(String number);

    String autoGenerateNum();

    CrmContractQuotationVO getEditInfoById(Long id);
}
