package com.jayud.airfreight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.airfreight.model.po.AirExtensionField;
import com.jayud.airfreight.mapper.AirExtensionFieldMapper;
import com.jayud.airfreight.service.IAirExtensionFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 空运扩展字段表 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-12-01
 */
@Service
public class AirExtensionFieldServiceImpl extends ServiceImpl<AirExtensionFieldMapper, AirExtensionField> implements IAirExtensionFieldService {

    /**
     * 根据条件获取空运扩展字段
     */
    @Override
    public List<AirExtensionField> getByCondition(AirExtensionField airExtensionField) {
        QueryWrapper<AirExtensionField> condition = new QueryWrapper<>(airExtensionField);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据唯一标识修改数据
     */
    @Override
    public boolean updateByUniqueSign(String uniqueSign, AirExtensionField airExtensionField) {
        QueryWrapper<AirExtensionField> condition = new QueryWrapper<>();
        condition.lambda().eq(AirExtensionField::getThirdPartyUniqueSign, uniqueSign);
        return this.baseMapper.update(airExtensionField, condition) > 0;
    }
}
