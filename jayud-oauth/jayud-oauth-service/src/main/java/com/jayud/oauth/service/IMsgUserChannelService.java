package com.jayud.oauth.service;

import com.jayud.oauth.model.po.MsgUserChannel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户消息渠道 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
public interface IMsgUserChannelService extends IService<MsgUserChannel> {

    /**
     *
     * @param userIds
     * @return
     */
    List<MsgUserChannel> getByUserIds(List<Long> userIds);
}
