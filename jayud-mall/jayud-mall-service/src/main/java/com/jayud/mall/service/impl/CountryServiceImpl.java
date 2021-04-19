package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.CountryForm;
import com.jayud.mall.model.po.Country;
import com.jayud.mall.mapper.CountryMapper;
import com.jayud.mall.model.vo.CountryVO;
import com.jayud.mall.service.ICountryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    CountryMapper countryMapper;

    @Override
    public List<CountryVO> findCountry(CountryForm form) {
        QueryWrapper<Country> queryWrapper = new QueryWrapper<>();
        String name = form.getName();
        if(name != null && name != ""){
            queryWrapper.like("name", name);
        }
        String code = form.getCode();
        if(code != null && code != ""){
            queryWrapper.eq("code", code);
        }
        String geo = form.getGeo();
        if(geo != null) {
            queryWrapper.eq("geo", geo);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<Country> countries = countryMapper.selectList(queryWrapper);
        List<CountryVO> countryVOS = ConvertUtil.convertList(countries, CountryVO.class);
        return countryVOS;
    }
}
