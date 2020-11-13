package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OceanWaybillCaseRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OceanWaybillCaseRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * 根据运单id，查询箱号信息list
     * @param oceanWaybillId
     * @return
     */
    List<OceanWaybillCaseRelationVO> findXhxxByOceanWaybillId(Long oceanWaybillId);
}
