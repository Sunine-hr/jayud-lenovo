package com.jayud.mall.service;

import com.jayud.mall.model.bo.CountryForm;
import com.jayud.mall.model.po.Country;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CountryVO;

import java.util.List;

/**
 * <p>
 * 国家基础信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-19
 */
public interface ICountryService extends IService<Country> {

    /**
     * 查询国家
     * @param form
     * @return
     */
    List<CountryVO> findCountry(CountryForm form);
}
