package com.jayud.tools.mapper;

import com.jayud.tools.model.po.CargoName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
