package com.jayud.finance.service;

import com.jayud.finance.po.CustomsPayable;
import com.jayud.finance.po.CustomsReceivable;

import java.util.List;

/**
 * 报关的应收应付信息向金蝶推送数据
 */
public interface CustomsFinancePushService {
    Boolean pushReceivable(List<CustomsReceivable> customsReceivable);

    Boolean pushPayable(List<CustomsPayable> customsPayableForms);
}
