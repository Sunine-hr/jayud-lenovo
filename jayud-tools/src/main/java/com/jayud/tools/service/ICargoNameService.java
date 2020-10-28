package com.jayud.tools.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.vo.CargoNameVO;

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
    void importExcel(List<List<Object>> list);

    /**
     * <p>查询A类表list集合</p>
     * <p>A类表:不存在`敏感品名`的货物表</p>
     * @return
     */
    List<CargoNameVO> findCargoNameListByA();

    /**
     * <p>查询B类表list集合</p>
     * <p>B类表:存在`敏感品名`的货物表</p>
     * @return
     */
    List<CargoNameVO> findCargoNameListByB();

    /**
     * 删除所有`货物名称表`
     */
    void deleteAllCargoName();
}
