package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.AirPort;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 空运港口表 服务类
 *
 * @author jayud
 * @since 2022-03-24
 */
public interface IAirPortService extends IService<AirPort> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-24
     * @param: airPort
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.AirPort>
     **/
    IPage<AirPort> selectPage(AirPort airPort,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-24
     * @param: airPort
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.AirPort>
     **/
    List<AirPort> selectList(AirPort airPort);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-24
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-24
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-24
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryAirPortForExcel(Map<String, Object> paramMap);

    /**
     * 根据code获取空运港口
     * @param code
     * @return
     */
    AirPort isCodeExistence(String code);

    /**
     * 根据code获取空运港口
     * @param name
     * @return
     */
    AirPort isNameExistence(String name);

    void saveAirPort(AirPort airPort);
}
