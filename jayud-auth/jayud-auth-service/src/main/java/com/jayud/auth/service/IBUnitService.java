package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.BUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 计量单位代码表 服务类
 *
 * @author jayud
 * @since 2022-03-25
 */
public interface IBUnitService extends IService<BUnit> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-25
     * @param: bUnit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BUnit>
     **/
    IPage<BUnit> selectPage(BUnit bUnit,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-25
     * @param: bUnit
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.BUnit>
     **/
    List<BUnit> selectList(BUnit bUnit);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-25
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-25
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-25
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryBUnitForExcel(Map<String, Object> paramMap);

    /**
     * 获取单位下拉
     * @return
     */
    List<String> getUnits();

}
