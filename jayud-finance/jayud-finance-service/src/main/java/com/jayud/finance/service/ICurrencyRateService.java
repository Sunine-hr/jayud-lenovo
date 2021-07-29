package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.AddCurrencyManageForm;
import com.jayud.finance.bo.EditCurrencyRateForm;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.vo.CurrencyRateVO;
import com.jayud.finance.vo.InitComboxStrVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 币种汇率 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-12
 */
public interface ICurrencyRateService extends IService<CurrencyRate> {

    /**
     * 应付对账单分页查询
     *
     * @param form
     * @return
     */
    IPage<CurrencyRateVO> findCurrencyRateByPage(QueryCurrencyRateForm form);


    /**
     * 新增汇率
     *
     * @param form
     * @return
     */
    CommonResult saveCurrencyRate(AddCurrencyManageForm form);

    /**
     * 编辑汇率
     *
     * @param form
     * @return
     */
    Boolean editCurrencyRate(EditCurrencyRateForm form);

    /**
     * 根据原始币种和兑换币种获取汇率
     *
     *
     * @param code
     * @param oCode
     * @param dCode
     * @return
     */
    BigDecimal getExchangeRate(String oCode, String dCode,String accountTerm);

    /**
     * 根据币种CODE查名称描述
     *
     * @param code
     * @return
     */
    String getNameByCode(String code);

    /**
     * 核销时初始化下拉币种
     *
     * @param currencyName
     * @return
     */
    List<InitComboxStrVO> initHeXiaoCurrency(String currencyName);

    /**
     * 根据原始币种和兑换币种获取汇率
     *
     * @return
     */
    Map<String, BigDecimal> getExchangeRates(String dcCode, String month);
}
