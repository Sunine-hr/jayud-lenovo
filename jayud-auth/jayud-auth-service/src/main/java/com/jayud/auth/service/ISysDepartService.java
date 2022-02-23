package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.QuerySysDeptForm;
import com.jayud.auth.model.po.SysDepart;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织机构表 服务类
 *
 * @author jayud
 * @since 2022-02-22
 */
public interface ISysDepartService extends IService<SysDepart> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: sysDepart
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysDepart>
     **/
    IPage<SysDepart> selectPage(SysDepart sysDepart,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-22
     * @param: sysDepart
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysDepart>
     **/
    List<SysDepart> selectList(SysDepart sysDepart);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-22
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);


    /**
     * 根据查询条件，查询部门树
     * @param form
     * @return
     */
    List<SysDepart> selectDeptTree(QuerySysDeptForm form);

    /**
     * 保存组织or部门
     * @param depart
     */
    void saveSysDepart(SysDepart depart);

    /**
     * 获取上级组织
     * @param form
     * @return
     */
    List<SysDepart> selectSuperiorOrganization(QuerySysDeptForm form);
}
