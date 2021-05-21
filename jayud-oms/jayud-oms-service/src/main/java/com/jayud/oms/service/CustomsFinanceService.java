package com.jayud.oms.service;


import com.jayud.oms.model.po.CustomsReceivable;

import java.util.List;

/**
 * 报关的应收信息处理
 */
public interface CustomsFinanceService {
    /**
     * 推送应收
     *
     * @param customsReceivable
     * @return
     */
    Boolean saveReceivable(List<CustomsReceivable> customsReceivable);

}
