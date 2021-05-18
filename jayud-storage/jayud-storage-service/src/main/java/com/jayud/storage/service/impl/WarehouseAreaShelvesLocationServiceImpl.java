package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesLocationForm;
import com.jayud.storage.model.po.WarehouseAreaShelvesLocation;
import com.jayud.storage.mapper.WarehouseAreaShelvesLocationMapper;
import com.jayud.storage.model.vo.WarehouseAreaShelvesLocationVO;
import com.jayud.storage.service.IWarehouseAreaShelvesLocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 仓库区域货架库位表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Service
public class WarehouseAreaShelvesLocationServiceImpl extends ServiceImpl<WarehouseAreaShelvesLocationMapper, WarehouseAreaShelvesLocation> implements IWarehouseAreaShelvesLocationService {

    @Override
    public List<WarehouseAreaShelvesLocation> getUpdateTime() {
        return this.baseMapper.getUpdateTime();
    }

    @Override
    public IPage<WarehouseAreaShelvesLocationVO> findWarehouseAreaShelvesLocationByPage(QueryWarehouseAreaShelvesLocationForm form) {
        Page<WarehouseAreaShelvesLocationVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<WarehouseAreaShelvesLocationVO> pageInfo = this.baseMapper.findWarehouseAreaShelvesLocationByPage(page,form);
        return pageInfo;
    }

    @Override
    public boolean saveOrUpdateWarehouseAreaShelvesLocation(List<WarehouseAreaShelvesLocationForm> form) {
        List<WarehouseAreaShelvesLocation> warehouseAreaShelvesLocations = ConvertUtil.convertList(form, WarehouseAreaShelvesLocation.class);
        for (WarehouseAreaShelvesLocation warehouseAreaShelvesLocation : warehouseAreaShelvesLocations) {
            warehouseAreaShelvesLocation.setStatus(1);
            warehouseAreaShelvesLocation.setCreateTime(LocalDateTime.now());
            warehouseAreaShelvesLocation.setCreateUser(UserOperator.getToken());
        }
        return this.saveOrUpdateBatch(warehouseAreaShelvesLocations);
    }

    @Override
    public List<WarehouseAreaShelvesLocationVO> getListByShelvesId(QueryWarehouseAreaShelvesLocationForm form) {
        return this.baseMapper.getListByShelvesId(form);
    }

    @Override
    public List<WarehouseAreaShelvesLocation> getList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status",1);
        return this.baseMapper.selectList(queryWrapper);
    }
}
