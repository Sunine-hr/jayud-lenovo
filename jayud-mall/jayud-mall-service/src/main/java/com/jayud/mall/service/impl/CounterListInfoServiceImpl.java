package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.CounterListInfo;
import com.jayud.mall.mapper.CounterListInfoMapper;
import com.jayud.mall.model.vo.CounterCaseInfoExcelVO;
import com.jayud.mall.model.vo.CounterCaseInfoVO;
import com.jayud.mall.model.vo.CounterListInfoVO;
import com.jayud.mall.service.ICounterListInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 柜子清单信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
@Service
public class CounterListInfoServiceImpl extends ServiceImpl<CounterListInfoMapper, CounterListInfo> implements ICounterListInfoService {

    @Autowired
    CounterListInfoMapper counterListInfoMapper;

    @Override
    public CounterListInfoVO findCounterListInfoById(Long id) {
        return counterListInfoMapper.findCounterListInfoById(id);
    }

    @Override
    public List<CounterCaseInfoVO> findCounterCaseInfo(Long b_id) {
        return counterListInfoMapper.findCounterCaseInfo(b_id);
    }

    @Override
    public List<CounterCaseInfoExcelVO> findCounterCaseInfoBybid(Long b_id) {
        List<CounterCaseInfoExcelVO> counterCaseInfoExcelVOS = counterListInfoMapper.findCounterCaseInfoBybid(b_id);
        return counterCaseInfoExcelVOS;
    }
}
