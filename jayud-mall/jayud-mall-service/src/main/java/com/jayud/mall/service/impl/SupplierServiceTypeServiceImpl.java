package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.po.SupplierServiceType;
import com.jayud.mall.mapper.SupplierServiceTypeMapper;
import com.jayud.mall.model.vo.SupplierServiceTypeVO;
import com.jayud.mall.service.ISupplierServiceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 供应商服务类型 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
@Service
public class SupplierServiceTypeServiceImpl extends ServiceImpl<SupplierServiceTypeMapper, SupplierServiceType> implements ISupplierServiceTypeService {

    @Autowired
    SupplierServiceTypeMapper supplierServiceTypeMapper;

    @Override
    public List<SupplierServiceTypeVO> findSupplierServiceType() {
        QueryWrapper<SupplierServiceType> queryWrapper = new QueryWrapper<>();
        List<SupplierServiceType> list = this.list(queryWrapper);
        List<SupplierServiceTypeVO> supplierServiceTypeVOS = ConvertUtil.convertList(list, SupplierServiceTypeVO.class);
        return supplierServiceTypeVOS;
    }
}
