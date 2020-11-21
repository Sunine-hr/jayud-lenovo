package com.jayud.mall.mapper;

import com.jayud.mall.model.po.NumberGenerated;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 单号生成器 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-21
 */
@Mapper
@Component
public interface NumberGeneratedMapper extends BaseMapper<NumberGenerated> {

}
