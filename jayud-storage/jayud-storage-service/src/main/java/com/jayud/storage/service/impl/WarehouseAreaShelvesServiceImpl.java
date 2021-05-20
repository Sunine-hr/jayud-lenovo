package com.jayud.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.enums.WarehouseStatusEnum;
import com.jayud.storage.model.po.Warehouse;
import com.jayud.storage.model.po.WarehouseArea;
import com.jayud.storage.model.po.WarehouseAreaShelves;
import com.jayud.storage.mapper.WarehouseAreaShelvesMapper;
import com.jayud.storage.model.vo.WarehouseAreaShelvesFormVO;
import com.jayud.storage.model.vo.WarehouseAreaShelvesVO;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import com.jayud.storage.service.IWarehouseAreaShelvesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品区域货架表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Service
public class WarehouseAreaShelvesServiceImpl extends ServiceImpl<WarehouseAreaShelvesMapper, WarehouseAreaShelves> implements IWarehouseAreaShelvesService {

    @Override
    public boolean saveOrUpdateWarehouseAreaShelves(WarehouseAreaShelvesForm form) {
        for (ShelvesForm shelvesForm : form.getShelvesName()) {
            WarehouseAreaShelves warehouseAreaShelves = ConvertUtil.convert(form, WarehouseAreaShelves.class);
            warehouseAreaShelves.setShelvesName(shelvesForm.getName());
            warehouseAreaShelves.setCreateTime(LocalDateTime.now());
            warehouseAreaShelves.setCreateUser(UserOperator.getToken());
            warehouseAreaShelves.setStatus(1);
            boolean b = this.saveOrUpdate(warehouseAreaShelves);
            if(!b){
                return false;
            }
        }

        return true;
    }

    @Override
    public void operationWarehouseAreaShelves(OperationForm form) {
        WarehouseAreaShelves warehouseAreaShelves = this.baseMapper.selectById(form.getId());
        if(ObjectUtil.isEmpty(warehouseAreaShelves)){
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，没有找到该仓库区域");
        }
        if(warehouseAreaShelves.getStatus().equals(WarehouseStatusEnum.DISABLE.getCode())){
            warehouseAreaShelves.setStatus(WarehouseStatusEnum.ENABLE.getCode());
        } else if(warehouseAreaShelves.getStatus().equals(WarehouseStatusEnum.ENABLE.getCode())){
            warehouseAreaShelves.setStatus(WarehouseStatusEnum.DISABLE.getCode());
        }else{
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，输入的状态有误");
        }
        this.saveOrUpdate(warehouseAreaShelves);
    }

    @Override
    public IPage<WarehouseAreaShelvesVO> findWarehouseAreaShelvesByPage(QueryWarehouseAreaShelvesForm form) {
        Page<WarehouseAreaShelvesVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<WarehouseAreaShelvesVO> pageInfo = this.baseMapper.findWarehouseAreaShelvesByPage(page,form);
        return pageInfo;
    }

    @Override
    public IPage<WarehouseAreaShelvesFormVO> findWarehouseAreaShelvesLocationByPage(QueryWarehouseAreaShelves2Form form) {
        Page<WarehouseAreaShelvesFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<WarehouseAreaShelvesFormVO> pageInfo = this.baseMapper.findWarehouseAreaShelvesLocationByPage(page,form);
        return pageInfo;
    }

    @Override
    public WarehouseAreaShelves getWarehouseAreaShelvesByShelvesName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("shelves_name",name);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public WarehouseAreaShelvesFormVO getWarehouseAreaShelvesByShelvesId(Long shelvesId) {
        return this.baseMapper.getWarehouseAreaShelvesByShelvesId(shelvesId);
    }
}
