package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oauth.mapper.SystemUserLegalMapper;
import com.jayud.oauth.model.po.SystemUserLegal;
import com.jayud.oauth.service.ISystemUserLegalService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since
 */
@Service
public class SystemUserLegalServiceImpl extends ServiceImpl<SystemUserLegalMapper, SystemUserLegal> implements ISystemUserLegalService {


    @Override
    public List<Long> getLegalId(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",id);
        List<SystemUserLegal> list = baseMapper.selectList(queryWrapper);
        List<Long> longs = new ArrayList<>();
        for (SystemUserLegal systemUserLegal : list) {
            longs.add(systemUserLegal.getId());
        }
        return longs;
    }
}
