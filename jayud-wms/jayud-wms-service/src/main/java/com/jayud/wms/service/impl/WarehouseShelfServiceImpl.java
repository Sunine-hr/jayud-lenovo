package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.model.po.WarehouseShelf;
import com.jayud.wms.mapper.InventoryDetailMapper;
import com.jayud.wms.mapper.WarehouseLocationMapper;
import com.jayud.wms.mapper.WarehouseShelfMapper;
import com.jayud.wms.service.IWarehouseShelfService;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 货架信息 服务实现类
 *
 * @author jyd
 * @since 2022-03-05
 */
@Service
public class WarehouseShelfServiceImpl extends ServiceImpl<WarehouseShelfMapper, WarehouseShelf> implements IWarehouseShelfService {


    @Autowired
    private WarehouseShelfMapper warehouseShelfMapper;
    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;

    @Override
    public IPage<WarehouseShelf> selectPage(WarehouseShelf warehouseShelf,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WarehouseShelf> page=new Page<WarehouseShelf>(pageNo, pageSize);
        IPage<WarehouseShelf> pageList= warehouseShelfMapper.pageList(page, warehouseShelf);
        //计算并设值 1、可用库位容量 2、总库位容量
        List<WarehouseShelf> warehouseShelfList = pageList.getRecords();
        warehouseShelfList.forEach(w ->{
            Long warehouseId = w.getWarehouseId();//仓库id
            Long warehouseAreaId = w.getWarehouseAreaId();//库区id
            Long shelfId = w.getId();//货架id

            BigDecimal totalLocationCapacity = new BigDecimal("0");//总库位容量
            BigDecimal usedLocationCapacity = new BigDecimal("0");//已使用的库位容量
            BigDecimal availableLocationCapacity = new BigDecimal("0");//可用库位容量

            Map<String, Object> shelfMap = new HashMap<>();
            shelfMap.put("warehouseId", warehouseId);
            shelfMap.put("warehouseAreaId", warehouseAreaId);
            shelfMap.put("shelfId", shelfId);
            //1、根据货架信息 查询货架下 所有库位的容量
            List<WarehouseLocation> warehouseLocationList = warehouseLocationMapper.selectLocationCapacityByShelfId(shelfMap);

            for (int k=0; k<warehouseLocationList.size(); k++){
                WarehouseLocation warehouseLocation = warehouseLocationList.get(k);
                BigDecimal locationCapacity = warehouseLocation.getLocationCapacity();//单个库位的容量
                //累加 统计总容量
                totalLocationCapacity = totalLocationCapacity.add(locationCapacity);
            }


            List<Long> warehouseLocationIds = warehouseLocationList.stream().map(warehouseLocation -> warehouseLocation.getId()).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(warehouseLocationIds)){
                shelfMap.put("warehouseLocationIds", warehouseLocationIds);
            }else{
                warehouseLocationIds = Arrays.asList(0L);
                shelfMap.put("warehouseLocationIds", warehouseLocationIds);
            }
            //2、根据货架库位信息 查询库位下 已经使用的库位容量（PS：这里获取的 库存明细的 可用量）
            List<InventoryDetail> inventoryDetailList = inventoryDetailMapper.selectLocationCapacityByshelf(shelfMap);
            for (int k=0; k<inventoryDetailList.size(); k++){
                InventoryDetail inventoryDetail = inventoryDetailList.get(k);
                BigDecimal usableCount = inventoryDetail.getUsableCount();
                //累加 统计已使用的库位容量
                usedLocationCapacity = usedLocationCapacity.add(usableCount);
            }

            //可用库位容量 = 总库位容量 - 已使用的库位容量
            availableLocationCapacity = totalLocationCapacity.subtract(usedLocationCapacity);

            w.setTotalLocationCapacity(totalLocationCapacity);
            w.setUsedLocationCapacity(usedLocationCapacity);
            w.setAvailableLocationCapacity(availableLocationCapacity);

        });
        return pageList;
    }

    @Override
    public List<WarehouseShelf> selectList(WarehouseShelf warehouseShelf){
        return warehouseShelfMapper.list(warehouseShelf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarehouseShelf saveOrUpdateWarehouseShelf(WarehouseShelf warehouseShelf) {
        Long id = warehouseShelf.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            warehouseShelf.setCreateBy(CurrentUserUtil.getUsername());
            warehouseShelf.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
            //warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, warehouseShelf.getCode());
            //warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
            //List<WarehouseShelf> list = this.list(warehouseShelfQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        }else{
            //修改 --> update 更新人、更新时间
            warehouseShelf.setUpdateBy(CurrentUserUtil.getUsername());
            warehouseShelf.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<WarehouseShelf> warehouseShelfQueryWrapper = new QueryWrapper<>();
            //warehouseShelfQueryWrapper.lambda().ne(WarehouseShelf::getId, id);
            //warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getCode, warehouseShelf.getCode());
            //warehouseShelfQueryWrapper.lambda().eq(WarehouseShelf::getIsDeleted, 0);
            //List<WarehouseShelf> list = this.list(warehouseShelfQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(warehouseShelf);
        return warehouseShelf;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delWarehouseShelf(Long id) {
        WarehouseShelf warehouseShelf = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(warehouseShelf)){
            throw new IllegalArgumentException("货架信息不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        warehouseShelf.setUpdateBy(CurrentUserUtil.getUsername());
        warehouseShelf.setUpdateTime(new Date());
        warehouseShelf.setIsDeleted(true);
        this.saveOrUpdate(warehouseShelf);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWarehouseShelfForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWarehouseShelfForExcel(paramMap);
    }

    @Override
    public WarehouseShelf getWarehouseShelfBycode(String code) {
        QueryWrapper<WarehouseShelf> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseShelf::getCode,code);
        queryWrapper.lambda().eq(WarehouseShelf::getIsDeleted,0);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<WarehouseShelf> getDataByWarehouseAreaId(Long id) {
        QueryWrapper<WarehouseShelf> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(WarehouseShelf::getWarehouseAreaId,id);
        queryWrapper.lambda().eq(WarehouseShelf::getIsDeleted,0);
        queryWrapper.lambda().eq(WarehouseShelf::getStatus,1);
        return this.list(queryWrapper);
    }

}
