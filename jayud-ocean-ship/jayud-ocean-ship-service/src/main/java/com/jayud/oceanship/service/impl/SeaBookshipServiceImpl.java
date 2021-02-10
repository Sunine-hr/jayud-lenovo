package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.oceanship.enums.SeaBookShipStatusEnum;
import com.jayud.oceanship.po.SeaBookship;
import com.jayud.oceanship.mapper.SeaBookshipMapper;
import com.jayud.oceanship.service.ISeaBookshipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public void saveOrUpdateBookShip(SeaBookship seaBookship) {
        if(seaBookship.getId()==null){
            seaBookship.setCreateTime(LocalDateTime.now());
            seaBookship.setCreateUser(UserOperator.getToken());
            this.save(seaBookship);
        }else {
            seaBookship.setUpdateTime(LocalDateTime.now());
            seaBookship.setUpdateUser(UserOperator.getToken());
            this.updateById(seaBookship);
        }
    }

    @Override
    public boolean updateBySeaOrderId(Long id, SeaBookship seaBookship) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sea_order_id", id);
        return this.update(seaBookship,queryWrapper);
    }
}
