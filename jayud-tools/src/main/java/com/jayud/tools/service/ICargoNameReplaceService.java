package com.jayud.tools.service;

import com.jayud.common.CommonResult;
import com.jayud.tools.model.bo.CargoNameReplaceForm;
import com.jayud.tools.model.po.CargoNameReplace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 货物名称替换表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ICargoNameReplaceService extends IService<CargoNameReplace> {

    /**
     * 查询货品名称替换list
     * @param form
     * @return
     */
    List<CargoNameReplace> findCargoNameReplace(CargoNameReplaceForm form);

    /**
     * 导入货物名称替换表
     * @param list
     */
    void importExcel(List<CargoNameReplace> list);

    /**
     * 保存`货物名称替换表`
     * @param form
     * @return
     */
    CommonResult saveCargoNameReplace(CargoNameReplaceForm form);

    /**
     * 删除`货物名称替换表`
     * @param id
     * @return
     */
    CommonResult deleteCargoNameReplace(Long id);
}
