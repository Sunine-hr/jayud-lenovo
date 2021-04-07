package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.oms.model.bo.AddSupplierInfoForm;
import com.jayud.oms.model.po.SupplierRelaLegal;
import com.jayud.oms.mapper.SupplierRelaLegalMapper;
import com.jayud.oms.service.ISupplierRelaLegalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商对应法人主体表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-04-06
 */
@Service
public class SupplierRelaLegalServiceImpl extends ServiceImpl<SupplierRelaLegalMapper, SupplierRelaLegal> implements ISupplierRelaLegalService {

    @Override
    public Boolean saveSupplierRelLegal(AddSupplierInfoForm form) {
        //保存关系时先清空现有的
        QueryWrapper removeWrapper = new QueryWrapper();
        removeWrapper.eq("supplier_info_id", form.getId());
        remove(removeWrapper);

        List<SupplierRelaLegal> supplierRelaLegals = new ArrayList<>();
        for (Long legalEntityId : form.getLegalEntityIds()) {
            SupplierRelaLegal supplierRelaLegal = new SupplierRelaLegal();
            supplierRelaLegal.setSupplierInfoId(form.getId());
            supplierRelaLegal.setLegalEntityId(legalEntityId);
            supplierRelaLegal.setCreatedUser(UserOperator.getToken());
            supplierRelaLegals.add(supplierRelaLegal);
        }
        return saveBatch(supplierRelaLegals);
    }

    @Override
    public List<Long> getList(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("supplier_info_id",id);
        List<SupplierRelaLegal> list = this.list(queryWrapper);
        List<Long> longs = new ArrayList<>();
        for (SupplierRelaLegal supplierRelaLegal : list) {
            longs.add(supplierRelaLegal.getLegalEntityId());
        }
        return longs;
    }
}
