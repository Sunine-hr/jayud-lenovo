package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.QueryInventoryForm;
import com.jayud.wms.model.bo.QueryWarehouseLocationForm;
import com.jayud.wms.model.bo.WarehouseLocationForm;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.model.po.WarehouseLocation;
import com.jayud.wms.mapper.WarehouseLocationMapper;
import com.jayud.wms.service.*;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库位信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Service
public class WarehouseLocationServiceImpl extends ServiceImpl<WarehouseLocationMapper, WarehouseLocation> implements IWarehouseLocationService {


    @Autowired
    private IInventoryDetailService inventoryDetailService;

    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsMaterialToLoactionRelationService wmsMaterialToLoactionRelationService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IWarehouseAreaService warehouseAreaService;

    @Override
    public IPage<WarehouseLocationVO> selectPage(QueryWarehouseLocationForm warehouseLocation,
                                                 Integer pageNo,
                                                 Integer pageSize,
                                                 HttpServletRequest req){

        Page<WarehouseLocationVO> page=new Page<WarehouseLocationVO>(pageNo,pageSize);
        IPage<WarehouseLocationVO> pageList= baseMapper.pageList(page, warehouseLocation);
        return pageList;
    }

    @Override
    public List<WarehouseLocationVO> selectList(WarehouseLocation warehouseLocation){
        return baseMapper.list(warehouseLocation);
    }

    @Override
    public WarehouseLocation getWarehouseLocationByCode(String code, Long warehouseId, Long warehouseAreaId) {
        QueryWrapper<WarehouseLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseLocation::getCode,code);
        queryWrapper.lambda().eq(WarehouseLocation::getIsDeleted,0);
        queryWrapper.lambda().eq(WarehouseLocation::getWarehouseId,warehouseId);
        queryWrapper.lambda().eq(WarehouseLocation::getWarehouseAreaId,warehouseAreaId);
        queryWrapper.lambda().eq(WarehouseLocation::getStatus,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public BaseResult saveOrUpdateWarehouseLocation(WarehouseLocationForm warehouseLocation) {
        WarehouseLocation warehouseLocation1 = ConvertUtil.convert(warehouseLocation, WarehouseLocation.class);
        boolean isAdd = true;
        if (warehouseLocation1.getId() != null){
            isAdd = false;
            if (warehouseLocation1.getStatus() == 0){
                BaseResult recommendResult = checkRecommend(warehouseLocation1.getWarehouseId(), warehouseLocation1.getWarehouseAreaId(),warehouseLocation1.getCode());
                if (!recommendResult.isSuccess()){
                    return recommendResult;
                }
            }
        }
        boolean result = this.saveOrUpdate(warehouseLocation1);
        if(result){
            log.warn("新增或修改库位成功");
        }
        if (isAdd){
            BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    @Override
    public void deleteById(List<Long> ids) {
//        List<WarehouseLocation> list = new ArrayList<>();
        for (Long id : ids) {
            WarehouseLocation warehouseLocation = new WarehouseLocation();
            warehouseLocation.setId(id);
            warehouseLocation.setIsDeleted(1);
            warehouseLocation.setUpdateBy(CurrentUserUtil.getUsername());
            warehouseLocation.setUpdateTime(new Date());
//            list.add(warehouseLocation);
            boolean result = this.updateById(warehouseLocation);
            if(result){
                log.warn("删除库区成功");
            }
        }

    }

    @Override
    public List<WarehouseLocation> getWarehouseLocationByWarehouseAreaId(Long id) {
        QueryWrapper<WarehouseLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseLocation::getWarehouseAreaId,id);
        queryWrapper.lambda().eq(WarehouseLocation::getIsDeleted,0);
        return this.list(queryWrapper);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWarehouseLocationForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWarehouseLocationForExcel(paramMap);
    }

    @Override
    public List<WarehouseLocationVO> queryWarehouseLocation(QueryInventoryForm form) {
        QueryWrapper<InventoryDetail> inventoryDetailQueryWrapper = new QueryWrapper<>();
        inventoryDetailQueryWrapper.lambda().ne(InventoryDetail::getExistingCount, 0);//现有量 不为0
        inventoryDetailQueryWrapper.lambda().eq(InventoryDetail::getIsDeleted, 0);
        inventoryDetailQueryWrapper.lambda().select(InventoryDetail::getWarehouseLocationId);
        inventoryDetailQueryWrapper.lambda().groupBy(InventoryDetail::getWarehouseLocationId);
        List<InventoryDetail> list = inventoryDetailService.list(inventoryDetailQueryWrapper);
        List<Long> notIds = list.stream().map(k -> {
            return k.getWarehouseLocationId();
        }).collect(Collectors.toList());
        form.setNotIds(notIds);
        return this.baseMapper.queryWarehouseLocation(form);
    }

    @Override
    public List<WarehouseLocation> getWarehouseLocationByWarehouseShelfId(Long id) {
        QueryWrapper<WarehouseLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseLocation::getShelfId,id);
        queryWrapper.lambda().eq(WarehouseLocation::getIsDeleted,0);
        return this.list(queryWrapper);
    }

    @Override
    public WarehouseLocation getWarehouseLocationByCodeAndStatus(String code) {
        QueryWrapper<WarehouseLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseLocation::getCode,code);
        queryWrapper.lambda().eq(WarehouseLocation::getIsDeleted,0);
        queryWrapper.lambda().eq(WarehouseLocation::getStatus,0);
        return this.getOne(queryWrapper);
    }

    /**
     * @description 判断是否被推荐
     * @author  ciro
     * @date   2022/1/12 16:04
     * @param: warehouseId          仓库id
     * @param: warehouseAreaId      库区id
     * @param: locationCode         库位编号
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkRecommend(long warehouseId,long warehouseAreaId,String locationCode){
        Warehouse warehouse = warehouseService.getById(warehouseId);
        if (warehouse != null){
            if (warehouse.getStatus() == 0){
                return BaseResult.error("仓库已关闭！");
            }
            WarehouseArea warehouseArea = warehouseAreaService.getById(warehouseAreaId);
            if (warehouseArea != null){
                if (warehouseArea.getStatus() == 0){
                    return BaseResult.error("库区已关闭！");
                }
                BaseResult recommend = wmsMaterialToLoactionRelationService.checkRecommend(warehouse.getCode(),warehouseArea.getCode(),locationCode);
                return recommend;
            }else {
                return BaseResult.error("库区已被删除！");
            }
        }
        return BaseResult.error("仓库已被删除！");
    }


}
