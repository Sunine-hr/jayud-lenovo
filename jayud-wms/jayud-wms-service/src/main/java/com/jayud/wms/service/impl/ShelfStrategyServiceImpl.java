package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.ShelfStrategyMapper;
import com.jayud.wms.model.po.ShelfStrategy;
import com.jayud.wms.model.po.Warehouse;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.model.po.WmsOwerInfo;
import com.jayud.wms.service.IShelfStrategyService;
import com.jayud.wms.service.IWarehouseService;
import com.jayud.wms.service.IWmsMaterialBasicInfoService;
import com.jayud.wms.service.IWmsOwerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 上架策略 服务实现类
 *
 * @author jyd
 * @since 2022-01-13
 */
@Service
public class ShelfStrategyServiceImpl extends ServiceImpl<ShelfStrategyMapper, ShelfStrategy> implements IShelfStrategyService {


    @Autowired
    private ShelfStrategyMapper shelfStrategyMapper;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsOwerInfoService wmsOwerInfoService;
    @Autowired
    private IWarehouseService warehouseService;

    @Override
    public IPage<ShelfStrategy> selectPage(ShelfStrategy shelfStrategy,
                                           Integer pageNo,
                                           Integer pageSize,
                                           HttpServletRequest req) {

        Page<ShelfStrategy> page = new Page<ShelfStrategy>(pageNo, pageSize);
        IPage<ShelfStrategy> pageList = shelfStrategyMapper.pageList(page, shelfStrategy);
        return pageList;
    }

    @Override
    public List<ShelfStrategy> selectList(ShelfStrategy shelfStrategy) {
        return shelfStrategyMapper.list(shelfStrategy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShelfStrategy saveOrUpdateShelfStrategy(ShelfStrategy shelfStrategy) {
        Long id = shelfStrategy.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            shelfStrategy.setCreateBy(CurrentUserUtil.getUsername());
            shelfStrategy.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<ShelfStrategy> shelfStrategyQueryWrapper = new QueryWrapper<>();
            //shelfStrategyQueryWrapper.lambda().eq(ShelfStrategy::getCode, shelfStrategy.getCode());
            //shelfStrategyQueryWrapper.lambda().eq(ShelfStrategy::getIsDeleted, 0);
            //List<ShelfStrategy> list = this.list(shelfStrategyQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            shelfStrategy.setUpdateBy(CurrentUserUtil.getUsername());
            shelfStrategy.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<ShelfStrategy> shelfStrategyQueryWrapper = new QueryWrapper<>();
            //shelfStrategyQueryWrapper.lambda().ne(ShelfStrategy::getId, id);
            //shelfStrategyQueryWrapper.lambda().eq(ShelfStrategy::getCode, shelfStrategy.getCode());
            //shelfStrategyQueryWrapper.lambda().eq(ShelfStrategy::getIsDeleted, 0);
            //List<ShelfStrategy> list = this.list(shelfStrategyQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(shelfStrategy);
        return shelfStrategy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delShelfStrategy(int id) {
        ShelfStrategy shelfStrategy = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(shelfStrategy)) {
            throw new IllegalArgumentException("上架策略不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        shelfStrategy.setUpdateBy(CurrentUserUtil.getUsername());
        shelfStrategy.setUpdateTime(new Date());
        shelfStrategy.setIsDeleted(true);
        this.saveOrUpdate(shelfStrategy);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryShelfStrategyForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryShelfStrategyForExcel(paramMap);
    }

    @Override
    public Map<String, String> getPolicyPrioritys(Set<String> materialCodes) {
//        List<WmsMaterialBasicInfo> wmsMaterialBasicInfos = this.wmsMaterialBasicInfoService.getByMaterialCodes(materialCodes);
//        Map<String, String> map = new HashMap<>();
//        for (WmsMaterialBasicInfo wmsMaterialBasicInfo : wmsMaterialBasicInfos) {
//            if (wmsMaterialBasicInfo.getShelfStrategyId() != null) {
//                map.put(wmsMaterialBasicInfo.getMaterialCode(), 1 + "~" + wmsMaterialBasicInfo.getShelfStrategyId());
//                continue;
//            }
//
//            WmsOwerInfo owerInfo = this.wmsOwerInfoService.getById(wmsMaterialBasicInfo.getOwerId());
//            if (owerInfo.getShelfStrategyId() != null) {
//                map.put(wmsMaterialBasicInfo.getMaterialCode(), 2 + "~" + owerInfo.getShelfStrategyId());
//                continue;
//            }
//        }
        return null;
    }

    @Override
    public Map<String, String> getPolicyPriority(String materialCode, Long warehouseId) {
        List<WmsMaterialBasicInfo> wmsMaterialBasicInfos = this.wmsMaterialBasicInfoService.getByCondition(new WmsMaterialBasicInfo().setMaterialCode(materialCode));
        if (CollectionUtil.isEmpty(wmsMaterialBasicInfos)) {
            throw new ServiceException("不存在该物料");
        }

        Map<String, String> map = new HashMap<>();
        for (WmsMaterialBasicInfo wmsMaterialBasicInfo : wmsMaterialBasicInfos) {
            if (wmsMaterialBasicInfo.getShelfStrategyId() != null) {
                map.put(wmsMaterialBasicInfo.getMaterialCode(), 1 + "~" + wmsMaterialBasicInfo.getShelfStrategyId());
                continue;
            }

            WmsOwerInfo owerInfo = this.wmsOwerInfoService.getById(wmsMaterialBasicInfo.getOwerId());
            if (owerInfo.getShelfStrategyId() != null) {
                map.put(wmsMaterialBasicInfo.getMaterialCode(), 2 + "~" + owerInfo.getShelfStrategyId());
                continue;
            }
            Warehouse warehouse = this.warehouseService.getById(warehouseId);
            if (warehouse.getStockingStrategyId() != null) {
                map.put(wmsMaterialBasicInfo.getMaterialCode(), 3 + "~" + warehouse.getStockingStrategyId());
            }
        }
        return map;
    }

}
