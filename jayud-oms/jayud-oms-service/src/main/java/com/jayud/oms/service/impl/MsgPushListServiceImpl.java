package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.MsgPushList;
import com.jayud.oms.mapper.MsgPushListMapper;
import com.jayud.oms.service.IMsgPushListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息推送列表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@Service
public class MsgPushListServiceImpl extends ServiceImpl<MsgPushListMapper, MsgPushList> implements IMsgPushListService {

    @Override
    public List<MsgPushList> getByCondition(MsgPushList msgPushList) {
        return this.baseMapper.selectList(new QueryWrapper<>(msgPushList));
    }
}
