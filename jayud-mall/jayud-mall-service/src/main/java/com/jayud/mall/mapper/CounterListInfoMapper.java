package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CounterListInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.CounterCaseInfoVO;
import com.jayud.mall.model.vo.CounterListInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 柜子清单信息表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
@Component
public interface CounterListInfoMapper extends BaseMapper<CounterListInfo> {

    /**
     * 根据id，查询CounterListInfo
     * @param id
     * @return
     */
    CounterListInfoVO findCounterListInfoById(@Param("id") Long id);

    /**
     * 根据b_id，查询CounterCaseInfo list
     * @param b_id
     * @return
     */
    List<CounterCaseInfoVO> findCounterCaseInfo(@Param("b_id") Long b_id);

    /**
     * 根据柜子id，查询柜子下的 柜子清单信息表
     * @param counterId
     * @return
     */
    List<CounterListInfoVO> findCounterListInfoByCounterId(@Param("counterId") Long counterId);

    /**
     * 统计箱数
     * @param b_id
     * @return
     */
    Integer findCounterCaseInfoTotalBybid(@Param("b_id") Long b_id);
}
