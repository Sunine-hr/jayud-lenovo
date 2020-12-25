package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.SupplierInfoServiceTypeRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 供应商-服务类型关联表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
@Mapper
@Component
public interface SupplierInfoServiceTypeRelationMapper extends BaseMapper<SupplierInfoServiceTypeRelation> {

}
