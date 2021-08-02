package com.jayud.oms.mapper;

import com.jayud.oms.model.po.MessagePushTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 消息推送模板 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
public interface MessagePushTemplateMapper extends BaseMapper<MessagePushTemplate> {

    Map<String, Object> executeTemplateSQL(@Param("sqlSelect") String sqlSelect);
}
