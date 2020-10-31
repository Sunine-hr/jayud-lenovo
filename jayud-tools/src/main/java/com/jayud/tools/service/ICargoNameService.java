package com.jayud.tools.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.vo.CargoNameSmallVO;
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
    void importExcel(List<List<Object>> list, Long userId);

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

    /**
     * 导入Excel，第二版
     * @param list
     */
    void importExcelV2(List<CargoName> list);

    /**
     * <p>查询A类表list集合</p>
     * <p>V2</p>
     * <p>A类表:不存在`敏感品名`的货物表</p>
     * @return
     */
    List<CargoNameSmallVO> findCargoNameListByAV2(Long userId);

    /**
     * <p>查询B类表list集合</p>
     * <p>V2</p>
     * <p>B类表:存在`敏感品名`的货物表</p>
     * @return
     */
    List<CargoNameSmallVO> findCargoNameListByBV2(Long userId);

    /**
     * 清空`货物名称表`
     */
    void truncateCargoName();
}
