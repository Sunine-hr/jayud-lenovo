package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.CostInfoMapper;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.service.ICostInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 费用名描述 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
@Service
public class CostInfoServiceImpl extends ServiceImpl<CostInfoMapper, CostInfo> implements ICostInfoService {

    @Override
    public List<CostInfo> findCostInfo() {
        return baseMapper.selectList(null);
    }
}
