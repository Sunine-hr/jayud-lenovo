package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.CostType;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 费用类别 服务类
 *
 * @author jayud
 * @since 2022-04-11
 */
public interface ICostTypeService extends IService<CostType> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: costType
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CostType>
     **/
    IPage<CostType> selectPage(CostType costType,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: costType
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.CostType>
     **/
    List<CostType> selectList(CostType costType);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-04-11
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-11
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCostTypeForExcel(Map<String, Object> paramMap);

    /**
     * 查询所有启用费用类别
     * @return
     */
    List<CostType> getEnableCostType();
}
