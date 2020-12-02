package com.jayud.airfreight.mapper;

import com.jayud.airfreight.model.po.AirPort;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 飞机港口地址表 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2020-12-02
 */
@Mapper
public interface AirPortMapper extends BaseMapper<AirPort> {

}
