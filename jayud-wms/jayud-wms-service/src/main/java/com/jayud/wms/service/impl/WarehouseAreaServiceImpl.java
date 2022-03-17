package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.QueryWarehouseAreaForm;
import com.jayud.wms.model.bo.WarehouseAreaForm;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.model.po.WarehouseArea;
import com.jayud.wms.mapper.WarehouseAreaMapper;
import com.jayud.wms.service.IWarehouseAreaService;
import com.jayud.wms.service.IWarehouseService;
import com.jayud.wms.service.IWmsMaterialBasicInfoService;
import com.jayud.wms.service.IWmsMaterialToLoactionRelationService;
import com.jayud.wms.model.vo.WarehouseAreaVO;
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

/**
 * 库区信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Service
public class WarehouseAreaServiceImpl extends ServiceImpl<WarehouseAreaMapper, WarehouseArea> implements IWarehouseAreaService {


//    @Autowired
//    private WarehouseAreaMapper warehouseAreaMapper;

    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsMaterialToLoactionRelationService wmsMaterialToLoactionRelationService;
    @Autowired
    private IWarehouseService warehouseService;

    @Override
    public IPage<WarehouseAreaVO> selectPage(QueryWarehouseAreaForm warehouseArea,
                                             Integer pageNo,
                                             Integer pageSize,
                                             HttpServletRequest req){

        Page<WarehouseAreaVO> page=new Page<WarehouseAreaVO>(pageNo,pageSize);
        IPage<WarehouseAreaVO> pageList= baseMapper.pageList(page, warehouseArea);
        return pageList;
    }

    @Override
    public List<WarehouseAreaVO> selectList(WarehouseArea warehouseArea){
        return baseMapper.list(warehouseArea);
    }

    @Override
    public WarehouseArea getWarehouseAreaByName(String name, Long warehouseId) {
        QueryWrapper<WarehouseArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseArea::getName,name);
        queryWrapper.lambda().eq(WarehouseArea::getIsDeleted,0);
        queryWrapper.lambda().eq(WarehouseArea::getWarehouseId,warehouseId);
        return this.getOne(queryWrapper);
    }

    @Override
    public WarehouseArea getWarehouseAreaByCode(String name, Long warehouseId) {
        QueryWrapper<WarehouseArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WarehouseArea::getCode,name);
        queryWrapper.lambda().eq(WarehouseArea::getIsDeleted,0);
        queryWrapper.lambda().eq(WarehouseArea::getWarehouseId,warehouseId);
        return this.getOne(queryWrapper);
    }

    @Override
    public BaseResult saveOrUpdateWarehouseArea(WarehouseAreaForm warehouseArea) {
        WarehouseArea warehouseArea1 = ConvertUtil.convert(warehouseArea, WarehouseArea.class);
        boolean isAdd = true;
        if (warehouseArea1.getId() != null){
            isAdd = false;
            if (warehouseArea1.getStatus() == 0){
                BaseResult recommendResult = checkRecommend(warehouseArea1.getWarehouseId(),warehouseArea1.getCode());
                if (!recommendResult.isSuccess()){
                    return recommendResult;
                }
            }
        }
        boolean result = this.saveOrUpdate(warehouseArea1);
        if(result){
            log.warn("新增或修改库区成功");
        }
        if (isAdd){
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    @Override
    public void deleteById(List<Long> ids) {
//        List<WarehouseArea> list = new ArrayList<>();
        for (Long id : ids) {
            WarehouseArea warehouseArea = new WarehouseArea();
            warehouseArea.setId(id);
            warehouseArea.setIsDeleted(1);
            warehouseArea.setUpdateBy(CurrentUserUtil.getUsername());
            warehouseArea.setUpdateTime(new Date());
//            list.add(warehouseArea);
            boolean result = this.updateById(warehouseArea);
            if(result){
                log.warn("删除库区成功");
            }
        }

    }

    @Override
    public List<WarehouseArea> getWarehouseAreaByWarehouseId(Long id) {
        QueryWrapper<WarehouseArea> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(WarehouseArea::getWarehouseId,id);
        queryWrapper.lambda().eq(WarehouseArea::getIsDeleted,0);
        return this.list(queryWrapper);
    }

    @Override
    public List<WarehouseArea> getWarehouseAreaByWarehouseIdAndStatus(Long id) {
        QueryWrapper<WarehouseArea> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(WarehouseArea::getWarehouseId,id);
        queryWrapper.lambda().eq(WarehouseArea::getIsDeleted,0);
        queryWrapper.lambda().eq(WarehouseArea::getStatus,1);
        return this.list(queryWrapper);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryWarehouseAreaForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWarehouseAreaForExcel(paramMap);
    }


    /**
     * @description 判断是否被上架推荐
     * @author  ciro
     * @date   2022/1/12 15:47
     * @param: warehouseId  仓库id
     * @param: warehouseAreaCode    库区编号
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkRecommend(long warehouseId,String warehouseAreaCode){
        Warehouse warehouse = warehouseService.getById(warehouseId);
        if (warehouse != null){
            if (warehouse.getStatus() == 0){
                return BaseResult.error("仓库已被冻结！");
            }
            BaseResult baseResult = wmsMaterialBasicInfoService.checkRecommend(warehouse.getCode(),warehouseAreaCode);
            if (!baseResult.isSuccess()){
                return baseResult;
            }
            BaseResult warehouseResult = wmsMaterialToLoactionRelationService.checkRecommend(warehouse.getCode(),warehouseAreaCode,null);
            if (!warehouseResult.isSuccess()){
                return warehouseResult;
            }
            return BaseResult.ok();
        }
        return BaseResult.error("仓库已被删除！");
    }

}
