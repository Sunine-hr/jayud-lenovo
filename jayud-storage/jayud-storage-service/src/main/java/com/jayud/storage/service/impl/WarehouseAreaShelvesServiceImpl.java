package com.jayud.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.OperationForm;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelves2Form;
import com.jayud.storage.model.bo.QueryWarehouseAreaShelvesForm;
import com.jayud.storage.model.bo.WarehouseAreaShelvesForm;
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
        WarehouseAreaShelves warehouseAreaShelves = ConvertUtil.convert(form, WarehouseAreaShelves.class);
        warehouseAreaShelves.setCreateTime(LocalDateTime.now());
        warehouseAreaShelves.setCreateUser(UserOperator.getToken());
        warehouseAreaShelves.setStatus(1);
        boolean b = this.saveOrUpdate(warehouseAreaShelves);
        if(!b){
            return false;
        }
        return true;
    }

    @Override
    public void operationWarehouseAreaShelves(OperationForm form) {
        WarehouseAreaShelves warehouseAreaShelves = this.baseMapper.selectById(form.getId());
        if(ObjectUtil.isEmpty(warehouseAreaShelves)){
            Asserts.fail(ResultEnum.OPR_FAIL, "操作失败，没有找到该仓库区域");
        }
        Warehouse warehouse = ConvertUtil.convert(warehouseAreaShelves, Warehouse.class);
        Integer status = form.getStatus();
        if(status.equals(WarehouseStatusEnum.DISABLE.getCode())){
            warehouse.setStatus(WarehouseStatusEnum.DISABLE.getCode());
        }else if(status.equals(WarehouseStatusEnum.ENABLE.getCode())){
            warehouse.setStatus(WarehouseStatusEnum.ENABLE.getCode());
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
}
