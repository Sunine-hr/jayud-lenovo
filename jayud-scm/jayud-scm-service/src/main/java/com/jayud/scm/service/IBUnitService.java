package com.jayud.scm.service;

import com.jayud.scm.model.po.BUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 计量单位代码表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IBUnitService extends IService<BUnit> {

    //获取所有单位
    List<String> getUnits();

}
