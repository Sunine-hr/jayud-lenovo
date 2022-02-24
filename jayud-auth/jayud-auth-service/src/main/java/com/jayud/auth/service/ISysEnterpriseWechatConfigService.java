package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysEnterpriseDingdingConfig;
import com.jayud.auth.model.po.SysEnterpriseWechatConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统企业微信配置表 服务类
 *
 * @author jayud
 * @since 2022-02-24
 */
public interface ISysEnterpriseWechatConfigService extends IService<SysEnterpriseWechatConfig> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-24
     * @param: sysEnterpriseWechatConfig
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysEnterpriseWechatConfig>
     **/
    IPage<SysEnterpriseWechatConfig> selectPage(SysEnterpriseWechatConfig sysEnterpriseWechatConfig,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-24
     * @param: sysEnterpriseWechatConfig
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysEnterpriseWechatConfig>
     **/
    List<SysEnterpriseWechatConfig> selectList(SysEnterpriseWechatConfig sysEnterpriseWechatConfig);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-24
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-24
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);

    /**
     * @description 保存数据
     * @author  ciro
     * @date   2022/2/24 12:52
     * @param: sysEnterpriseWechatConfig
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult saveConfig(SysEnterpriseWechatConfig sysEnterpriseWechatConfig);

    /**
     * @description 根据租户编码查询
     * @author  ciro
     * @date   2022/2/24 12:52
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysEnterpriseWechatConfig>
     **/
    BaseResult<SysEnterpriseWechatConfig> selectByTenantCode(String tenantCode);


}
