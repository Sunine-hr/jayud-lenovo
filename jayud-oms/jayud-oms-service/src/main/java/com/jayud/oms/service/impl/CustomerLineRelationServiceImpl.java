package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.CustomerLineRelationMapper;
import com.jayud.oms.model.po.CustomerLineRelation;
import com.jayud.oms.service.ICustomerLineRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户线路客户列表 服务实现类
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Service
public class CustomerLineRelationServiceImpl extends ServiceImpl<CustomerLineRelationMapper, CustomerLineRelation> implements ICustomerLineRelationService {

    /**
     * 根据客户线路ID删除客户线路客户列表
     * @param customerLineId
     */
    @Override
    public void deleteByCustomerLineId(Long customerLineId) {
        this.remove(new QueryWrapper<CustomerLineRelation>().lambda().eq(CustomerLineRelation::getCustomerLineId, customerLineId));
    }

    /**
     * 根据客户线路ID查询客户线路客户列表
     * @param id
     * @return
     */
    @Override
    public List<CustomerLineRelation> getListByCustomerLineId(Long id) {
        return this.baseMapper.getListByCustomerLineId(id);
    }
}
