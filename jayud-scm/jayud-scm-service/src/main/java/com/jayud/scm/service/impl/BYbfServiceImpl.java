package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.BYbf;
import com.jayud.scm.mapper.BYbfMapper;
import com.jayud.scm.service.IBYbfService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 运保费表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-27
 */
@Service
public class BYbfServiceImpl extends ServiceImpl<BYbfMapper, BYbf> implements IBYbfService {

    @Override
    public BYbf getBYbfByDtaTime(LocalDateTime now) {
        QueryWrapper<BYbf> queryWrapper = new QueryWrapper();

        queryWrapper.lambda().ge(BYbf::getBeginDtm,now);
        queryWrapper.lambda().le(BYbf::getEndDtm,now);
        return this.list(queryWrapper).get(0);
    }
}
