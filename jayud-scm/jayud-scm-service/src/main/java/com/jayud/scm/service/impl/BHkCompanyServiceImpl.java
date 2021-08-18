package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.mapper.BHkCompanyMapper;
import com.jayud.scm.model.po.BHkCompany;
import com.jayud.scm.service.IBHkCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 香港抬头表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-18
 */
@Service
public class BHkCompanyServiceImpl extends ServiceImpl<BHkCompanyMapper, BHkCompany> implements IBHkCompanyService {

    @Autowired
    BHkCompanyMapper bHkCompanyMapper;

    @Override
    public List<BHkCompany> findBHkCompany() {
        QueryWrapper<BHkCompany> queryWrapper = new QueryWrapper<>();
        List<BHkCompany> list = this.list(queryWrapper);
        return list;
    }
}
