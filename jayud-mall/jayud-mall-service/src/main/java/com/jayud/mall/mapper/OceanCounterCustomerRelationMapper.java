package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OceanCounterCustomerRelation;
import com.jayud.mall.model.vo.OceanCounterCustomerRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单对应货柜信息和客户关联表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OceanCounterCustomerRelationMapper extends BaseMapper<OceanCounterCustomerRelation> {

    /**
     * 根据柜号信息，获取装柜信息list
     * @param oceanCounterId
     * @return
     */
    List<OceanCounterCustomerRelationVO> findZgxxListByOceanCounterId(Long oceanCounterId);
}
