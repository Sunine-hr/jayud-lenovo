package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oauth.model.po.MsgUserChannel;
import com.jayud.oauth.mapper.MsgUserChannelMapper;
import com.jayud.oauth.service.IMsgUserChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户消息渠道 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@Service
public class MsgUserChannelServiceImpl extends ServiceImpl<MsgUserChannelMapper, MsgUserChannel> implements IMsgUserChannelService {

    @Override
    public List<MsgUserChannel> getByUserIds(List<Long> userIds) {
        QueryWrapper<MsgUserChannel> condition = new QueryWrapper<>();
        condition.lambda().in(MsgUserChannel::getUserId, userIds);
        return this.baseMapper.selectList(condition);
    }
}
