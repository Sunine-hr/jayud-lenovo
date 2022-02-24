package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysUrl;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统链接表 服务类
 *
 * @author jayud
 * @since 2022-02-21
 */
public interface ISysUrlService extends IService<SysUrl> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUrl
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysUrl>
     **/
    IPage<SysUrl> selectPage(SysUrl sysUrl,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-21
     * @param: sysUrl
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysUrl>
     **/
    List<SysUrl> selectList(SysUrl sysUrl);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-21
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-21
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);

    /**
     * @description 保存系统链接信息
     * @author  ciro
     * @date   2022/2/22 11:20
     * @param: sysUrl
     * @return: com.jayud.common.BaseResult
     **/
    BaseResult saveUrl(SysUrl sysUrl);

    /**
     * @description 根据租户编码获取关联系统信息
     * @author  ciro
     * @date   2022/2/24 9:06
     * @param: tenantCode
     * @return: java.util.List<com.jayud.auth.model.po.SysUrl>
     **/
    List<SysUrl> getSystemByTenantCode(String tenantCode);


}
