package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.CurrencyInfo;
import com.jayud.oms.model.vo.CurrencyInfoVO;
import com.jayud.oms.model.vo.InitComboxStrVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 币种 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
public interface ICurrencyInfoService extends IService<CurrencyInfo> {

    /**
     * 获取币种信息
     *
     * @param createdTimeStr
     * @return
     */
    List<CurrencyInfoVO> findCurrencyInfo(String createdTimeStr);

    /**
     * 根据币种code获取币种信息
     */
    List<CurrencyInfo> getByCodes(Set<String> currencyCodes);

    /**
     * 查询下拉币种
     */
    public List<InitComboxStrVO> initCurrencyInfo();
}
