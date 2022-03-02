package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.crm.model.po.CrmCreditVisitRole;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户走访记录-拜访人员(员工表)关联表 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmCreditVisitRoleService extends IService<CrmCreditVisitRole> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisitRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditVisitRole>
     **/
    IPage<CrmCreditVisitRole> selectPage(CrmCreditVisitRole crmCreditVisitRole,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisitRole
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmCreditVisitRole>
     **/
    List<CrmCreditVisitRole> selectList(CrmCreditVisitRole crmCreditVisitRole);



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




}
