package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.CounterCase;
import com.jayud.mall.model.vo.CounterCaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 货柜对应运单箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-22
 */
@Mapper
@Component
public interface CounterCaseMapper extends BaseMapper<CounterCase> {

    /**
     * 根据提单id，查询装柜信息
     * @param obId
     * @return
     */
    List<CounterCaseVO> findCounterCaseByObId(@Param("obId") Long obId);

    /**
     * 根据提单id，查询装柜信息，汇总箱子的体积
     * @param obId
     * @return
     */
    BigDecimal findCounterCaseVolumeTotalByObId(@Param("obId") Long obId);
}
