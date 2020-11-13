package com.jayud.finance.service.impl;

import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.mapper.OrderBillCostTotalMapper;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.finance.vo.OrderBillCostTotalVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderBillCostTotalServiceImpl extends ServiceImpl<OrderBillCostTotalMapper, OrderBillCostTotal> implements IOrderBillCostTotalService {

    @Override
    public List<OrderBillCostTotalVO> findOrderBillCostTotal(List<Long> costIds) {
        return baseMapper.findOrderBillCostTotal(costIds);
    }
}
