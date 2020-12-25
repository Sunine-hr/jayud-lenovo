package com.jayud.airfreight.service;

import com.jayud.airfreight.model.po.AirExtensionField;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 空运扩展字段表 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-12-01
 */
public interface IAirExtensionFieldService extends IService<AirExtensionField> {

    /**
     * 根据条件获取空运扩展字段
     */
    public List<AirExtensionField> getByCondition(AirExtensionField airExtensionField);

    /**
     * 根据唯一标识修改数据
     */
    public boolean updateByUniqueSign(String uniqueSign, AirExtensionField airExtensionField);
}
