package com.jayud.customs.mapper;

import com.jayud.customs.model.po.GeneralApiLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 报关请求接口历史数据表 Mapper 接口
 * </p>
 *
 * @author william.chen
 * @since 2020-09-10
 */
@Mapper
public interface GeneralApiLogMapper extends BaseMapper<GeneralApiLog> {

}
