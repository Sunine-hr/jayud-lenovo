package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.CrmCreditVisitForm;
import com.jayud.crm.model.po.CrmCreditVisit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.crm.model.vo.CrmCreditVisitVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_客户_客户走访记录 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmCreditVisitService extends IService<CrmCreditVisit> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditVisit>
     **/
    IPage<CrmCreditVisitVO> selectPage(CrmCreditVisitForm crmCreditVisitForm,
                                       Integer currentPage,
                                       Integer pageSize,
                                       HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisit
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditVisit>
     **/
    List<CrmCreditVisit> selectList(CrmCreditVisit crmCreditVisit);

    BaseResult saveOrUpdateCrmCreditVisit(CrmCreditVisitForm crmCreditVisitForm);

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
    void logicDel(List<Long> ids);


    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    CrmCreditVisitVO findCrmCreditVisitIdOne(Long id);

}
