package com.jayud.finance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.vo.CurrencyRateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
