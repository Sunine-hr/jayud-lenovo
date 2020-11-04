package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.finance.bo.QueryPaymentBillForm;
import com.jayud.finance.bo.QueryPaymentBillNumForm;
import com.jayud.finance.po.OrderPaymentBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.vo.OrderPaymentBillVO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface IOrderPaymentBillService extends IService<OrderPaymentBill> {

    /**
     * 查询应收出账单列表
     * @param form
     * @return
     */
    IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form);

    /**
     * 已生成对账单列表
     * @param form
     * @return
     */
    Map<String,Object> findPaymentBillNum(QueryPaymentBillNumForm form);

}
