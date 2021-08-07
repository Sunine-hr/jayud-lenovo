package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryMessagePushTemplateForm;
import com.jayud.oms.model.po.MessagePushTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.MessagePushTemplateVO;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface MessagePushTemplateMapper extends BaseMapper<MessagePushTemplate> {

    Map<String, Object> executeTemplateSQL(@Param("sqlSelect") String sqlSelect);

    IPage<MessagePushTemplateVO> findByPage(@Param("page") Page<MessagePushTemplateVO> page, @Param("form") QueryMessagePushTemplateForm form);
}
