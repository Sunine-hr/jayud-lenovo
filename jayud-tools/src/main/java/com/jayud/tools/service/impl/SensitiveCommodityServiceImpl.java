package com.jayud.tools.service.impl;

import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.mapper.SensitiveCommodityMapper;
import com.jayud.tools.service.ISensitiveCommodityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 敏感品名表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Service
public class SensitiveCommodityServiceImpl extends ServiceImpl<SensitiveCommodityMapper, SensitiveCommodity> implements ISensitiveCommodityService {

    @Autowired
    SensitiveCommodityMapper sensitiveCommodityMapper;

    @Override
    public List<SensitiveCommodity> getSensitiveCommodityList() {

        return sensitiveCommodityMapper.getSensitiveCommodityList();
    }
}
