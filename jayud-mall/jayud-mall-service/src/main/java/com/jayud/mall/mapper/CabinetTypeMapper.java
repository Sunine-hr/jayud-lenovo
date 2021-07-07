package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CabinetType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 柜型基本信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-01-07
 */
@Mapper
@Component
public interface CabinetTypeMapper extends BaseMapper<CabinetType> {

}
