package com.jayud.scm.mapper;

import com.jayud.scm.model.po.HubShipping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 * 出库单主表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Mapper
public interface HubShippingMapper extends BaseMapper<HubShipping> {

    void automaticGenerationHubShipping(Map<String, Object> map);
}
