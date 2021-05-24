package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.BusinessLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 业务日志表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-24
 */
@Mapper
@Component
public interface BusinessLogMapper extends BaseMapper<BusinessLog> {

}
