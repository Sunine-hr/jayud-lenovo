package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.model.po.SupplierInfo;
import com.jayud.mall.mapper.SupplierInfoMapper;
import com.jayud.mall.service.ISupplierInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 供应商信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class SupplierInfoServiceImpl extends ServiceImpl<SupplierInfoMapper, SupplierInfo> implements ISupplierInfoService {

    @Autowired
    SupplierInfoMapper supplierInfoMapper;

    @Override
    public List<SupplierInfo> findSupplierInfo(SupplierInfoForm form) {
        QueryWrapper<SupplierInfo> queryWrapper = new QueryWrapper<>();
        String keyword = form.getKeyword();
        if(keyword != null && keyword != ""){
            queryWrapper.like("supplier_code", keyword).or().like("supplier_ch_name", keyword);
        }
        List<SupplierInfo> supplierInfos = supplierInfoMapper.selectList(queryWrapper);
        return supplierInfos;
    }
}
