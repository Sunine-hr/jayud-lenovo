package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.CostInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.entity.InitComboxVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用名描述 服务类
 *
 * @author jayud
 * @since 2022-04-11
 */
public interface ICostInfoService extends IService<CostInfo> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: costInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CostInfo>
     **/
    IPage<CostInfo> selectPage(CostInfo costInfo,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: costInfo
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.CostInfo>
     **/
    List<CostInfo> selectList(CostInfo costInfo);



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
    List<LinkedHashMap<String, Object>> queryCostInfoForExcel(Map<String, Object> paramMap);

    /**
     * 获取费用类型
     *
     * @return
     */
    List<CostInfo> findCostInfo();

    Map<String, List<InitComboxVO>> initCostTypeByCostInfoCode();

    List<CostInfo> getCostInfoByStatus(String status);
}
