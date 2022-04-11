package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.CurrencyRate;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 币种汇率 服务类
 *
 * @author jayud
 * @since 2022-04-11
 */
public interface ICurrencyRateService extends IService<CurrencyRate> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: currencyRate
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CurrencyRate>
     **/
    IPage<CurrencyRate> selectPage(CurrencyRate currencyRate,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: currencyRate
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.CurrencyRate>
     **/
    List<CurrencyRate> selectList(CurrencyRate currencyRate);



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
    List<LinkedHashMap<String, Object>> queryCurrencyRateForExcel(Map<String, Object> paramMap);


}
