package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysPost;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位表 服务类
 *
 * @author jayud
 * @since 2022-02-22
 */
public interface ISysPostService extends IService<SysPost> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: sysPost
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysPost>
     **/
    IPage<SysPost> selectPage(SysPost sysPost,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-22
     * @param: sysPost
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysPost>
     **/
    List<SysPost> selectList(SysPost sysPost);



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




}
