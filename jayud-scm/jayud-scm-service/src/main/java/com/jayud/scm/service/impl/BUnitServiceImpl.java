package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.scm.model.po.BUnit;
import com.jayud.scm.mapper.BUnitMapper;
import com.jayud.scm.service.IBUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 计量单位代码表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class BUnitServiceImpl extends ServiceImpl<BUnitMapper, BUnit> implements IBUnitService {

    @Override
    public List<String> getUnits() {
        return this.baseMapper.getUnits();
    }
}
