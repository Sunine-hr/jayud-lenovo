package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oceanship.enums.SeaBookShipStatusEnum;
import com.jayud.oceanship.po.SeaBookship;
import com.jayud.oceanship.mapper.SeaBookshipMapper;
import com.jayud.oceanship.service.ISeaBookshipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 海运订船表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Service
public class SeaBookshipServiceImpl extends ServiceImpl<SeaBookshipMapper, SeaBookship> implements ISeaBookshipService {

    @Override
    public SeaBookship getEnableBySeaOrderId(Long id) {
        QueryWrapper<SeaBookship> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SeaBookship::getSeaOrderId, id);
        queryWrapper.lambda().ne(SeaBookship::getStatus, SeaBookShipStatusEnum.DELETE.getCode());
        return this.baseMapper.selectOne(queryWrapper);
    }
}
