package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oauth.mapper.SystemUserLegalMapper;
import com.jayud.oauth.model.po.LegalEntity;
import com.jayud.oauth.model.po.SystemUserLegal;
import com.jayud.oauth.service.ILegalEntityService;
import com.jayud.oauth.service.ISystemUserLegalService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ILegalEntityService legalEntityService;

    @Override
    public List<Long> getLegalId(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("system_user_id",id);
        List<SystemUserLegal> list = baseMapper.selectList(queryWrapper);
        List<Long> longs = new ArrayList<>();
        for (SystemUserLegal systemUserLegal : list) {
            longs.add(systemUserLegal.getLegalId());
        }
        return longs;
    }

    @Override
    public List<String> getLegalName(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("system_user_id",id);
        List<SystemUserLegal> list = baseMapper.selectList(queryWrapper);
        List<String> str = new ArrayList<>();
        for (SystemUserLegal systemUserLegal : list) {
            LegalEntity legalEntity = legalEntityService.getById(systemUserLegal.getLegalId());
            str.add(legalEntity.getLegalName());
        }
        return str;
    }
}
