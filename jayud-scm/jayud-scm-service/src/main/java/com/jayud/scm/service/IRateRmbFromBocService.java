package com.jayud.scm.service;

import com.jayud.scm.model.po.RateRmbFromBoc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 中行汇率表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
public interface IRateRmbFromBocService extends IService<RateRmbFromBoc> {

    List<RateRmbFromBoc> getRateRmbFromBocByTimeAndCurrencyName(LocalDateTime publishTime, String currencyName);
}
