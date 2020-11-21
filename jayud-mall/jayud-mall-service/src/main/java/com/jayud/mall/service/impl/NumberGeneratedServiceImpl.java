package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.NumberGenerated;
import com.jayud.mall.mapper.NumberGeneratedMapper;
import com.jayud.mall.service.INumberGeneratedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单号生成器 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-21
 */
@Service
public class NumberGeneratedServiceImpl extends ServiceImpl<NumberGeneratedMapper, NumberGenerated> implements INumberGeneratedService {

}
