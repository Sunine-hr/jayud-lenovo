package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.SaveCounterCaseForm;
import com.jayud.mall.model.po.CounterCase;
import com.jayud.mall.mapper.CounterCaseMapper;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICounterCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 货柜对应运单箱号信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-22
 */
@Service
public class CounterCaseServiceImpl extends ServiceImpl<CounterCaseMapper, CounterCase> implements ICounterCaseService {

    @Autowired
    CounterCaseMapper counterCaseMapper;
    @Autowired
    BaseService baseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCounterCase(SaveCounterCaseForm form) {
        AuthUser user = baseService.getUser();
        Long oceanCounterId = form.getOceanCounterId();//柜号id
        List<Long> orderCaseIds = form.getOrderCaseIds();//运单箱号id

        QueryWrapper<CounterCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_case_id",orderCaseIds);

        //1.先删除，运单箱号关联过的柜号
        this.remove(queryWrapper);

        List<CounterCase> list = new ArrayList<>();
        orderCaseIds.forEach(orderCaseId ->{
            CounterCase counterCase = new CounterCase();
            counterCase.setOceanCounterId(oceanCounterId);
            counterCase.setOrderCaseId(orderCaseId);
            counterCase.setUserId(user.getId().intValue());
            counterCase.setUserName(user.getName());
            list.add(counterCase);
        });
        this.saveOrUpdateBatch(list);

        return CommonResult.success("修改订单箱号，配载信息，成功");
    }
}
