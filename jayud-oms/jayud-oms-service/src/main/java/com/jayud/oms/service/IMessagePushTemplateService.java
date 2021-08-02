package com.jayud.oms.service;

import com.jayud.oms.model.bo.AddMessagePushTemplateForm;
import com.jayud.oms.model.po.MessagePushTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息推送模板 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
public interface IMessagePushTemplateService extends IService<MessagePushTemplate> {


    void saveOrUpdate(AddMessagePushTemplateForm form);


    Map<String, Object> executeTemplateSQL(String sqlSelect);

    List<MessagePushTemplate> getByCondition(MessagePushTemplate messagePushTemplate);

    boolean checkUnique(AddMessagePushTemplateForm form);
}
