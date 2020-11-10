package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.QueryCurrencyRateForm;
import com.jayud.mall.model.po.CurrencyRate;
import com.jayud.mall.model.vo.CurrencyRateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 币种汇率 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Mapper
@Component
public interface CurrencyRateMapper extends BaseMapper<CurrencyRate> {

    /**
     * 查询list
     * @param form
     * @return
     */
    List<CurrencyRateVO> findCurrencyRate(@Param("form") QueryCurrencyRateForm form);
}
