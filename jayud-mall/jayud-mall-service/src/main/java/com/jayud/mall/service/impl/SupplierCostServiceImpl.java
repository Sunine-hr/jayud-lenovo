package com.jayud.mall.service.impl;

import com.jayud.mall.model.bo.SupplierCostForm;
import com.jayud.mall.model.po.SupplierCost;
import com.jayud.mall.mapper.SupplierCostMapper;
import com.jayud.mall.model.vo.SupplierCostVO;
import com.jayud.mall.service.ISupplierCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 供应商服务费用 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Service
public class SupplierCostServiceImpl extends ServiceImpl<SupplierCostMapper, SupplierCost> implements ISupplierCostService {

    @Autowired
    SupplierCostMapper supplierCostMapper;

    @Override
    public List<SupplierCostVO> findSupplierCost(SupplierCostForm form) {
        List<SupplierCostVO> supplierCostVOS = supplierCostMapper.findSupplierCost(form);
        return supplierCostVOS;
    }
}
