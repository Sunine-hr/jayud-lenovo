package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OceanWaybill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
@Mapper
@Component
public interface OceanWaybillMapper extends BaseMapper<OceanWaybill> {

}
