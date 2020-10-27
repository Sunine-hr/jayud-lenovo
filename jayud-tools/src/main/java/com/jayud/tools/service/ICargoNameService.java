package com.jayud.tools.service;

import com.jayud.tools.model.po.CargoName;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 货物名称表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
public interface ICargoNameService extends IService<CargoName> {

    /**
     * 导入list
     * @param list
     */
    void importExcel(List<CargoName> list);
}
