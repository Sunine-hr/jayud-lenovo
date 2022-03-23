package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置表 服务类
 *
 * @author jayud
 * @since 2022-03-23
 */
public interface ISystemConfigService extends IService<SystemConfig> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: systemConfig
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SystemConfig>
     **/
    IPage<SystemConfig> selectPage(SystemConfig systemConfig,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: systemConfig
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SystemConfig>
     **/
    List<SystemConfig> selectList(SystemConfig systemConfig);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-23
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-23
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySystemConfigForExcel(Map<String, Object> paramMap);


    Boolean saveOrUpdateSystemConfig(SystemConfig systemConfig);
}
