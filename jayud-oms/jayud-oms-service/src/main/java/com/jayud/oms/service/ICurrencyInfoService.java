package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.CurrencyInfo;
import com.jayud.oms.model.vo.CurrencyInfoVO;

import java.util.List;

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
     * @return
     */
    List<CurrencyInfoVO> findCurrencyInfo();


}
