package com.jayud.mall.mapper;

import com.jayud.mall.model.po.SupplierCost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 供应商服务费用 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Mapper
@Component
public interface SupplierCostMapper extends BaseMapper<SupplierCost> {

}
