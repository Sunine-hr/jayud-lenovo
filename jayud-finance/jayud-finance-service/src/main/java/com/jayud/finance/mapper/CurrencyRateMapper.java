package com.jayud.finance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.vo.CurrencyRateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 币种汇率 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-12
 */
@Mapper
public interface CurrencyRateMapper extends BaseMapper<CurrencyRate> {

    /**
     * 应付对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<CurrencyRateVO> findCurrencyRateByPage(Page page, @Param("form") QueryCurrencyRateForm form);

    /**
     * 根据原始币种和兑换币种获取汇率
     * @param oCode
     * @param dCode
     * @return
     */
    BigDecimal getExchangeRate(@Param("oCode") String oCode,@Param("dCode")String dCode);

    /**
     * 根据币种CODE查名称描述
     * @param code
     * @return
     */
    String getNameByCode(@Param("code") String code);
}
