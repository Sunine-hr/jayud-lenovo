package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CounterCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 货柜对应运单箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-22
 */
@Mapper
@Component
public interface CounterCaseMapper extends BaseMapper<CounterCase> {

}
