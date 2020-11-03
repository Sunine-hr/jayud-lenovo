package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.TransportWay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运输方式 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface TransportWayMapper extends BaseMapper<TransportWay> {

}
