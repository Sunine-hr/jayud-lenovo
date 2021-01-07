package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.model.vo.OceanCounterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单对应货柜信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OceanCounterMapper extends BaseMapper<OceanCounter> {

    /**
     * 根据提单id，查询提单关联货柜信息
     * @param obId
     * @return
     */
    List<OceanCounterVO> findOceanCounterVOByObId(@Param("obId") Long obId);

}
