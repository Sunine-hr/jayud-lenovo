package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysArea;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统-中国行政地区表 服务类
 *
 * @author jayud
 * @since 2022-02-26
 */
public interface ISysAreaService extends IService<SysArea> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-26
     * @param: sysArea
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysArea>
     **/
    IPage<SysArea> selectPage(SysArea sysArea,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-26
     * @param: sysArea
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysArea>
     **/
    List<SysArea> selectList(SysArea sysArea);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-26
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-26
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);




}
