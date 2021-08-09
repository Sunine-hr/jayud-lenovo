package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.BBrand;
import com.jayud.scm.mapper.BBrandMapper;
import com.jayud.scm.service.IBBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌库表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class BBrandServiceImpl extends ServiceImpl<BBrandMapper, BBrand> implements IBBrandService {

    @Override
    public BBrand getNameByNameEn(String skuBrand) {
        QueryWrapper<BBrand> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BBrand::getEnName,skuBrand);
        queryWrapper.lambda().eq(BBrand::getVoided,0);
        return this.getOne(queryWrapper);
    }
}
