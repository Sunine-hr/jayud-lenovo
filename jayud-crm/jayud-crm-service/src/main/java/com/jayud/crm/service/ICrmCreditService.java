package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.bo.AddCrmCreditForm;
import com.jayud.crm.model.po.CrmCredit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCreditVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_额度_额度总量(crm_credit) 服务类
 *
 * @author jayud
 * @since 2022-03-03
 */
public interface ICrmCreditService extends IService<CrmCredit> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCredit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCredit>
     **/
    IPage<CrmCreditVO> selectPage(CrmCredit crmCredit,
                                  Integer currentPage,
                                  Integer pageSize,
                                  HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-03
     * @param: crmCredit
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCredit>
     **/
    List<CrmCredit> selectList(CrmCredit crmCredit);



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
    List<LinkedHashMap<String, Object>> queryCrmCreditForExcel(Map<String, Object> paramMap);


    /**
     * 新增/编辑
     * @param form
     */
    void saveOrUpdate(AddCrmCreditForm form);

    /**
     * 是否存在相同值
     * @param creditValue
     * @return
     */
    boolean exitNumber(String creditValue);
}
