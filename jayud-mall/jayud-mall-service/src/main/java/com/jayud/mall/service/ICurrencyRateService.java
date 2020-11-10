package com.jayud.mall.service;

import com.jayud.mall.model.bo.QueryCurrencyRateForm;
import com.jayud.mall.model.po.CurrencyRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CurrencyRateVO;

import java.util.List;

/**
 * <p>
 * 币种汇率 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
public interface ICurrencyRateService extends IService<CurrencyRate> {

    /**
     * 查询list
     * @param form
     * @return
     */
    List<CurrencyRateVO> findCurrencyRate(QueryCurrencyRateForm form);
}
