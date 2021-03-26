package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oceanship.po.SeaContainerInformation;
import com.jayud.oceanship.mapper.SeaContainerInformationMapper;
import com.jayud.oceanship.service.ISeaContainerInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 货柜信息表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-25
 */
@Service
public class SeaContainerInformationServiceImpl extends ServiceImpl<SeaContainerInformationMapper, SeaContainerInformation> implements ISeaContainerInformationService {

    @Override
    public List<SeaContainerInformation> getList(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sea_rep_id",id);
        List list = this.baseMapper.selectList(queryWrapper);
        return list;
    }
}
