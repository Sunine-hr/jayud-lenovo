package com.jayud.oms.mapper;

import com.jayud.oms.model.po.CustomerLineRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 客户线路客户列表 Mapper 接口
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
public interface CustomerLineRelationMapper extends BaseMapper<CustomerLineRelation> {

    List<CustomerLineRelation> getListByCustomerLineId(@Param("id") Long id);
}
