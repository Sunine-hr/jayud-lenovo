package com.jayud.mall.mapper;

import com.jayud.mall.model.po.BillTaskRelevance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提单任务关联 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Mapper
@Component
public interface BillTaskRelevanceMapper extends BaseMapper<BillTaskRelevance> {

}
