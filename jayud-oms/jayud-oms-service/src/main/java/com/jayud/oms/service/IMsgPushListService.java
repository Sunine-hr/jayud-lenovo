package com.jayud.oms.service;

import com.jayud.oms.model.po.MsgPushList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息推送列表 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
public interface IMsgPushListService extends IService<MsgPushList> {
    List<MsgPushList> getByCondition(MsgPushList msgPushList);
}
