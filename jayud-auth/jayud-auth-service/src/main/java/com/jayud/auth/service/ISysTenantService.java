package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.SysTenantForm;
import com.jayud.auth.model.po.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 多租户信息表 服务类
 *
 * @author jayud
 * @since 2022-02-22
 */
public interface ISysTenantService extends IService<SysTenant> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: sysTenant
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysTenant>
     **/
    IPage<SysTenant> selectPage(SysTenant sysTenant,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-22
     * @param: sysTenant
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysTenant>
     **/
    List<SysTenant> selectList(SysTenant sysTenant);



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
     * @description 保存租户
     * @author  ciro
     * @date   2022/2/22 10:38
     * @param: sysTenantForm
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult saveTenant(SysTenantForm sysTenantForm);


    /**
     * @description 创建租户初始化数据
     * @author  ciro
     * @date   2022/2/23 13:58
     * @param: sysTenantForm
     * @return: void
     **/
    void initCreateTenant(SysTenantForm sysTenantForm);

    /**
     * @description 根据id查询
     * @author  ciro
     * @date   2022/2/24 10:23
     * @param: id
     * @return: com.jayud.auth.model.bo.SysTenantForm
     **/
    SysTenantForm selectByTenantId(Long id);

    /**
     * @description 根据租户编码查询
     * @author  ciro
     * @date   2022/2/25 10:49
     * @param: tenantCode
     * @return: com.jayud.auth.model.po.SysTenant
     **/
    SysTenant selectByTenantCode(String tenantCode);

    /**
     * @description 根据id集合删除
     * @author  ciro
     * @date   2022/2/25 13:42
     * @param: deleteForm
     * @return: void
     **/
    void delByIds(DeleteForm deleteForm);

}
