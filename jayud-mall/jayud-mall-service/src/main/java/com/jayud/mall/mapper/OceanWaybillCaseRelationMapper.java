package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OceanWaybillCaseRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运单对应箱号关联表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
@Mapper
@Component
public interface OceanWaybillCaseRelationMapper extends BaseMapper<OceanWaybillCaseRelation> {

}
