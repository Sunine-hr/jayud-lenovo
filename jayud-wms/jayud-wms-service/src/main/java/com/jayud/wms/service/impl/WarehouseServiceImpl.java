package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.QueryWarehouseForm;
import com.jayud.wms.model.bo.WarehouseForm;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.mapper.WarehouseMapper;
import com.jayud.wms.service.IWarehouseProcessConfService;
import com.jayud.wms.service.IWarehouseService;
import com.jayud.wms.service.IWmsMaterialBasicInfoService;
import com.jayud.wms.service.IWmsMaterialToLoactionRelationService;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息表 服务实现类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {


    //    @Autowired
//    private WarehouseMapper warehouseMapper;
    @Autowired
    private IWarehouseProcessConfService warehouseProcessConfService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsMaterialToLoactionRelationService wmsMaterialToLoactionRelationService;


    @Override
    public IPage<Warehouse> selectPage(QueryWarehouseForm queryWarehouseForm,
                                       Integer pageNo,
                                       Integer pageSize,
                                       HttpServletRequest req) {
        Page<Warehouse> page = new Page<Warehouse>(pageNo, pageSize);
//        IPage<WarehouseForm> pageList= warehouseMapper.selectPage(page, warehouse);
        IPage<Warehouse> pageList = baseMapper.pageList(page, queryWarehouseForm);

        return pageList;
    }

    @Override
    public List<Warehouse> selectList(QueryWarehouseForm warehouse) {
//        return warehouseMapper.selectList(warehouse);
        return baseMapper.list(warehouse);
    }

    @Override
    public Warehouse getWarehouseByName(String name) {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Warehouse::getName, name);
        queryWrapper.lambda().eq(Warehouse::getIsDeleted, 0);
        return this.getOne(queryWrapper);
    }

    @Override
    public Warehouse getWarehouseByCode(String name) {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Warehouse::getCode, name);
        queryWrapper.lambda().eq(Warehouse::getIsDeleted, 0);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult saveOrUpdateWarehouse(WarehouseForm warehouse) {
        Warehouse warehouse1 = ConvertUtil.convert(warehouse, Warehouse.class);
        boolean isAdd = true;
        if (warehouse1.getId() != null){
            isAdd = false;
            //保存为禁用
            if (warehouse1.getStatus() == 0){
                BaseResult recommendResult = checkRecommend(warehouse1.getCode());
                if (!recommendResult.isSuccess()){
                    return recommendResult;
                }
            }
        }
        boolean result = this.saveOrUpdate(warehouse1);
        if (result) {
            if (warehouse.getId() == null) {
                warehouseProcessConfService.initConfig(warehouse1.getId());
            }
            log.warn("新增或修改仓库成功");
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    @Override
    public void deleteById(List<Long> ids) {
        for (Long id : ids) {
            Warehouse warehouse = new Warehouse();
            warehouse.setId(id);
            warehouse.setIsDeleted(1);
            warehouse.setUpdateBy(CurrentUserUtil.getUsername());
            warehouse.setUpdateTime(new Date());
            boolean result = this.updateById(warehouse);
            if (result) {
                log.warn("删除仓库成功");
            }
        }

    }

    @Override
    public List<Warehouse> getWarehouse() {
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Warehouse::getIsDeleted, 0);
        queryWrapper.lambda().eq(Warehouse::getStatus, 1);
        return this.list(queryWrapper);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWarehouseForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWarehouseForExcel(paramMap);
    }

    @Override
    public Warehouse selectByWarehouseMsg(Long warehouseId, String warehouseCode) {
        if (warehouseId == null && StringUtils.isBlank(warehouseCode)){
            return null;
        }
        LambdaQueryWrapper<Warehouse> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null){
            lambdaQueryWrapper.eq(Warehouse::getId,warehouseId);
        }
        if (StringUtils.isNotBlank(warehouseCode)){
            lambdaQueryWrapper.eq(Warehouse::getCode,warehouseCode);
        }
        return getOne(lambdaQueryWrapper);
    }

    /**
     * @description 根据货主id查询仓库信息
     * @author  ciro
     * @date   2022/2/9 17:46
     * @param: owerId
     * @return: com.jyd.component.commons.result.Result
     **/
    @Override
    public BaseResult queryWarehouseByOwerId(Long owerId) {
        List<Warehouse> warehouseList = this.baseMapper.queryWarehouseByOwerId(owerId);
        return BaseResult.ok(warehouseList);
    }

    /**
     * @description 检查仓库是否被上架推荐
     * @author  ciro
     * @date   2022/1/12 15:38
     * @param: warehouseCode
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkRecommend(String warehouseCode){
        BaseResult baseResult = wmsMaterialBasicInfoService.checkRecommend(warehouseCode,null);
        if (!baseResult.isSuccess()){
            return baseResult;
        }
        BaseResult warehouseResult = wmsMaterialToLoactionRelationService.checkRecommend(warehouseCode,null,null);
        if (!warehouseResult.isSuccess()){
            return warehouseResult;
        }
        return BaseResult.ok();
    }

}
