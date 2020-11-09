package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.mapper.OrderPaymentBillDetailMapper;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderPaymentBillDetailServiceImpl extends ServiceImpl<OrderPaymentBillDetailMapper, OrderPaymentBillDetail> implements IOrderPaymentBillDetailService {

    @Override
    public IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(QueryPaymentBillDetailForm form) {
        return null;// TODO
    }
}
