package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.ClearingWay;
import com.jayud.mall.model.vo.ClearingWayVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 基础表-结算方式 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-26
 */
@Mapper
@Component
public interface ClearingWayMapper extends BaseMapper<ClearingWay> {

    /**
     * 查询结算方式 list
     * @return
     */
    List<ClearingWayVO> findClearingWay();
}
