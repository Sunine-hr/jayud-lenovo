package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysTenantRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户规则配置 服务类
 *
 * @author jayud
 * @since 2022-03-05
 */
public interface ISysTenantRoleService extends IService<SysTenantRole> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-05
     * @param: sysTenantRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysTenantRole>
     **/
    IPage<SysTenantRole> selectPage(SysTenantRole sysTenantRole,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-05
     * @param: sysTenantRole
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysTenantRole>
     **/
    List<SysTenantRole> selectList(SysTenantRole sysTenantRole);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-05
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-05
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-05
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySysTenantRoleForExcel(Map<String, Object> paramMap);

    /**
     * @description 根据租户编码获取规则
     * @author  ciro
     * @date   2022/3/5 11:32
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysTenantRole>
     **/
    BaseResult<SysTenantRole> selectByTenatCode(String tenantCode);
}
