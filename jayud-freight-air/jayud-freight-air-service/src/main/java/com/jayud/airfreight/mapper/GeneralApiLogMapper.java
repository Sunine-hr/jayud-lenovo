package com.jayud.airfreight.mapper;

import com.jayud.airfreight.model.po.GeneralApiLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 经过api模块进行操作的接口请求历史数据表 Mapper 接口
 * </p>
 *
 * @author william.chen
 * @since 2020-09-17
 */
@Mapper
public interface GeneralApiLogMapper extends BaseMapper<GeneralApiLog> {

}
