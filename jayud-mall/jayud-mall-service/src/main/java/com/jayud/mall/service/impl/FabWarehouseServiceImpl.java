package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.FabWarehouseMapper;
import com.jayud.mall.model.bo.FabWarehouseArgsForm;
import com.jayud.mall.model.bo.FabWarehouseForm;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import com.jayud.mall.model.po.FabWarehouse;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.service.IFabWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 应收/FBA仓库 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class FabWarehouseServiceImpl extends ServiceImpl<FabWarehouseMapper, FabWarehouse> implements IFabWarehouseService {

    @Autowired
    FabWarehouseMapper fabWarehouseMapper;

    @Override
    public IPage<FabWarehouseVO> findFabWarehouseByPage(QueryFabWarehouseForm form) {
        //定义分页参数
        Page<FabWarehouse> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<FabWarehouseVO> pageInfo = fabWarehouseMapper.findFabWarehouseByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<FabWarehouseVO> findfabWarehouse(FabWarehouseArgsForm form) {
        QueryWrapper<FabWarehouse> queryWrapper = new QueryWrapper<>();
        String warehouseCode = form.getWarehouseCode();
        if(warehouseCode != null && warehouseCode != ""){
            queryWrapper.eq("warehouse_code", warehouseCode);
        }
        String warehouseName = form.getWarehouseName();
        if(warehouseName != null && warehouseName != ""){
            queryWrapper.eq("warehouse_name", warehouseName);
        }
        List<FabWarehouse> list = this.list(queryWrapper);
        List<FabWarehouseVO> fabWarehouseVOS = ConvertUtil.convertList(list, FabWarehouseVO.class);
        return fabWarehouseVOS;
    }

    @Override
    public CommonResult saveFabWarehouse(FabWarehouseForm form) {
        FabWarehouse fabWarehouse = ConvertUtil.convert(form, FabWarehouse.class);
        Integer id = fabWarehouse.getId();
        String warehouseCode = fabWarehouse.getWarehouseCode();
        if(ObjectUtil.isNotEmpty(id)){
            QueryWrapper<FabWarehouse> qw = new QueryWrapper<>();
            qw.ne("id", id);
            qw.eq("warehouse_code", warehouseCode);
            List<FabWarehouse> list = this.list(qw);
            if(CollUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "仓库代码已存在");
            }
        }else{
            QueryWrapper<FabWarehouse> qw = new QueryWrapper<>();
            qw.eq("warehouse_code", warehouseCode);
            List<FabWarehouse> list = this.list(qw);
            if(CollUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "仓库代码已存在");
            }
        }
        this.saveOrUpdate(fabWarehouse);
        return CommonResult.success("保存成功");
    }

    @Override
    public CommonResult deleteFabWarehouse(Integer id) {
        this.removeById(id);
        return CommonResult.success("删除成功");
    }

    @Override
    public CommonResult auditFabWarehouse(FabWarehouseForm form) {
        return CommonResult.success("审核成功");
    }
}
