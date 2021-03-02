package com.jayud.trailer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.trailer.enums.TrailerOrderStatusEnum;
import com.jayud.trailer.po.TrailerDispatch;
import com.jayud.trailer.mapper.TrailerDispatchMapper;
import com.jayud.trailer.service.ITrailerDispatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Service
public class TrailerDispatchServiceImpl extends ServiceImpl<TrailerDispatchMapper, TrailerDispatch> implements ITrailerDispatchService {

    @Override
    public TrailerDispatch getEnableByTrailerOrderId(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",id);
        queryWrapper.ne("status", TrailerOrderStatusEnum.DELETE.getCode());
        return this.baseMapper.selectOne(queryWrapper);
    }
}
