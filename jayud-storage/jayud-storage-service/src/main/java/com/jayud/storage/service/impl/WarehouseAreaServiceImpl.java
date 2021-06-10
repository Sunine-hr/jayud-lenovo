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
import com.jayud.storage.model.po.WarehouseArea;
import com.jayud.storage.mapper.WarehouseAreaMapper;
import com.jayud.storage.model.vo.WarehouseAreaVO;
import com.jayud.storage.service.IWarehouseAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 仓库区域表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Service
public class WarehouseAreaServiceImpl extends ServiceImpl<WarehouseAreaMapper, WarehouseArea> implements IWarehouseAreaService {

    @Override
    public boolean saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseAreaForm) {
        for (AreaForm areaForm : warehouseAreaForm.getAreaForms()) {
            WarehouseArea warehouseArea = ConvertUtil.convert(areaForm, WarehouseArea.class);
            warehouseArea.setCreateTime(LocalDateTime.now());
            warehouseArea.setCreateUser(UserOperator.getToken());
            warehouseArea.setWarehouseId(warehouseAreaForm.getWarehouseId());
            warehouseArea.setStatus(1);
            warehouseArea.setRemarks(warehouseAreaForm.getRemarks());
            boolean b = this.saveOrUpdate(warehouseArea);
            if(!b){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<WarehouseAreaVO> getList(QueryWarehouseAreaForm form) {
        return this.baseMapper.getList(form);
    }

    @Override
    public IPage<WarehouseAreaVO> findWarehouseAreaByPage(QueryWarehouseAreaForm form) {
        Page<WarehouseAreaVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<WarehouseAreaVO> pageInfo = this.baseMapper.findWarehouseAreaByPage(page,form);
        return pageInfo;
    }

    @Override
    public void operationWarehouseArea(OperationForm form) {
        Long id = form.getId();
        WarehouseArea warehouseArea = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(warehouseArea)){
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，没有找到该仓库区域");
        }
        if(warehouseArea.getStatus().equals(WarehouseStatusEnum.DISABLE.getCode())){
            warehouseArea.setStatus(WarehouseStatusEnum.ENABLE.getCode());
        }else if(warehouseArea.getStatus().equals(WarehouseStatusEnum.ENABLE.getCode())){
            warehouseArea.setStatus(WarehouseStatusEnum.DISABLE.getCode());
        }else{
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，输入的状态有误");
        }
        this.saveOrUpdate(warehouseArea);
    }

    @Override
    public WarehouseArea getWarehouseAreaByAreaCode(String areaCode, Long warehouseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("area_code",areaCode);
        queryWrapper.eq("warehouse_id",warehouseId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public WarehouseArea getWarehouseAreaByAreaName(String areaName, Long warehouseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("area_name",areaName);
        queryWrapper.eq("warehouse_id",warehouseId);
        return this.baseMapper.selectOne(queryWrapper);
    }


}
