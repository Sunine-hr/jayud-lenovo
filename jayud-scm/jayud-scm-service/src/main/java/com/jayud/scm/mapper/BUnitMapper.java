package com.jayud.scm.mapper;

import com.jayud.scm.model.po.BUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 计量单位代码表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Mapper
public interface BUnitMapper extends BaseMapper<BUnit> {

    List<String> getUnits();
}
