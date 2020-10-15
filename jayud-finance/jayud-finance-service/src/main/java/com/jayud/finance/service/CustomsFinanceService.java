package com.jayud.finance.service;

import com.jayud.finance.po.CustomsPayable;
import com.jayud.finance.po.CustomsReceivable;
import com.jayud.finance.po.YunbaoguanPushProperties;

import java.util.List;

/**
 * 报关的应收应付信息向金蝶推送数据
 */
public interface CustomsFinanceService {
    /**
     * 推送应收
     *
     * @param customsReceivable
     * @return
     */
    Boolean pushReceivable(List<CustomsReceivable> customsReceivable, YunbaoguanPushProperties properties);

    /**
     * 推送应付
     *
     * @param customsPayableForms
     * @return
     */
    Boolean pushPayable(List<CustomsPayable> customsPayableForms,YunbaoguanPushProperties properties);

    /**
     * 删除金蝶指定订单
     *
     * @return
     */

    YunbaoguanPushProperties removeSpecifiedInvoice(YunbaoguanPushProperties properties);

}
