package com.jayud.oms.service;

import com.jayud.oms.model.po.CustomerLineRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户线路客户列表 服务类
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
public interface ICustomerLineRelationService extends IService<CustomerLineRelation> {

    /**
     * 根据客户线路ID删除客户线路客户列表
     * @param customerLineId
     */
    void deleteByCustomerLineId(Long customerLineId);

    /**
     * 根据客户线路ID查询客户线路客户列表
     * @param id
     * @return
     */
    List<CustomerLineRelation> getListByCustomerLineId(Long id);
}
