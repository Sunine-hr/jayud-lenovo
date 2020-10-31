package com.jayud.tools.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.model.vo.CargoNameSmallVO;
import com.jayud.tools.model.vo.CargoNameVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 货物名称表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Mapper
@Component
public interface CargoNameMapper extends BaseMapper<CargoName> {

    /**
     * 导入Excel数据
     * @param excels
     */
    void importExcel(List<CargoName> excels);

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
     * 根据用户id，删除货物表
     */
    void deleteCargoNameByUserId(Long userId);

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
