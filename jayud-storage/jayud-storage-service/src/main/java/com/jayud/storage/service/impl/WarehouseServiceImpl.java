package com.jayud.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.mapper.WarehouseMapper;
import com.jayud.storage.model.bo.OperationWarehouseForm;
import com.jayud.storage.model.bo.QueryWarehouseForm;
import com.jayud.storage.model.bo.SaveWarehouseForm;
import com.jayud.storage.model.enums.WarehouseStatusEnum;
import com.jayud.storage.model.po.Warehouse;
import com.jayud.storage.model.vo.WarehouseVO;
import com.jayud.storage.service.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 仓库表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

    @Autowired
    WarehouseMapper warehouseMapper;

    @Override
    public List<WarehouseVO> findWarehouse(QueryWarehouseForm form) {
        return warehouseMapper.findWarehouse(form);
    }

    @Override
    public IPage<WarehouseVO> findWarehouseByPage(QueryWarehouseForm form) {
        //定义分页参数
        Page<WarehouseVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));
        IPage<WarehouseVO> pageInfo = warehouseMapper.findWarehouseByPage(page, form);
        return pageInfo;
    }

    @Override
    public void operationWarehouse(OperationWarehouseForm form) {
        Long id = form.getId();
        WarehouseVO warehouseVO = warehouseMapper.findWarehouseById(id);
        if(ObjectUtil.isEmpty(warehouseVO)){
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，没有找到该仓库");
        }
        Warehouse warehouse = ConvertUtil.convert(warehouseVO, Warehouse.class);
        Integer status = form.getStatus();
        if(status.equals(WarehouseStatusEnum.DISABLE.getCode())){
            warehouse.setStatus(WarehouseStatusEnum.DISABLE.getCode());
        }else if(status.equals(WarehouseStatusEnum.ENABLE.getCode())){
            warehouse.setStatus(WarehouseStatusEnum.ENABLE.getCode());
        }else{
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，输入的状态有误");
        }
        this.saveOrUpdate(warehouse);
    }

    @Override
    public void saveWarehouse(SaveWarehouseForm form) {

    }

    @Override
    public WarehouseVO findWarehouseById(Long id) {
        return warehouseMapper.findWarehouseById(id);
    }
}
