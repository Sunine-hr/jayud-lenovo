package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.SystemConf;
import com.jayud.oms.mapper.SystemConfMapper;
import com.jayud.oms.service.ISystemConfService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息系统配置 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-04
 */
@Service
public class SystemConfServiceImpl extends ServiceImpl<SystemConfMapper, SystemConf> implements ISystemConfService {

    @Override
    public List<SystemConf> getByCondition(SystemConf systemConf) {
        QueryWrapper<SystemConf> condition = new QueryWrapper<>(systemConf);
        return this.baseMapper.selectList(condition);
    }
}
