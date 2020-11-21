package com.jayud.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.AddCurrencyManageForm;
import com.jayud.finance.bo.EditCurrencyRateForm;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.vo.CurrencyRateVO;

/**
 * <p>
 * 币种汇率 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-12
 */
public interface ICurrencyRateService extends IService<CurrencyRate> {

    /**
     * 应付对账单分页查询
     * @param form
     * @return
     */
    IPage<CurrencyRateVO> findCurrencyRateByPage(QueryCurrencyRateForm form);


    /**
     * 新增汇率
     * @param form
     * @return
     */
    CommonResult saveCurrencyRate(AddCurrencyManageForm form);

    /**
     * 编辑汇率
     * @param form
     * @return
     */
    Boolean editCurrencyRate(EditCurrencyRateForm form);
}
