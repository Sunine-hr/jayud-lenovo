package com.jayud.mall.service;

import com.jayud.mall.model.bo.TradingRecordCZForm;
import com.jayud.mall.model.bo.TradingRecordForm;
import com.jayud.mall.model.bo.TradingRecordQueryForm;
import com.jayud.mall.model.po.TradingRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.TradingRecordVO;

import java.util.List;

/**
 * <p>
 * 交易记录表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
public interface ITradingRecordService extends IService<TradingRecord> {

    /**
     * 客户充值
     * @param form
     */
    void customerPay(TradingRecordForm form);

    /**
     * 查询充值记录
     * @param form
     * @return
     */
    List<TradingRecordVO> findTradingRecordByCz(TradingRecordCZForm form);

    /**
     * 查询充值、支付记录
     * @param form
     * @return
     */
    List<TradingRecordVO> findTradingRecord(TradingRecordQueryForm form);
}
