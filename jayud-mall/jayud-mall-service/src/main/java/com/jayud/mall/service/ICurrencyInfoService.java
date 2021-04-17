package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.QueryCurrencyInfoForm;
import com.jayud.mall.model.po.CurrencyInfo;
import com.jayud.mall.model.vo.CurrencyInfoVO;

import java.util.List;

/**
 * <p>
 * 币种 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
public interface ICurrencyInfoService extends IService<CurrencyInfo> {

    /**
     * 查询list
      * @param form
     * @return
     */
    List<CurrencyInfoVO> findCurrencyInfo(QueryCurrencyInfoForm form);
}
