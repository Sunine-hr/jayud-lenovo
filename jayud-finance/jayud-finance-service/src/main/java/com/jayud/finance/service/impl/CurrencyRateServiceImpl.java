package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.finance.bo.AddCurrencyManageForm;
import com.jayud.finance.bo.AddCurrencyRateForm;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.mapper.CurrencyRateMapper;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.vo.CurrencyRateVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 币种汇率 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-12
 */
@Service
public class CurrencyRateServiceImpl extends ServiceImpl<CurrencyRateMapper, CurrencyRate> implements ICurrencyRateService {

    @Override
    public IPage<CurrencyRateVO> findCurrencyRateByPage(QueryCurrencyRateForm form) {
        //定义分页参数
        Page<CurrencyRateVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opc.id"));
        IPage<CurrencyRateVO> pageInfo = baseMapper.findCurrencyRateByPage(page, form);
        return pageInfo;
    }

    @Override
    public Boolean saveCurrencyRate(AddCurrencyManageForm form) {
        //新增的时候批量操作
        List<CurrencyRate> currencyRates = new ArrayList<>();

        return saveBatch(currencyRates);
    }

    @Override
    public Boolean editCurrencyRate(AddCurrencyRateForm form) {
        return null;
    }

}
