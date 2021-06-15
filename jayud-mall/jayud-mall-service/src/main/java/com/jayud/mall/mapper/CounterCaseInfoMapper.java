package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CounterCaseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 柜子箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
@Mapper
@Component
public interface CounterCaseInfoMapper extends BaseMapper<CounterCaseInfo> {

}
