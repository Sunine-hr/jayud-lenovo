package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.Country;
import com.jayud.mall.mapper.CountryMapper;
import com.jayud.mall.service.ICountryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 国家基础信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-19
 */
@Service
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements ICountryService {

}
