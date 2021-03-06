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

    /**
     * 根据 本币id，他币id，查询对应的转换汇率
     * @param dcid  本币(currency_info id)
     * @param ocid  他币(currency_info id)
     * @return
     */
    CurrencyRateVO findCurrencyRateByDcidAndOcid(@Param("dcid") Integer dcid, @Param("ocid") Integer ocid);
}
