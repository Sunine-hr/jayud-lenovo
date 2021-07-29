package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.finance.bo.AddCurrencyManageForm;
import com.jayud.finance.bo.AddCurrencyRateForm;
import com.jayud.finance.bo.EditCurrencyRateForm;
import com.jayud.finance.bo.QueryCurrencyRateForm;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.CurrencyRateMapper;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.vo.CurrencyRateVO;
import com.jayud.finance.vo.InitComboxStrVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    @Autowired
    private OmsClient omsClient;

    @Override
    public IPage<CurrencyRateVO> findCurrencyRateByPage(QueryCurrencyRateForm form) {
        //定义分页参数
        Page<CurrencyRateVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("cr.id"));
        IPage<CurrencyRateVO> pageInfo = baseMapper.findCurrencyRateByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult saveCurrencyRate(AddCurrencyManageForm form) {
        //不允许添加相同币种的汇率
        List<AddCurrencyRateForm> rateList = form.getRateFormList();
        int repeatNum = rateList.stream()
                .collect(Collectors.groupingBy(a -> String.valueOf(a.getOcid()).concat(String.valueOf(a.getDcid())), Collectors.counting()))
                .entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
                .collect(Collectors.toList()).size();
        if (repeatNum != 0) {
            return CommonResult.error(10000, "不允许添加相同币种的汇率");
        }

        //查询币种
        Map<Long, InitComboxStrVO> currencyMap = this.omsClient.initCurrencyInfo().getData().stream().collect(Collectors.toMap(InitComboxStrVO::getId, e -> e));
        //同一个币种同一个月份只允许配置一条
        QueryWrapper<CurrencyRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month", form.getMonth());

        //查询这个月份汇率管理
        Map<String, CurrencyRate> map = this.baseMapper.selectList(queryWrapper).stream().collect(Collectors.toMap(e -> e.getOcid() + "-" + e.getDcid(), e -> e));

        //间接汇率
        List<CurrencyRate> indirectExchangeRates = new ArrayList<>();
        //新增的时候批量操作
        List<CurrencyRate> currencyRates = new ArrayList<>();
        for (AddCurrencyRateForm addCurrencyRateForm : form.getRateFormList()) {
//            queryWrapper.eq("ocid", addCurrencyRateForm.getOcid());
//            queryWrapper.eq("dcid", addCurrencyRateForm.getDcid());

//            List<CurrencyRate> existList = baseMapper.selectList(queryWrapper);
//            if (existList != null && existList.size() > 0) {
//                return CommonResult.error(10000, "该月份已存在相同币种的汇率");
//            }
            CurrencyRate tmp = map.get(addCurrencyRateForm.getOcid() + "-" + addCurrencyRateForm.getDcid());
            if (tmp != null) {
                return CommonResult.error(10000, "该月份已存在相同币种的汇率");
            }

            CurrencyRate currencyRate = new CurrencyRate();
            currencyRate.setOcid(addCurrencyRateForm.getOcid());
            currencyRate.setDcid(addCurrencyRateForm.getDcid());
            currencyRate.setExchangeRate(addCurrencyRateForm.getExchangeRate());
            currencyRate.setMonth(form.getMonth());
            currencyRate.setCreatedUser(form.getLoginUserName());
            currencyRates.add(currencyRate);

            //原币兑人民币才有间接汇率:算法1/原币兑人民币汇率
            if ("CNY".equals(currencyMap.get(addCurrencyRateForm.getDcid()).getCode())
                    && !"CNY".equals(currencyMap.get(addCurrencyRateForm.getOcid()).getCode())) {
                CurrencyRate tmpRate = map.get(addCurrencyRateForm.getDcid() + "-" + addCurrencyRateForm.getOcid());
                BigDecimal indirectExchangeRate = new BigDecimal(1).divide(addCurrencyRateForm.getExchangeRate(), 4, RoundingMode.HALF_EVEN);
                CurrencyRate indirectRate = new CurrencyRate().setMonth(currencyRate.getMonth()).setExchangeRate(indirectExchangeRate)
                        .setCreatedTime(currencyRate.getCreatedTime()).setCreatedUser(currencyRate.getCreatedUser())
                        .setOcid(currencyRate.getDcid()).setDcid(currencyRate.getOcid());
                if (tmpRate != null) {
                    indirectRate.setId(tmpRate.getId());
                }
                indirectExchangeRates.add(indirectRate);
            }

        }
        //批量处理间接汇率
        saveOrUpdateBatch(indirectExchangeRates);
        saveBatch(currencyRates);
        return CommonResult.success();
    }

    @Override
    @Transactional
    public Boolean editCurrencyRate(EditCurrencyRateForm form) {
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setId(form.getId());
        currencyRate.setExchangeRate(form.getExchangeRate());//只允许改汇率

        CurrencyRate tmp = this.getById(form.getId());

        //同一个币种同一个月份只允许配置一条
        QueryWrapper<CurrencyRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month", tmp.getMonth());

        //查询这个月份汇率管理
        Map<String, CurrencyRate> map = this.baseMapper.selectList(queryWrapper).stream().collect(Collectors.toMap(e -> e.getOcid() + "-" + e.getDcid(), e -> e));

        //查询币种
        Map<Long, InitComboxStrVO> currencyMap = this.omsClient.initCurrencyInfo().getData().stream().collect(Collectors.toMap(InitComboxStrVO::getId, e -> e));
        //原币兑人民币才有间接汇率:算法1/原币兑人民币汇率
        if ("CNY".equals(currencyMap.get(tmp.getDcid()).getCode())) {
            CurrencyRate tmpRate = map.get(tmp.getDcid() + "-" + tmp.getOcid());
            BigDecimal indirectExchangeRate = new BigDecimal(1).divide(form.getExchangeRate(), 4, RoundingMode.HALF_EVEN);
            CurrencyRate indirectRate = new CurrencyRate().setMonth(tmp.getMonth()).setExchangeRate(indirectExchangeRate)
                    .setOcid(tmp.getDcid()).setDcid(tmp.getOcid());
            if (tmpRate != null) {
                indirectRate.setId(tmpRate.getId());
            } else {
                indirectRate.setCreatedTime(LocalDateTime.now()).setCreatedUser(UserOperator.getToken());
            }
            //同步间接汇率
            this.saveOrUpdate(indirectRate);
        }
        return updateById(currencyRate);
    }

    @Override
    public BigDecimal getExchangeRate(String oCode, String dCode,String accountTerm) {
        return baseMapper.getExchangeRate(oCode, dCode,accountTerm);
    }

    @Override
    public String getNameByCode(String code) {
        return baseMapper.getNameByCode(code);
    }

    @Override
    public List<InitComboxStrVO> initHeXiaoCurrency(String currencyName) {
        return baseMapper.initHeXiaoCurrency(currencyName);
    }

    /**
     * 根据原始币种和兑换币种获取汇率
     *
     * @return
     */
    @Override
    public Map<String, BigDecimal> getExchangeRates(String dcCode, String month) {
        List<InitComboxStrVO> currencyInfos = omsClient.initCurrencyInfo().getData();
        if (CollectionUtils.isEmpty(currencyInfos)) {
            return null;
        }
        Map<Long, String> currencyMap = currencyInfos.stream().collect(Collectors.toMap(InitComboxStrVO::getId, InitComboxStrVO::getCode));
        AtomicReference<Long> dcId = new AtomicReference<>();
        currencyMap.forEach((k, v) -> {
            if (v.equals(dcCode)) {
                dcId.set(k);
            }
        });


        QueryWrapper<CurrencyRate> condition = new QueryWrapper<>();
        condition.lambda().eq(CurrencyRate::getMonth, month)
                .eq(CurrencyRate::getDcid, dcId.get())
                .eq(CurrencyRate::getStatus, StatusEnum.ENABLE.getCode());
        List<CurrencyRate> currencyRates = this.baseMapper.selectList(condition);
        Map<String, BigDecimal> exchange = new HashMap<>(currencyRates.size());
        currencyRates.forEach(e -> {
            exchange.put(currencyMap.get(e.getOcid()), e.getExchangeRate());
        });
        return exchange;
    }


}
