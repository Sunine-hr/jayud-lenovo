package com.jayud.oms.service;

import com.jayud.oms.model.po.MsgPushRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 消息推送记录 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
public interface IMsgPushRecordService extends IService<MsgPushRecord> {

    /**
     * 推送任务
     */
    public void createPushTask(String triggerStatus, Map<String, Object> sqlParam,
                               LocalDateTime now, Map<String, Object> otherParam);

}
