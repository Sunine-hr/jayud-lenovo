package com.jayud.scm.service;

import com.jayud.scm.model.po.BBrand;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌库表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IBBrandService extends IService<BBrand> {

    BBrand getNameByNameEn(String skuBrand);
}
