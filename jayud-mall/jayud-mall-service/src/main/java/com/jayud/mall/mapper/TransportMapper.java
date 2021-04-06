package com.jayud.mall.mapper;

import com.jayud.mall.model.po.Transport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运输管理表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
@Mapper
@Component
public interface TransportMapper extends BaseMapper<Transport> {

}
