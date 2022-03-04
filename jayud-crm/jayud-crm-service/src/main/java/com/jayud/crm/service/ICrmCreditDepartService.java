package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.bo.AddCrmCreditDepartForm;
import com.jayud.crm.model.po.CrmCreditDepart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCreditDepartVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_额度_部门额度授信管理(crm_credit_depart) 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCreditDepartService extends IService<CrmCreditDepart> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCreditDepart
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditDepart>
     **/
    IPage<CrmCreditDepartVO> selectPage(CrmCreditDepart crmCreditDepart,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCreditDepart
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditDepart>
     **/
    List<CrmCreditDepart> selectList(CrmCreditDepart crmCreditDepart);



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
    List<LinkedHashMap<String, Object>> queryCrmCreditDepartForExcel(Map<String, Object> paramMap);


    void saveOrUpdate(AddCrmCreditDepartForm form);

    BigDecimal calculationRemainingCreditLine(String creditId, String tenantCode);
}
