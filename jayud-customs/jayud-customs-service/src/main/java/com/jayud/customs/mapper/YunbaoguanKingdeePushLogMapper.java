package com.jayud.customs.mapper;

import com.jayud.customs.model.po.YunbaoguanKingdeePushLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 云报关到金蝶推送日志 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-22
 */
@Mapper
@Component
public interface YunbaoguanKingdeePushLogMapper extends BaseMapper<YunbaoguanKingdeePushLog> {

}
