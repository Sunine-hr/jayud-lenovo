package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OceanConfDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * (配载单)配置对应的报价与提单 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface OceanConfDetailMapper extends BaseMapper<OceanConfDetail> {

}
