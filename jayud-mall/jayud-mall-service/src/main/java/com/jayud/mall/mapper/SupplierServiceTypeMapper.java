package com.jayud.mall.mapper;

import com.jayud.mall.model.po.SupplierServiceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 供应商服务类型 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
@Mapper
@Component
public interface SupplierServiceTypeMapper extends BaseMapper<SupplierServiceType> {

}
