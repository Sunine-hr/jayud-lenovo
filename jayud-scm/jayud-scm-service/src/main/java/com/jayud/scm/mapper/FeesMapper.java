package com.jayud.scm.mapper;

import com.jayud.scm.model.po.Fees;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 费用计算公式表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-10
 */
@Mapper
public interface FeesMapper extends BaseMapper<Fees> {

    List<String> formulaSettingMaintenance(String modelType);
}
