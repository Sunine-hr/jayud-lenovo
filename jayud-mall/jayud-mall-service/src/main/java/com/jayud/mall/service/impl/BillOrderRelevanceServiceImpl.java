package com.jayud.mall.service.impl;

import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.po.BillOrderRelevance;
import com.jayud.mall.mapper.BillOrderRelevanceMapper;
import com.jayud.mall.service.IBillOrderRelevanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 提单关联订单(任务通知表) 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-17
 */
@Service
public class BillOrderRelevanceServiceImpl extends ServiceImpl<BillOrderRelevanceMapper, BillOrderRelevance> implements IBillOrderRelevanceService {

    @Autowired
    BillOrderRelevanceMapper billOrderRelevanceMapper;

    @Override
    public List<BillOrderRelevance> findBillOrderRelevanceByBillId(Long billId) {
        return billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBillOrderRelevance(List<BillOrderRelevance> form) {
        List<BillOrderRelevance> billOrderRelevances = ConvertUtil.convertList(form, BillOrderRelevance.class);
        this.saveOrUpdateBatch(billOrderRelevances);
    }


}