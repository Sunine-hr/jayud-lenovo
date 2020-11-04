package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.constant.CommonConstant;
import com.jayud.finance.bo.QueryPaymentBillForm;
import com.jayud.finance.bo.QueryPaymentBillNumForm;
import com.jayud.finance.mapper.OrderPaymentBillMapper;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.vo.OrderPaymentBillNumVO;
import com.jayud.finance.vo.OrderPaymentBillVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderPaymentBillServiceImpl extends ServiceImpl<OrderPaymentBillMapper, OrderPaymentBill> implements IOrderPaymentBillService {

    @Override
    public IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form) {
        //定义分页参数
        Page<OrderPaymentBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("oi.legal_name"));
        IPage<OrderPaymentBillVO> pageInfo = baseMapper.findPaymentBillByPage(page, form);
        return pageInfo;
    }

    @Override
    public Map<String, Object> findPaymentBillNum(QueryPaymentBillNumForm form) {
        List<OrderPaymentBillNumVO> resultList = baseMapper.findPaymentBillNum(form);
        Map<String, Object> result = new HashMap<>();
        result.put(CommonConstant.LIST,resultList);
        result.put(CommonConstant.BILL_NUM_TOTAL,resultList.stream().collect(Collectors.summarizingInt(OrderPaymentBillNumVO::getBillNum)));//订单数合计
        result.put(CommonConstant.RMB_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getRmb).reduce(BigDecimal.ZERO,BigDecimal::add));//人民币合计
        result.put(CommonConstant.DOLLAR_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getDollar).reduce(BigDecimal.ZERO,BigDecimal::add));//美元合计
        result.put(CommonConstant.EURO_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getEuro).reduce(BigDecimal.ZERO,BigDecimal::add));//欧元合计
        result.put(CommonConstant.HK_DOLLAR_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getHKDollar).reduce(BigDecimal.ZERO,BigDecimal::add));//港币合计
        result.put(CommonConstant.LOCAL_AMOUNT_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add));//本币金额合计
        result.put(CommonConstant.HE_XIAO_AMOUNT,resultList.stream().map(OrderPaymentBillNumVO::getHeXiaoAmount).reduce(BigDecimal.ZERO,BigDecimal::add));//已收金额，即财务已核销金额合计
        return result;
    }
}
