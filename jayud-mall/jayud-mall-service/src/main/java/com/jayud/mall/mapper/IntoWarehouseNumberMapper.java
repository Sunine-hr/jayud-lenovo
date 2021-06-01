package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.IntoWarehouseNumber;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 进仓单号表(取单号) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-01
 */
@Mapper
@Component
public interface IntoWarehouseNumberMapper extends BaseMapper<IntoWarehouseNumber> {

    /**
     * 获取进仓单号
     * @return
     */
    String getWarehouseNo();
}
