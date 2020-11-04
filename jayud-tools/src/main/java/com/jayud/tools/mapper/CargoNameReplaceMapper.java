package com.jayud.tools.mapper;

import com.jayud.tools.model.po.CargoNameReplace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 货物名称替换表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface CargoNameReplaceMapper extends BaseMapper<CargoNameReplace> {

}
